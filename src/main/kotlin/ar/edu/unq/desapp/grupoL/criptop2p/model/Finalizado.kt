package ar.edu.unq.desapp.grupoL.criptop2p.model

import ar.edu.unq.desapp.grupoL.criptop2p.Deposito

class Finalizado: EstadoTransaccion() {

    override fun  realizaTransferencia(direccionEmvio: String, monto:Double, comprador:Usuario): Deposito {
        throw Exception ("Error: Lartransacción ha finalizador")
    }
    override fun  notificarPago(transaccion:Transaccion,deposito: Deposito) {}
    override fun  confirmarRecepcion(transaccion:Transaccion) {}
    override fun  enviarCriptoActivo(transaccion:Transaccion) {}
    override fun finalizaTransaccion(transaccion:Transaccion) {}

}