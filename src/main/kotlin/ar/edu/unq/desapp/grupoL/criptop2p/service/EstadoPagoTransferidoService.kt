package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.Deposito
import ar.edu.unq.desapp.grupoL.criptop2p.model.Transaccion
import ar.edu.unq.desapp.grupoL.criptop2p.model.Usuario
import org.springframework.stereotype.Service

@Service
class EstadoPagoTransferidoService:EstadoTransaccionService() {

    override fun  realizaTransferencia(direccionEmvio: String, monto:Double, comprador: Usuario) : Deposito {
        return  brokerService.realizarTransferencia(direccionEmvio,monto, comprador)
    }
    override fun  notificarPago(transaccion: Transaccion, deposito: Deposito) {
        brokerService.notificarPago (transaccion, deposito)
    }

    override fun  confirmarRecepcion(transaccion: Transaccion):Boolean {
      return   brokerService.confirmarRecepcion(transaccion)
    }

    override fun  enviarCriptoActivo(transaccion: Transaccion) {
        throw Exception ("El Pago aón no ha sido confirmado")
    }

    override fun finalizaTransaccion(transaccion: Transaccion) {
        throw Exception ("El Pago aón no ha sido confirmado")
    }
}