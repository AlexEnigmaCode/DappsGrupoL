package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.*
import ar.edu.unq.desapp.grupoL.criptop2p.model.*
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.PublicacionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

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


    @Transactional
    fun publicar(id: Int, intencion: IntencionRegisterMapper): Publicacion {
        try {
            val usuario = userService.findByID(id)
            val diahora = LocalDate.now().toString()
            val cantidadoperaciones = usuario.cantidadOperaciones
            val monto = intencion.cantidad!! * intencion.cotizacion!!
            val reputacion = usuario.reputacion.toString()
            val publicacion = PublicacionRegisterMapper(
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
                null
            )
            return publisherRepository.save(publicacion)
        } catch (e: Exception) {
            throw ItemNotFoundException("User with Id:  $id not found")
        }
    }


    @Transactional
    fun findAll(): List<Publicacion> {
        val publicaciones = publisherRepository.findAll()
        return publicaciones
    }


    @Transactional
    fun procesarTransaccion(publicacion: Publicacion,usuario: Usuario): Publicacion {
       lateinit var  transaccion :Publicacion

        try {

            if (!publicacion.isCanceled()) {
                transaccion = procesar(publicacion,usuario)

            } else {
                 // descontarPuntos
            }


        } catch (e: Exception) {
            throw ItemNotFoundException("User with Id:  not found")
        }
           return transaccion
    }

   private  fun procesar(publicacion: Publicacion,usuario:Usuario): Publicacion{


       when (publicacion.operacion) {

          "compra" -> {

                publicacion.direccionEnvio = publicacion.usuario!!.walletAddress!!
               publicacion.accion =  Accion.REALIZAR_TRANSFERENCIA
               this.realizarTransferencia(publicacion,usuario) }
            else -> {
                publicacion.direccionEnvio  = publicacion.usuario!!.cvu!!
                publicacion.accion =  Accion.CONFIRMAR_RECEPCION
               this.confirmarRecepcion(publicacion,usuario)
                }

       }
                 return  generarTransaccion(publicacion, usuario)
        }


    private fun realizarTransferencia(publicacion:Publicacion, usuario:Usuario) {

              enviarDinero(usuario,publicacion)
              notificarPago (usuario)

    }

    private fun confirmarRecepcion(publicacion:Publicacion,usuario:Usuario) {
        val  cuenta  = mercadoPagoService.getCuenta(publicacion.usuario!!)
        val montoDepositado =  mercadoPagoService.consultarMonto(cuenta ,usuario)
        if  ( montoDepositado < publicacion.monto ) {
            throw Exception (" El monto depositado no es suficiente para realizar la transaccion" +
                    " o no se ha hecho el deposito en la cuenta  $cuenta")
        }
         enviarCriptoActivo(usuario,publicacion)
   }


    fun generarTransaccion(publicacion:Publicacion, usuario:Usuario): Publicacion {
           val newPublicacion =  PublicacionRegisterMapper(
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
              publicacion.accion
           )
            return  publisherRepository.save(newPublicacion)
    }

    private fun enviarDinero(usuario:Usuario, publicacion:Publicacion){
       val  cuenta  = mercadoPagoService.getCuenta(usuario)
         mercadoPagoService.depositar(cuenta, publicacion.monto, publicacion.usuario!! )

    }

    fun notificarPago (usuario:Usuario){

    }


    fun enviarCriptoActivo (usuario:Usuario,publicacion:Publicacion){
           val wallet = getVirtualWallet(usuario)
        guardarCriptoActivo(wallet,publicacion, usuario)
    }



    fun guardarCriptoActivo(wallet:VirtualWallet,publicacion:Publicacion, usuario:Usuario){
        val  criptoActivo = CriptoActivoWalletMapper(publicacion.criptoactivo!!, publicacion.cotizacion,publicacion.cantidad!!,publicacion.monto)
        wallet.criptoactivos.add(criptoActivo)

    }



    fun createVirtualWallet (usuario:Usuario){
        val wallet =  VirtualWallet (usuario, criptoActivos )
        wallets.add(wallet)
    }

    fun getVirtualWallet(usuario:Usuario): VirtualWallet{
        return  wallets.find { it.usuario.walletAddress == usuario.walletAddress} ?:  throw ItemNotFoundException("User with Virtual Wallet: ${usuario.walletAddress} not found")
    }

    fun wallets(): MutableList<VirtualWallet> {
        return  wallets
    }

}