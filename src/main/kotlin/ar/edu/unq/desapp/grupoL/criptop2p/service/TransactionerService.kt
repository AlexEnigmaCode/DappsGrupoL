package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.*
import ar.edu.unq.desapp.grupoL.criptop2p.model.CriptoActivo
import ar.edu.unq.desapp.grupoL.criptop2p.model.Publicacion
import ar.edu.unq.desapp.grupoL.criptop2p.model.Transaccion
import ar.edu.unq.desapp.grupoL.criptop2p.model.Usuario
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.PublicacionRepository
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

    @Autowired
    lateinit var  publisherService: PublisherService


    @Transactional
    fun procesarTransaccion(id: Long, publicacion: Publicacion,  cotizacionActual : Double  ): Transaccion {

        if (isCanceled(publicacion, cotizacionActual)) {
            throw Exception ("transaccion ${publicacion.id}  cancelada, no cumple los requerimientos según la cotizacion actual ")
        }

        return procesar(id, publicacion)
    }

    @Transactional
     fun procesar(id: Long,publicacion: Publicacion): Transaccion {
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
                  val deposito =  realizarTransferencia(transaccion)
                   notificarPago (transaccion, deposito)

               }
               "venta" -> {
                   transaccion.direccionEnvio = publicacion.usuario!!.cvu!!
                   transaccion.accion = Accion.CONFIRMAR_RECEPCION
                   val  cuenta  = mercadoPagoService.getCuenta(transaccion.direccionEnvio!!)
                   if  ( ! confirmarRecepcion(transaccion) ) {
                       throw Exception (" El monto depositado no es suficiente para realizar la transaccion" +
                               " o no se ha hecho el deposito en la cuenta  $cuenta")
                   }
                   enviarCriptoActivo(transaccion)
                  // finalizarTransaccion(transaccion)
                  finalizarTransaccion(publicacion)
                   }
           }

           return  transactionerRepository.save(transaccion)

           }

       catch (e: Exception) {
           throw ItemNotFoundException("User with Id:  $id not found")
       }
    }


    @Transactional
    fun cancelar(id: Long,publicacion: Publicacion): Transaccion {
        lateinit var direccionEnvio: String
        lateinit var accion: Accion

        try {

            val usuario = userService.findByID(id)
            usuario.descontarReputacion(20.0)
            val cantidadOperaciones = publicacion.usuario!!.cantidadOperaciones
            val reputacion = publicacion.usuario!!.reputacion

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
               "",
                Accion.CANCELAR,
                usuario
            )
            return transaccion
        } catch (e: Exception) {
            throw ItemNotFoundException("User with Id:  $id not found")
        }

    }



        @Transactional
        fun findByID(id: Long): Transaccion {
        val transaccion =  transactionerRepository.findById(id)
        if ( ! (transaccion.isPresent ))
        {throw ItemNotFoundException("Transaction with Id:  $id not found") }
        val newTransaccion=  transaccion.get()
        return newTransaccion

    }



    @Transactional
    fun deleteById(id: Long) {
        val transaccion =   transactionerRepository.findById(id)
        if ( ! (transaccion.isPresent ))
        {throw ItemNotFoundException("Transacción with Id:  $id not found") }
        transactionerRepository.deleteById(id)
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


   fun isCanceled(publicacion:Publicacion,cotizacionActual: Double): Boolean {
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


       fun confirmarRecepcion(transaccion:Transaccion):Boolean {
        val  cuenta  = mercadoPagoService.getCuenta(transaccion.direccionEnvio!!)
        val montoDepositado =  mercadoPagoService.consultarMonto(cuenta ,transaccion.usuarioSelector!!)
        return  ( montoDepositado >= transaccion.monto )
       }


     fun realizarTransferencia(transaccion:Transaccion) : Deposito{
        val  cuenta  = mercadoPagoService.getCuenta(transaccion.usuarioSelector!!.cvu!!)
        val deposito =   mercadoPagoService.depositar(cuenta, transaccion.monto, transaccion.usuario!! )
        return deposito
    }

    fun notificarPago (transaccion:Transaccion, deposito:Deposito){
          transaccion.usuarioSelector!!.notificar(deposito)

   }


   // fun finalizarTransaccion(transaccion:Transaccion){
       fun finalizarTransaccion(publicacion:Publicacion){
       // transactionerRepository.deleteById(transaccion.id!!)
        publisherService.deleteById(publicacion.id!!)

    }


    fun enviarCriptoActivo (transaccion:Transaccion){
        val walletAddress = transaccion.usuarioSelector!!.walletAddress!!
        val wallet = getVirtualWallet(walletAddress)
        guardarCriptoActivo(wallet,transaccion)
    }


    fun guardarCriptoActivo(wallet:VirtualWallet,transaccion:Transaccion) {
        if (existeCriptoActivo(transaccion.criptoactivo!!,wallet)) {
            val criptoActivoGuardado = wallet.criptoactivos.find { it.criptoActivo == transaccion.criptoactivo!! }
            criptoActivoGuardado!!.monto  += transaccion.monto

        }
        else {
            val  criptoActivo = CriptoActivoWalletMapper(transaccion.criptoactivo!!, transaccion.cotizacion,transaccion.cantidad!!,transaccion.monto)
            wallet.criptoactivos.add(criptoActivo)  }
    }


    fun existeCriptoActivo (criptoActivo: String,wallet: VirtualWallet):Boolean{
        val criptoActivoGuardado = wallet.criptoactivos.find { it.criptoActivo == criptoActivo }
        return  (criptoActivoGuardado != null)

    }



    fun createVirtualWallet (usuario:Usuario): VirtualWallet{
        val wallet =  VirtualWallet (usuario, criptoActivos )
        wallets.add(wallet)
        return wallet
    }

    fun getVirtualWallet(walletAddress:String): VirtualWallet{
        return  wallets.find { it.usuario.walletAddress == walletAddress} ?:  throw ItemNotFoundException("User with Virtual Wallet: $walletAddress not found")
    }


    fun criptoActivosDeLaVirtualWalletDeUsuario(usuario:Usuario): MutableList<CriptoActivoWalletMapper> {
        val wallet = getVirtualWallet( usuario.walletAddress!!)
        val criptoActivos = wallet.criptoactivos
        return criptoActivos
    }

    fun getCriptoActivoDeLaVirtualWalletDeUsuario(transaccion:Transaccion): CriptoActivoWalletMapper {
      val usuario = transaccion.usuarioSelector!!
      val criptoactivos = criptoActivosDeLaVirtualWalletDeUsuario(usuario)
      return  criptoactivos.find { it.criptoActivo == transaccion.criptoactivo } ?:throw ItemNotFoundException("Not found criptoActivo")
    }


    fun wallets(): MutableList<VirtualWallet> {
        return  wallets
    }

    @Transactional
    fun volumenOperadoEntreFechas(usuarioId:Long, fecha1: LocalDateTime, fecha2:LocalDateTime): VolumenCriptoActivoOperadoMapper{
        val transacciones = transacciones()
        val transaccionesParaUnUsuario = transaccionesParaUnUsuario (usuarioId,transacciones)
        val  transaccionesDeUsuarioEntreFechas =  transaccionesDeUnUsuarioEntreFechas(usuarioId,fecha1, fecha2,transaccionesParaUnUsuario)
        val  valorTotalOperado =  transaccionesDeUsuarioEntreFechas.sumOf { it.monto }
        val transaccionesCriptoActivos =  transaccionesAgrupadoPorCriptoActivos(transaccionesDeUsuarioEntreFechas)
        val criptoActivos =  volumenCriptoActivos( transaccionesCriptoActivos )

        return  VolumenCriptoActivoOperadoMapper(LocalDateTime.now(), valorTotalOperado, criptoActivos )
    }


    fun transacciones(): MutableList<Transaccion> {
        return  transactionerRepository.findAll().toMutableList()
    }

    fun transaccionesParaUnUsuario (usuarioId: Long,transacciones: MutableList<Transaccion>): MutableList<Transaccion> {
            transacciones.filter{ it.usuario!!.id == usuarioId  }.toMutableList()
           if (transacciones.isEmpty()){
               throw ItemNotFoundException ("No hay transacciones con el id de usuario $usuarioId ")
           }
        return transacciones
    }

    fun transaccionesDeUnUsuarioEntreFechas(usuarioId: Long,fecha1:LocalDateTime, fecha2:LocalDateTime, transacciones: MutableList<Transaccion>):MutableList<Transaccion>{
        transacciones.filter { entreFechas(it.diahora!!,fecha1,fecha2) }.toMutableList()
        if (transacciones.isEmpty()){
            throw ItemNotFoundException ("No hay transacciones que se encuentre entre las fechas $fecha1 y $fecha2 ")
        }
        return transacciones
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
         return transaccionesCriptoActivos.sortedBy { it.criptoActivo }.toMutableList()
    }


    fun volumenCriptoActivos( transaccionesCriptoActivos : MutableList<TransaccionCriptoActivoMapper> ): MutableList<CriptoActivoWalletMapper> {
        lateinit var criptoActivo :CriptoActivoWalletMapper
        val  criptoActivos = mutableListOf<CriptoActivoWalletMapper>()

       for (cripto in transaccionesCriptoActivos){
          val cantidad = cripto.criptoActivos.sumOf { it.cantidad }
         val  cotizacionActual = 100.0
         val monto =  cantidad * cotizacionActual
          criptoActivo =  CriptoActivoWalletMapper(cripto.criptoActivo,cotizacionActual, cantidad,monto)
          criptoActivos.add(criptoActivo)
        }
          return  criptoActivos.sortedBy { it.criptoActivo }.toMutableList()


    }




    fun entreFechas(fecha:LocalDateTime, fecha1:LocalDateTime, fecha2:LocalDateTime): Boolean{
        return (   fecha.isBefore(fecha2) || fecha.isEqual(fecha2)  )
                &&  (fecha.isAfter(fecha2) || fecha.isEqual(fecha1))
    }
}