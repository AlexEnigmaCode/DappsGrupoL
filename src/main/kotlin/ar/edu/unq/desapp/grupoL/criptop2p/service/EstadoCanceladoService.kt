package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.Deposito
import ar.edu.unq.desapp.grupoL.criptop2p.model.Transaccion
import ar.edu.unq.desapp.grupoL.criptop2p.model.Usuario
import org.springframework.stereotype.Service

@Service
class EstadoCanceladoService: EstadoTransaccionService() {

    override fun  realizaTransferencia(direccionEmvio: String, monto:Double,comprador: Usuario): Deposito {
        throw Exception ("Error: No se puede realizar la acción, La transacción fue cancelada")
    }

    override fun  notificarPago(transaccion: Transaccion, deposito: Deposito) {
        throw Exception ("Error: No se puede realizar la acción, La transacción fue cancelada")
    }

    override fun  confirmarRecepcion(transaccion: Transaccion) :Boolean {
        throw Exception ("Error: No se puede realizar la acción, La transacción fue cancelada")
    }

    override fun  enviarCriptoActivo(transaccion: Transaccion) {
        throw Exception ("Error: No se puede realizar la acción, La transacción fue cancelada")
    }

    override fun finalizaTransaccion(transaccion: Transaccion) {
        throw Exception ("Error: No se puede realizar la acción, La transacción fue cancelada")
    }

}