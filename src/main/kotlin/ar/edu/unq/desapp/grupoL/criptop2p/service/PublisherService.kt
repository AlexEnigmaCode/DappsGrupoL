package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.*
import ar.edu.unq.desapp.grupoL.criptop2p.model.*
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.PublicacionRepository
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
            val cantidadoperaciones = usuario.cantidadOperaciones
            val monto = intencion.cantidad!! * intencion.cotizacion!!
            val reputacion = usuario.reputacion.toString()

            val publicacion = Publicacion(
                0,
                diahora,
                intencion.criptoactivo,
                intencion.cantidad,
                intencion.cotizacion,
                monto,
                usuario,
                intencion.operacion,
                cantidadoperaciones,
                reputacion,
                false,
                null,
                null ,
             null)
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
       usuario.descontarReputacion(20)
    }




    @Transactional
    fun findAll(): List<Publicacion> {
        val publicaciones = publisherRepository.findAll()
        return publicaciones
    }


    @Transactional
    fun procesarTransaccion(publicacion: Publicacion,usuario: Usuario): Publicacion {
       var  transaccion :Publicacion = publicacion


                if (!isCanceled(publicacion)) {
                    transaccion = procesar(publicacion,usuario)

                }


           return transaccion
    }

   private  fun procesar(publicacion: Publicacion,usuario:Usuario): Publicacion {

       publicacion.usuario!!.icrementarOperqaciones()
       when (publicacion.operacion) {

           "compra" -> {
               publicacion.direccionEnvio = publicacion.usuario!!.walletAddress!!
               publicacion.accion = Accion.REALIZAR_TRANSFERENCIA
               publicacion.usuarioSelector = usuario
               this.realizarTransferencia(publicacion)
           }
           "venta" -> {
               publicacion.direccionEnvio = publicacion.usuario!!.cvu!!
               publicacion.accion = Accion.CONFIRMAR_RECEPCION
               publicacion.usuarioSelector = usuario
               this.confirmarRecepcion(publicacion)
           }


       }

       val newPublicacion =  incrementarReputacionSegunTiempo(publicacion)
       return  publisherRepository.save(newPublicacion)

   }




    private fun  incrementarReputacionSegunTiempo(publicacion:Publicacion):Publicacion {

       if (esFechaAnterior(LocalDateTime.now(), publicacion.diahora!!) ){

         publicacion.usuario!!.incrementarReputacion(10)
            publicacion.usuarioSelector!!.incrementarReputacion(10)
        }
        else {
            publicacion.usuario!!.incrementarReputacion(5)
            publicacion.usuarioSelector!!.incrementarReputacion(5)
        }

       return publicacion
    }



    private fun  esFechaAnterior(fechaActual:LocalDateTime, fechaPublicacion:LocalDateTime):Boolean{
       return  (fechaActual.isBefore(fechaPublicacion.plusMinutes(30)))

    }

    private fun realizarTransferencia(publicacion:Publicacion) {

             val deposito =  enviarDinero(publicacion)
              notificarPago (publicacion, deposito)

    }

    private fun confirmarRecepcion(publicacion:Publicacion) {
        val  cuenta  = mercadoPagoService.getCuenta(publicacion.direccionEnvio!!)
        val montoDepositado =  mercadoPagoService.consultarMonto(cuenta ,publicacion.usuarioSelector!!)
        if  ( montoDepositado < publicacion.monto ) {
            throw Exception (" El monto depositado no es suficiente para realizar la transaccion" +
                    " o no se ha hecho el deposito en la cuenta  $cuenta")
        }

        enviarCriptoActivo(publicacion)
   }




    fun generarTransaccion(publicacion:Publicacion, usuario:Usuario): Publicacion {
           val newPublicacion =  Publicacion(
               publicacion.id,
               publicacion.diahora,
               publicacion.criptoactivo,
               publicacion.cantidad,
               publicacion.cotizacion,
               publicacion.monto,
               publicacion.usuario,
               publicacion.operacion,
               publicacion.cantidadoperaciones,
               publicacion.reputacion,
               publicacion.cancelada,
               publicacion.direccionEnvio,
              publicacion.accion,
              publicacion.usuarioSelector
           )
            return  publisherRepository.save(newPublicacion)
    }

    private fun enviarDinero(publicacion:Publicacion) : Deposito{
       val  cuenta  = mercadoPagoService.getCuenta(publicacion.usuarioSelector!!.cvu!!)
       val deposito =   mercadoPagoService.depositar(cuenta, publicacion.monto, publicacion.usuario!! )
     return deposito
    }

    fun notificarPago (publicacion:Publicacion, deposito:Deposito){
        val notificacionesDeDeposito =  publicacion.usuarioSelector!!.notificacionesDeDeposito
        notificacionesDeDeposito.add(deposito)
    }


    fun enviarCriptoActivo (publicacion:Publicacion){
           val walletAddress = publicacion.usuarioSelector!!.walletAddress!!
           val wallet = getVirtualWallet(walletAddress)
        guardarCriptoActivo(wallet,publicacion)
    }



    fun guardarCriptoActivo(wallet:VirtualWallet,publicacion:Publicacion){
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