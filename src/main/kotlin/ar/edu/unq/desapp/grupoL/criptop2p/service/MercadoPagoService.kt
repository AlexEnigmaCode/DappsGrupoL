package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.ItemNotFoundException
import ar.edu.unq.desapp.grupoL.criptop2p.model.CuentaCVU
import ar.edu.unq.desapp.grupoL.criptop2p.model.Deposito
import ar.edu.unq.desapp.grupoL.criptop2p.model.Usuario
import org.springframework.stereotype.Service

@Service
class MercadoPagoService {

    // val usuarios = mutableListOf<Usuario>()
    val cuentas = mutableListOf<CuentaCVU>()
     val depositos = mutableListOf<Deposito>()



    fun crearCuentaParaCliente (usuario:Usuario){
        //usuarios.add(usuario)
       val cuenta =  CuentaCVU (usuario, depositos )
        cuentas.add(cuenta)
    }

     fun getCuenta(usuario:Usuario): CuentaCVU{
       return  cuentas.find { it.usuario.cvu == usuario.cvu} ?:  throw ItemNotFoundException("Client with CVUu: ${usuario.cvu} not found")
    }

     fun cuentas(): MutableList<CuentaCVU> {
       return  cuentas
    }

    fun depositar(cuenta:CuentaCVU,monto:Double, usuario:Usuario){
       val  deposito = Deposito(usuario, monto)
       actualizarDeposito(cuenta,deposito, usuario)

    }


    fun actualizarDeposito(cuenta:CuentaCVU,deposito:Deposito, usuario:Usuario){
        if ( cuenta.depositos.any{ it.usuario.cvu == usuario.cvu } ) {
           val  nuevodeposito = cuenta.depositos.find{ it.usuario.cvu == usuario.cvu  }
            nuevodeposito!!.monto  +=   deposito.monto
        }
        else {
            cuenta.depositos.add(deposito)
        }
    }



    fun consultarMonto(cuenta:CuentaCVU ,usuario:Usuario): Double {
        val deposito:Deposito =  cuenta.depositos.find { it.usuario.cvu == usuario.cvu }  ?:  throw ItemNotFoundException("Client with CVUu: ${usuario.cvu} not found")
        return deposito.monto
    }

}