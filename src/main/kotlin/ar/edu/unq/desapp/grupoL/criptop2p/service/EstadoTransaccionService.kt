package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.Deposito
import ar.edu.unq.desapp.grupoL.criptop2p.model.Transaccion
import ar.edu.unq.desapp.grupoL.criptop2p.model.Usuario
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
abstract class EstadoTransaccionService {
    @Autowired
    protected lateinit var brokerService: BrokerService

    abstract  fun  realizaTransferencia(direccionEmvio: String,monto:Double, comprador: Usuario): Deposito
    abstract fun  notificarPago(transaccion: Transaccion, deposito: Deposito)
    abstract fun  confirmarRecepcion(transaccion: Transaccion):Boolean
    abstract fun  enviarCriptoActivo(transaccion: Transaccion)
    abstract fun finalizaTransaccion(transaccion: Transaccion)
}