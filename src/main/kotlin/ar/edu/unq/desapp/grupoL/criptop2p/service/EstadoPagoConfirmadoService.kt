package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.Deposito
import ar.edu.unq.desapp.grupoL.criptop2p.model.Transaccion
import ar.edu.unq.desapp.grupoL.criptop2p.model.Usuario
import org.springframework.stereotype.Service

@Service
class EstadoPagoConfirmadoService: EstadoTransaccionService() {

    override fun  realizaTransferencia(direccionEmvio: String,monto:Double,comprador: Usuario) : Deposito {
        throw  Exception ("No se puede realizar  transferencia a $direccionEmvio si el pago ya está confirmado")
    }
    override fun  notificarPago(transaccion: Transaccion, deposito: Deposito) {}
    override fun  confirmarRecepcion(transaccion: Transaccion):Boolean {
       return  brokerService.confirmarRecepcion(transaccion)
    }

    override fun  enviarCriptoActivo(transaccion: Transaccion) {
        brokerService.enviarCriptoActivo (transaccion)
    }


    override fun finalizaTransaccion(transaccion: Transaccion) {
        throw Exception ( "Error: Aún no se ha enviado el criptoactivo" )}
}