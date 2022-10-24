package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.*
import ar.edu.unq.desapp.grupoL.criptop2p.model.Publicacion
import ar.edu.unq.desapp.grupoL.criptop2p.model.Transaccion
import ar.edu.unq.desapp.grupoL.criptop2p.model.Usuario
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.TransaccionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class TransactionerService {

    var  criptoActivos = mutableListOf<CriptoActivoWalletMapper>()
    var transacciones = mutableListOf<Transaccion>()
    val wallets = mutableListOf<VirtualWallet>()


    @Autowired
    private lateinit var userService: UserService


    @Autowired
    private lateinit var mercadoPagoService: MercadoPagoService

    @Autowired
    private  lateinit var consumer : ConsumerCriptoActivoMicroService

    @Autowired
    private lateinit var transactionerRepository: TransaccionRepository


    @Transactional
    fun procesarTransaccion(id: Long, publicacion: Publicacion,  cotizacionActual : Double  ): Transaccion {

        if (!isCanceled(publicacion, cotizacionActual)) {
            throw Exception ("transaccion ${publicacion.id}  cancelada, no cumple los requerimientos según la cotizacion actual ")
        }

        return procesar(id, publicacion)
    }

    private  fun procesar(id: Long,publicacion: Publicacion): Transaccion {
        lateinit var  direccionEnvio:String
        lateinit var accion :Accion

       try {

           val usuario = userService.findByID(id)
           val cantidadOperaciones = publicacion.usuario!!.icrementarOperqaciones()
           val reputacion = incrementarReputacionSegunTiempo(publicacion.diahora!!)

           publicacion.usuario!!.incrementarReputacion(reputacion)
           usuario.incrementarReputacion(reputacion)


           val transaccion = Transaccion(
               0,
               LocalDateTime.now(),
               publicacion.criptoactivo,
               publicacion.cantidad,
               publicacion.cotizacion,
               publicacion.monto,
               publicacion.usuario,
               publicacion.operacion,
               cantidadOperaciones,
               reputacion,
               direccionEnvio,
               accion,
               usuario
           )


           when (publicacion.operacion) {

               "compra" -> {
                   transaccion.direccionEnvio = publicacion.usuario!!.walletAddress!!
                   transaccion.accion = Accion.REALIZAR_TRANSFERENCIA
                   this.realizarTransferencia(transaccion)
               }
               "venta" -> {
                   transaccion.direccionEnvio = publicacion.usuario!!.cvu!!
                   transaccion.accion = Accion.CONFIRMAR_RECEPCION
                   this.confirmarRecepcion(transaccion)
               }

           }
           return  transactionerRepository.save(transaccion)
       }
       catch (e: Exception) {
           throw ItemNotFoundException("User with Id:  $id not found")
       }




    }


    @Transactional
    fun cancelar (publicacion:Publicacion,usuario:Usuario){
        usuario.descontarReputacion(20.0)
    }

    private fun  incrementarReputacionSegunTiempo(diahora:LocalDateTime):Double {

        if (esFechaAnterior(LocalDateTime.now(), diahora)) {
            return 10.0
        }
        else {
            return 5.0
        }
    }





    private fun  esFechaAnterior(fechaActual:LocalDateTime, fechaPublicacion:LocalDateTime):Boolean{
        return  (fechaActual.isBefore(fechaPublicacion.plusMinutes(30)))

    }


    private fun isCanceled(publicacion:Publicacion,cotizacionActual: Double): Boolean {

        return  when    (publicacion.operacion) {
            "compra" -> {
               // cotizacionActual(publicacion.criptoactivo!!) > publicacion.cotizacion
                cotizacionActual > publicacion.cotizacion
            }
            else -> {
               // cotizacionActual (publicacion.criptoactivo!!)  < publicacion.cotizacion
                cotizacionActual  < publicacion.cotizacion
            }

        }

    }

    private fun  cotizacionActual(symbol:String): Double{
        val  criptoActivo = consumer.consumeBySymbol(symbol)
        return  criptoActivo.cotizacion!!.toDouble()
    }


    private fun realizarTransferencia(transaccion:Transaccion) {

        val deposito =  enviarDinero(transaccion)
        notificarPago (transaccion, deposito)

    }

    private fun confirmarRecepcion(transaccion:Transaccion) {
        val  cuenta  = mercadoPagoService.getCuenta(transaccion.direccionEnvio!!)
        val montoDepositado =  mercadoPagoService.consultarMonto(cuenta ,transaccion.usuarioSelector!!)
        if  ( montoDepositado < transaccion.monto ) {
            throw Exception (" El monto depositado no es suficiente para realizar la transaccion" +
                    " o no se ha hecho el deposito en la cuenta  $cuenta")
        }

        enviarCriptoActivo(transaccion)
    }






    private fun enviarDinero(transaccion:Transaccion) : Deposito{
        val  cuenta  = mercadoPagoService.getCuenta(transaccion.usuarioSelector!!.cvu!!)
        val deposito =   mercadoPagoService.depositar(cuenta, transaccion.monto, transaccion.usuario!! )
        return deposito
    }

    fun notificarPago (transaccion:Transaccion, deposito:Deposito){
        val notificacionesDeDeposito =  transaccion.usuarioSelector!!.notificacionesDeDeposito
        notificacionesDeDeposito.add(deposito)
    }


    fun enviarCriptoActivo (transaccion:Transaccion){
        val walletAddress = transaccion.usuarioSelector!!.walletAddress!!
        val wallet = getVirtualWallet(walletAddress)
        guardarCriptoActivo(wallet,transaccion)
    }



    fun guardarCriptoActivo(wallet:VirtualWallet,publicacion:Transaccion){
        val  criptoActivo = CriptoActivoWalletMapper(publicacion.criptoactivo!!, publicacion.cotizacion,publicacion.cantidad!!,publicacion.monto)
        agregarCriptoActivo(criptoActivo,wallet)
    }


    fun agregarCriptoActivo(criptoActivo: CriptoActivoWalletMapper, wallet: VirtualWallet) {
        if (existeCriptoActivo(criptoActivo,wallet)) {
            var criptoActivoGuardado = wallet.criptoactivos.find { it.criptoactivo == criptoActivo.criptoactivo }
            criptoActivoGuardado!!.monto  += criptoActivo.monto

        }
        else { wallet.criptoactivos.add(criptoActivo)  }
    }


    fun existeCriptoActivo (criptoActivo: CriptoActivoWalletMapper,wallet: VirtualWallet):Boolean{
        val criptoActivoGuardado = wallet.criptoactivos.find { it.criptoactivo == criptoActivo.criptoactivo }
        return  (criptoActivoGuardado != null)

    }




    fun createVirtualWallet (usuario:Usuario){
        val wallet =  VirtualWallet (usuario, criptoActivos )
        wallets.add(wallet)
    }

    fun getVirtualWallet(walletAddress:String): VirtualWallet{
        return  wallets.find { it.usuario.walletAddress == walletAddress} ?:  throw ItemNotFoundException("User with Virtual Wallet: $walletAddress not found")
    }

    fun wallets(): MutableList<VirtualWallet> {
        return  wallets
    }





    @Transactional
    fun volumenOperadoEntreFechas(usuarioId:Long, fecha1: LocalDateTime, fecha2:LocalDateTime  ): VolumenCriptoActivoOperadoMapper{

         val data =  informeData(usuarioId, fecha1, fecha2 )
        val transaccionesDeUsuarioEntreFechas = data.transacciones
        val transaccionesCriptoActivos =  transaccionesAgrupadoPorCriptoActivos(transaccionesDeUsuarioEntreFechas)
        val criptoActivos =  volumenCriptoActivos( transaccionesCriptoActivos )

        return VolumenCriptoActivoOperadoMapper(data.diahora,data.usuario, data.valorTotalOperados, criptoActivos )


    }


    fun informeData(usuarioId:Long, fecha1: LocalDateTime, fecha2:LocalDateTime  ): VolumenDataNapper{

        val  transaccionesDeUsuarioEntreFechas =  transaccionesDeUnUsuarioEntreFechas(usuarioId,fecha1, fecha2)
        val  usuario = transaccionesDeUsuarioEntreFechas.first().usuario!!
        val diahora = transaccionesDeUsuarioEntreFechas.first().diahora!!
        val usuarioView = UserViewMapper (usuario.id,usuario.name,usuario.surname,usuario.email,usuario.address,usuario.cvu,usuario.walletAddress)
        val  valorTotalOperado =  transaccionesDeUsuarioEntreFechas.sumOf { it.monto }

        return VolumenDataNapper(diahora,usuarioView,valorTotalOperado, transaccionesDeUsuarioEntreFechas )
    }



    fun transacciones(): MutableList<Transaccion> {
        return  transactionerRepository.findAll().toMutableList()
    }

    fun transaccionesParaUnUsuario (usuarioId: Long): MutableList<Transaccion> {
       val transacciones =   transacciones().filter{ it.usuario!!.id == usuarioId  }.toMutableList()
           if (transacciones.isEmpty()){
               throw ItemNotFoundException ("No hay transacciones con el id de usuario $usuarioId ")
           }
        return transacciones
    }

    fun transaccionesDeUnUsuarioEntreFechas(usuarioId: Long,fecha1:LocalDateTime, fecha2:LocalDateTime):MutableList<Transaccion>{
        val transacciones = transaccionesParaUnUsuario (usuarioId)
        val transaccionesEntreFechas = transacciones.filter { entreFechas(it.diahora!!,fecha1,fecha2) }.toMutableList()
        if (transaccionesEntreFechas.isEmpty()){
            throw ItemNotFoundException ("No hay transacciones que se encuentre entre las fechas $fecha1 y $fecha2 ")
        }
        return transaccionesEntreFechas

    }

    fun transaccionesAgrupadoPorCriptoActivos(transacciones : MutableList<Transaccion>): MutableList<TransaccionCriptoActivoMapper> {
        val transaccionesCriptoActivos = mutableListOf<TransaccionCriptoActivoMapper>()
        val  informeAgrupadosCriptoActivos =transacciones.groupBy { it.criptoactivo}
        val informecriptoActivos =   informeAgrupadosCriptoActivos.keys

        for ( criptoActivo in informecriptoActivos){
            val listaTransacciones =   informeAgrupadosCriptoActivos.get(criptoActivo)!!.toMutableList()
            val listacripto =  listaTransacciones.map {CriptoActivoWalletMapper (it.criptoactivo!!,it.cotizacion,it.cantidad!!,it.monto)}.toMutableList()
            val transaccionCriptoActivo =  TransaccionCriptoActivoMapper ( criptoActivo!!, listacripto)
            transaccionesCriptoActivos.add( transaccionCriptoActivo)

        }

         return transaccionesCriptoActivos

    }


    fun volumenCriptoActivos( transaccionesCriptoActivos : MutableList<TransaccionCriptoActivoMapper> ): MutableList<CriptoActivoWalletMapper> {
        lateinit var criptoActivo :CriptoActivoWalletMapper
        val  criptoActivos = mutableListOf<CriptoActivoWalletMapper>()

       for (cripto in transaccionesCriptoActivos){
          val cantidad = cripto.criptoActivos.sumOf { it.cantidad }
         val  cotizacion = 100.0
         val monto =  cantidad * cotizacion
          criptoActivo =  CriptoActivoWalletMapper(cripto.criptoActivo,cotizacion, cantidad,monto)
          criptoActivos.add(criptoActivo)
        }
           return  criptoActivos

    }




    private fun entreFechas(fecha:LocalDateTime, fecha1:LocalDateTime, fecha2:LocalDateTime): Boolean{
        return (   fecha.isBefore(fecha2) || fecha.isEqual(fecha2)  )
                &&  (fecha.isAfter(fecha2) || fecha.isEqual(fecha1))
    }



}