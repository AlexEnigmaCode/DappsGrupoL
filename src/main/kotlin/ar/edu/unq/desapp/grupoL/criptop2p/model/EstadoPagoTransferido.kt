package ar.edu.unq.desapp.grupoL.criptop2p.model

import ar.edu.unq.desapp.grupoL.criptop2p.Deposito

class EstadoPagoTransferido: EstadoTransaccion() {

    override fun  realizaTransferencia(direccionEmvio: String, monto:Double, comprador:Usuario) : Deposito {
        return  transactionerService.realizarTransferencia(direccionEmvio,monto, comprador)
    }
    override fun  notificarPago(transaccion:Transaccion, deposito: Deposito) {
       transactionerService.notificarPago (transaccion, deposito)
    }

    override fun  confirmarRecepcion(transaccion:Transaccion) {
        transactionerService.confirmarRecepcion(transaccion)
    }

    override fun  enviarCriptoActivo(transaccion:Transaccion) {
        throw Exception ("El Pago aón no ha sido confirmado")
    }

    override fun finalizaTransaccion(transaccion:Transaccion) {
        throw Exception ("El Pago aón no ha sido confirmado")
    }
}