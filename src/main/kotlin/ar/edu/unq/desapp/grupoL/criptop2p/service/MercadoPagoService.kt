package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.CuentaCVU
import ar.edu.unq.desapp.grupoL.criptop2p.Deposito
import ar.edu.unq.desapp.grupoL.criptop2p.ItemNotFoundException
import ar.edu.unq.desapp.grupoL.criptop2p.model.Usuario
import org.springframework.stereotype.Service

@Service

class MercadoPagoService {

    var cuentas = mutableListOf<CuentaCVU>()
    var depositos = mutableListOf<Deposito>()

    fun setCuentasClientes(cuentas: MutableList<CuentaCVU>){
     this.cuentas = cuentas
    }

    fun setDepositoCuentas(depositos : MutableList<Deposito>){
        this.depositos = depositos
    }

    fun cleanAll(){
        cuentas.removeAll(cuentas)
        depositos.removeAll(depositos)
    }

    fun depositos():MutableList<Deposito>{
       return depositos
    }

    fun crearCuentaParaCliente (usuario:Usuario){
        val depositos = mutableListOf<Deposito>()
        val cuenta =  CuentaCVU (usuario, depositos )
        cuentas.add(cuenta)
    }

     fun getCuenta(cvu:String): CuentaCVU{
       return  cuentas.find { it.usuario.cvu == cvu} ?:  throw ItemNotFoundException("Client with CVUu: ${cvu} not found")
    }

     fun cuentas(): MutableList<CuentaCVU> {
       return  cuentas
    }



    fun depositosDeLaCuentaDeUsuario(usuario:Usuario): MutableList <Deposito>{
       val cuenta = getCuenta( usuario.cvu!!)
       val depositos = cuenta.depositos
       return depositos
    }



    fun depositar(cuenta:CuentaCVU,monto:Double, usuario:Usuario): Deposito{
       val  deposito = Deposito(usuario, monto)
      return  actualizarDeposito(cuenta,deposito)

    }


    fun actualizarDeposito(cuenta:CuentaCVU,deposito:Deposito): Deposito{
        lateinit var depositoActual :Deposito
        if ( cuenta.depositos.any{ it.usuario.cvu == deposito.usuario.cvu } ) {
           val  nuevodeposito = cuenta.depositos.find{ it.usuario.cvu == deposito.usuario.cvu  }
            nuevodeposito!!.monto  +=   deposito.monto
            depositoActual = nuevodeposito!!
        }
        else {
            cuenta.depositos.add(deposito)
            depositoActual = deposito
        }
        return depositoActual
    }



    fun consultarMonto(cuenta:CuentaCVU ,usuario:Usuario): Double {
        val deposito:Deposito =  cuenta.depositos.find { it.usuario.cvu == usuario.cvu }  ?:  throw ItemNotFoundException("Client with CVU: ${usuario.cvu} not found")
        return deposito.monto
    }

}