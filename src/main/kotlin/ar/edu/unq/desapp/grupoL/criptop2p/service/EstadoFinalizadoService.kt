package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.Deposito
import ar.edu.unq.desapp.grupoL.criptop2p.model.Transaccion
import ar.edu.unq.desapp.grupoL.criptop2p.model.Usuario
import org.springframework.stereotype.Service

@Service
class EstadoFinalizadoService: EstadoTransaccionService() {

    override fun  realizaTransferencia(direccionEmvio: String, monto:Double, comprador: Usuario): Deposito {
        throw Exception ("Error: Lartransacción ha finalizador")
    }
    override fun  notificarPago(transaccion: Transaccion, deposito: Deposito) {}
    override fun  confirmarRecepcion(transaccion: Transaccion):Boolean{
                   throw Exception ("Error: Lartransacción ha finalizador") }
    override fun  enviarCriptoActivo(transaccion: Transaccion) {}
    override fun finalizaTransaccion(transaccion: Transaccion) {}
}