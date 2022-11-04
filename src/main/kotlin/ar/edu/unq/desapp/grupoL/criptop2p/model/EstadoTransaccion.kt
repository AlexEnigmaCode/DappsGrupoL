package ar.edu.unq.desapp.grupoL.criptop2p.model

import ar.edu.unq.desapp.grupoL.criptop2p.Deposito
import ar.edu.unq.desapp.grupoL.criptop2p.service.TransactionerService
import org.springframework.beans.factory.annotation.Autowired

abstract class EstadoTransaccion {
    @Autowired
    protected lateinit var transactionerService: TransactionerService

   abstract  fun  realizaTransferencia(direccionEmvio: String,monto:Double, comprador:Usuario): Deposito
   abstract fun  notificarPago(transaccion:Transaccion,deposito: Deposito)
   abstract fun  confirmarRecepcion(transaccion:Transaccion)
   abstract fun  enviarCriptoActivo(transaccion:Transaccion)
   abstract fun finalizaTransaccion(transaccion:Transaccion)

}