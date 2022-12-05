package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.Deposito
import ar.edu.unq.desapp.grupoL.criptop2p.model.Transaccion
import ar.edu.unq.desapp.grupoL.criptop2p.model.Usuario
import org.springframework.stereotype.Service

@Service
class EstadoInicialService:EstadoTransaccionService() {


    override fun  realizaTransferencia(direccionEmvio: String, monto:Double,comprador: Usuario) : Deposito {
        return  brokerService.realizarTransferencia(direccionEmvio, monto,comprador)

    }

    override fun  notificarPago(transaccion: Transaccion, deposito: Deposito) {
        throw Exception ("Aún no se ha realizado la transferencia")
    }

    override fun  confirmarRecepcion(transaccion: Transaccion):Boolean {
      return   brokerService.confirmarRecepcion( transaccion)
    }

    override fun  enviarCriptoActivo(transaccion: Transaccion) {
        throw Exception ("Aún no se ha realizado la transferencia")
    }

    override fun finalizaTransaccion(transaccion: Transaccion) {
        throw Exception ( "Error: Aún no se ha realizado la transferencia" )
    }


}