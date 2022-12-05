package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.*
import ar.edu.unq.desapp.grupoL.criptop2p.model.*
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.TransaccionRepository
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class TransactionerService {

     var  criptoActivos = mutableListOf<CriptoActivoWalletMapper>()
    var transacciones = mutableListOf<Transaccion>()
    val wallets = mutableListOf<VirtualWallet>()

    lateinit var  state : EstadoTransaccionService

    //@Autowired
   // protected lateinit var estadoTransaccionService: EstadoTransaccionService


    @Autowired
    private lateinit var userService: UserService


    @Autowired
    private lateinit var mercadoPagoService: MercadoPagoService

    @Autowired
    private  lateinit var consumer : ConsumerCriptoActivoMicroService

    @Autowired
    private lateinit var transactionerRepository: TransaccionRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    lateinit var  publisherService: PublisherService

  fun setActualState(actualState : EstadoTransaccionService){
      state = actualState
  }

  fun getActualState () : EstadoTransaccionService
  {return state }


    @Transactional
    fun procesarTransaccion(/*usuario: Usuario,*/ transaccion: Transaccion, cotizacionActual : Double  ): Transaccion {
       if (isCanceled(transaccion, cotizacionActual)) {
          // transaccion.state = EstadoCanceladoService()
          state = EstadoCanceladoService()
           throw Exception ("transaccion ${transaccion.id}  cancelada, no cumple los requerimientos según la cotizacion actual ")
        }
        return procesar(/*usuario,*/ transaccion)
    }

    @Transactional
    fun generateTransaction(usuario: Usuario,publicacion: Publicacion): Transaccion {
        lateinit var  direccionEnvio:String
        lateinit var accion :Accion
        if (usuario.id == publicacion.usuario!!.id!!) {
            throw PublicacionException ("Error: No puede generar la transaccion con su propia intención")
        }

            val cantidadOperaciones = publicacion.usuario!!.cantidadOperaciones
            //val reputacion = incrementarReputacionSegunTiempo(publicacion.diahora!!)

            //publicacion.usuario!!.incrementarReputacion(reputacion)
            //usuario.incrementarReputacion(reputacion)

        when (publicacion.operacion) {
            "compra" -> {
                direccionEnvio = publicacion.usuario!!.walletAddress!!
                accion = Accion.REALIZAR_TRANSFERENCIA

            }
            "venta" -> {
                direccionEnvio = publicacion.usuario!!.cvu!!
                accion = Accion.CONFIRMAR_RECEPCION

            }
        }

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
                1.0,
                direccionEnvio,
                accion,
                usuario   )

            return  transactionerRepository.save(transaccion)

    }


    @Transactional
     fun procesar(/*usuario: Usuario,*/ transaccion: Transaccion): Transaccion {
        lateinit var  direccionEnvio:String
        lateinit var accion :Accion

           transaccion.usuario!!.icrementarOperqaciones()
           val reputacion = incrementarReputacionSegunTiempo(transaccion.diahora!!)

          transaccion.usuario!!.incrementarReputacion(reputacion)
          //usuario.incrementarReputacion(reputacion)
          transaccion.usuarioselector!!.incrementarReputacion(reputacion)
        // transaccion.state = EstadoInicial()
             state = EstadoInicialService()
             return  transactionerRepository.save(transaccion)

           }

   @Transactional
    fun cancelar(/*usuario: Usuario,*/transaccion: Transaccion): Transaccion {
           // transaccion.state = Cancelado()
            state = EstadoCanceladoService()
           // usuario.descontarReputacion(20.0)
            transaccion.usuarioselector!!.descontarReputacion(20.0)
          //  userRepository.save(usuario)
           return transactionerRepository.save(transaccion)
          // return transaccion
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


   fun isCanceled(transaccion:Transaccion,cotizacionActual: Double): Boolean {
        return  when    (transaccion.operacion) {
            "compra" -> {
               // cotizacionActual(publicacion.criptoactivo!!) > publicacion.cotizacion
                cotizacionActual > transaccion.cotizacion
            }
            else -> {
               // cotizacionActual (publicacion.criptoactivo!!)  < publicacion.cotizacion
                cotizacionActual  < transaccion.cotizacion
            }
        }
    }


/*
    fun getComprador (transaccion:Transaccion): Usuario {
        return  when    (transaccion.operacion) {
            "compra" -> {
                 transaccion.usuario!!
            }
            else -> {
                 transaccion.usuarioselector!!
            }
        }
    }


    fun getVendedor(transaccion:Transaccion): Usuario {
        return  when    (transaccion.operacion) {
            "venta" -> {

                transaccion.usuario!!
            }
            else -> {
                transaccion.usuarioselector!!
            }
        }
    }

*/
    private fun  cotizacionActual(symbol:String): Double{
        val  criptoActivo = consumer.consumeBySymbol(symbol)
        return  criptoActivo.cotizacion!!.toDouble()
    }

    @Transactional
       fun confirmarRecepcion(transaccion:Transaccion):Boolean {
       // state = EstadoPagoConfirmadoService()
        //return state.confirmarRecepcion(transaccion)


        val  cuenta  = getCuenta(transaccion.direccionEnvio!!)
        val montoDepositado =  mercadoPagoService.consultarMonto(cuenta , transaccion.comprador()  /*transaccion.usuarioSelector!!*/)
        return  ( montoDepositado >= transaccion.monto )

       }



    @Transactional
     fun realizarTransferencia(direccionEnvio: String, monto:Double,comprador:Usuario) : Deposito{
       // setActualState (EstadoPagoTransferidoService())
        //return getActualState().realizaTransferencia(direccionEnvio, monto,comprador)

        val  cuenta  = getCuenta(direccionEnvio)
        val deposito =   mercadoPagoService.depositar(cuenta, monto, comprador )
        return deposito

           }


    @Transactional
    fun notificarPago (transaccion:Transaccion,deposito:Deposito){
       // state.notificarPago(transaccion,deposito)
         transaccion.vendedor().notificar(deposito)  // getVendedor(transaccion).notificar(deposito)

   }

       @Transactional
       fun finalizarTransaccion(transaccion:Transaccion){
      // state.finalizaTransaccion(transaccion)
       deleteById(transaccion.id!!)
        }

    @Transactional
    fun enviarCriptoActivo ( transaccion:Transaccion){
     //state.enviarCriptoActivo(transaccion)
        val walletAddress =  transaccion.comprador().walletAddress!!// getComprador(transaccion).walletAddress!!  //transaccion.usuarioSelector!!.walletAddress!!
        val wallet = getVirtualWallet(walletAddress)
        guardarCriptoActivo(transaccion,wallet)

    }


    fun guardarCriptoActivo(transaccion:Transaccion,wallet:VirtualWallet) {
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
        val criptoActivos = mutableListOf<CriptoActivoWalletMapper>()
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
      val usuario =  transaccion.comprador() // getComprador(transaccion)//transaccion.usuarioSelector!!
      val criptoactivos = criptoActivosDeLaVirtualWalletDeUsuario(usuario)
      return  criptoactivos.find { it.criptoActivo == transaccion.criptoactivo } ?:throw ItemNotFoundException("Not found criptoActivo")
    }

    fun depositar(cuenta:CuentaCVU,monto:Double, usuario:Usuario): Deposito{
        return mercadoPagoService.depositar(cuenta,monto,usuario)
    }


    fun wallets(): MutableList<VirtualWallet> {
        return  wallets
    }

    fun cuentas(): MutableList<CuentaCVU> {
        return  mercadoPagoService.cuentas()
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
          val transaccionesParaUsuario=   transacciones.filter{ it.usuario!!.id == usuarioId  }.toMutableList()
           if (transaccionesParaUsuario.isEmpty()){
               throw ItemNotFoundException ("No hay transacciones con el id de usuario $usuarioId ")
           }
        return transaccionesParaUsuario
    }

    fun transaccionesDeUnUsuarioEntreFechas(usuarioId: Long,fecha1:LocalDateTime, fecha2:LocalDateTime, transacciones: MutableList<Transaccion>):MutableList<Transaccion>{
       val transaccionesEntreFechas = transacciones.filter { entreFechas(it.diahora!!,fecha1,fecha2) }.toMutableList()
        if (transaccionesEntreFechas.isEmpty()){
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
          val sumaCotizaciones = cripto.criptoActivos.sumOf { it.cotizacion }
          val cantidadCotizacines  = cripto.criptoActivos.size
          val cotizacionPromedio = sumaCotizaciones/cantidadCotizacines
          val monto =  cantidad * cotizacionPromedio
          criptoActivo =  CriptoActivoWalletMapper(cripto.criptoActivo,cotizacionPromedio, cantidad,monto)
          criptoActivos.add(criptoActivo)
        }
          return  criptoActivos.sortedBy { it.criptoActivo }.toMutableList()
    }

    fun entreFechas(fecha:LocalDateTime, fecha1:LocalDateTime, fecha2:LocalDateTime): Boolean{
        return (   fecha.isBefore(fecha2) || fecha.isEqual(fecha2)  )
                &&  (fecha.isAfter(fecha1) || fecha.isEqual(fecha1))
    }

    fun getCuenta(cvu:String): CuentaCVU{
      return   mercadoPagoService.getCuenta(cvu)
    }

   fun  crearCuentaParaCliente (usuario:Usuario){
       mercadoPagoService.crearCuentaParaCliente(usuario)
   }
}