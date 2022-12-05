package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.*
import ar.edu.unq.desapp.grupoL.criptop2p.model.Transaccion
import ar.edu.unq.desapp.grupoL.criptop2p.model.Usuario
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.TransaccionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class BrokerService {
    val wallets = mutableListOf<VirtualWallet>()

    @Autowired
    private lateinit var mercadoPagoService: MercadoPagoService

    @Autowired
    private lateinit var transactionerRepository: TransaccionRepository




    @Transactional
    fun enviarCriptoActivo ( transaccion:Transaccion){
        val walletAddress =  transaccion.comprador().walletAddress!! //getComprador(transaccion).walletAddress!!  //transaccion.usuarioSelector!!.walletAddress!!
        val wallet = getVirtualWallet(walletAddress)
        guardarCriptoActivo(transaccion,wallet)
    }

    @Transactional
    fun confirmarRecepcion(transaccion:Transaccion):Boolean {
        val  cuenta  = getCuenta(transaccion.direccionEnvio!!)
        val montoDepositado =  mercadoPagoService.consultarMonto(cuenta , transaccion.comprador() )//getComprador(transaccion)  /*transaccion.usuarioSelector!!*/)
        return  ( montoDepositado >= transaccion.monto )
    }


    @Transactional
    fun realizarTransferencia(direccionEnvio: String, monto:Double,comprador:Usuario) : Deposito {
        val  cuenta  = getCuenta(direccionEnvio)
        val deposito =   mercadoPagoService.depositar(cuenta, monto, comprador )
        return deposito
    }


    @Transactional
    fun notificarPago (transaccion:Transaccion,deposito: Deposito){
        //transaccion.usuario!!.notificar(deposito)
        transaccion.vendedor().notificar(deposito)//getVendedor(transaccion).notificar(deposito)

    }


    @Transactional
    fun finalizarTransaccion(transaccion:Transaccion){
        deleteById(transaccion.id!!)
    }





    @Transactional
    fun deleteById(id: Long) {
        val transaccion =   transactionerRepository.findById(id)
        if ( ! (transaccion.isPresent ))
        {throw ItemNotFoundException("Transacción with Id:  $id not found") }
        transactionerRepository.deleteById(id)
    }


    fun isCanceled(transaccion:Transaccion,cotizacionActual: Double): Boolean {
        return  when    (transaccion.operacion) {
            "compra" -> {

                cotizacionActual > transaccion.cotizacion
            }
            else -> {

                cotizacionActual  < transaccion.cotizacion
            }
        }
    }

    fun getCuenta(cvu:String): CuentaCVU {
        return   mercadoPagoService.getCuenta(cvu)
    }
/*
    fun getComprador (transaccion:Transaccion): Usuario {
        return  when    (transaccion.operacion) {
            "compra" -> {
                transaccion.usuario!!
            }
            else -> {
                transaccion.usuarioSelector!!
            }
        }
    }


    fun getVendedor(transaccion:Transaccion): Usuario {
        return  when    (transaccion.operacion) {
            "venta" -> {

                transaccion.usuario!!
            }
            else -> {
                transaccion.usuarioSelector!!
            }
        }
    }
*/

    private fun  incrementarReputacionSegunTiempo(diahora: LocalDateTime):Double {

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

    fun guardarCriptoActivo(transaccion:Transaccion,wallet: VirtualWallet) {
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


    fun getVirtualWallet(walletAddress:String): VirtualWallet{
        return  wallets.find { it.usuario.walletAddress == walletAddress} ?:  throw ItemNotFoundException("User with Virtual Wallet: $walletAddress not found")
    }





}