package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.*
import ar.edu.unq.desapp.grupoL.criptop2p.model.*
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.PublicacionRepository
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.TransaccionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.LocalTime


@Service
class PublisherService {
    val publicaciones = listOf<Publicacion>()

    val wallets = mutableListOf<VirtualWallet>()
    val criptoActivos = mutableListOf<CriptoActivoWalletMapper>()

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var mercadoPagoService: MercadoPagoService

    @Autowired
    private lateinit var publisherRepository: PublicacionRepository

    @Autowired
    private lateinit var transactionerRepository: TransaccionRepository



    @Autowired
    private  lateinit var consumer : ConsumerCriptoActivoMicroService


    @Transactional
    fun publicar(id: Long, intencion: IntencionRegisterMapper): Publicacion {
       /* if ( ! puedePublicarSegunCotizacionActual(intencion)) {
            throw Exception("Error : No puede publicar, el precio de la publicación está por fuera del precio de referencia")
        }
*/
        try {
            val usuario = userService.findByID(id)

            val diahora = LocalDateTime.now()
            //val cantidadoperaciones = usuario.cantidadOperaciones
            val monto = intencion.cantidad!! * intencion.cotizacion!!
            //val reputacion = usuario.reputacion.toString()

            val publicacion = Publicacion(
                0,
                diahora,
                intencion.criptoactivo,
                intencion.cantidad,
                intencion.cotizacion,
                monto,
                usuario,
                intencion.operacion)

            return publisherRepository.save(publicacion)
        } catch (e: Exception) {
            throw ItemNotFoundException("User with Id:  $id not found")
        }
    }


  private fun puedePublicarSegunCotizacionActual(intencion: IntencionRegisterMapper): Boolean {
      val cotizacionActual = cotizacionActual(intencion.criptoactivo!!).toDouble()
      val minRef =  cotizacionActual * 0.95
      val maxRef =  cotizacionActual * 1.05
      return  (intencion.cotizacion  >= minRef) && (intencion.cotizacion <= maxRef)
  }


    @Transactional
    fun selectByID(id: Long, usuario:Usuario): Publicacion {
        val publicacion = publisherRepository.findById(id.toInt())
        if ( ! (publicacion.isPresent ))
        {throw ItemNotFoundException("Publicacion with Id:  $id not found") }
        val newPublicacion=  publicacion.get()

        if (usuario.id!!.toInt() == newPublicacion.usuario!!.id!!.toInt()) {
            throw IntentionException ("Error: No puede selecconar su propia intención")
        }
         return newPublicacion
    }

    @Transactional
    fun cancelar (publicacion:Publicacion,usuario:Usuario){
       usuario.descontarReputacion(20.0)
    }




    @Transactional
    fun findAll(): List<Publicacion> {
        val publicaciones = publisherRepository.findAll()
        return publicaciones
    }


    @Transactional
    fun procesarTransaccion(publicacion: Publicacion,usuario: Usuario): Transaccion {

                if (!isCanceled(publicacion)) {
                    throw Exception ("transaccion ${publicacion.id}  cancelada, no cumple los requerimientos según la cotizacion actual ")
                }

           return procesar(publicacion,usuario)
    }

   private  fun procesar(publicacion: Publicacion,usuario:Usuario): Transaccion {
    lateinit var  direccionEnvio:String
    lateinit var accion :Accion
      val cantidadOperaciones = publicacion.usuario!!.icrementarOperqaciones()
       val reputacion =  incrementarReputacionSegunTiempo(publicacion.diahora!!)

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
               usuario)


       when (publicacion.operacion) {

           "compra" -> {
                transaccion.direccionEnvio =  publicacion.usuario!!.walletAddress!!
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


    private fun isCanceled(publicacion:Publicacion): Boolean {

         return  when    (publicacion.operacion) {
            "compra" -> {
               cotizacionActual(publicacion.criptoactivo!!) > publicacion.cotizacion
               }
            else -> {
               cotizacionActual (publicacion.criptoactivo!!)  < publicacion.cotizacion
            }

         }

    }

      private fun  cotizacionActual(symbol:String): Int{
      val  criptoActivo = consumer.consumeBySymbol(symbol)
        return  criptoActivo.cotizacion!!.toInt()
    }

}