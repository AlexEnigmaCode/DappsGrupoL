package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.*
import ar.edu.unq.desapp.grupoL.criptop2p.model.Publicacion
import ar.edu.unq.desapp.grupoL.criptop2p.model.Transaccion
import ar.edu.unq.desapp.grupoL.criptop2p.model.Usuario
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.io.Serializable
import java.util.HashMap

@Service
class DTOService {

    fun usuarioToUserViewMapper(usuario: Usuario): UserViewMapper {
        val userViewMapper = UserViewMapper(
            usuario.id,
            usuario.name,
            usuario.surname,
            usuario.email,
            usuario.address,
            usuario.cvu,
            usuario.walletAddress,
            usuario.cantidadOperaciones,
            usuario.reputacion
        )
        return userViewMapper
    }

    fun publicacionToPublicacionViewMapper(publicacion: Publicacion): PublicacionViewMapper {
        val usuarioViewMapper = usuarioToUserViewMapper(publicacion.usuario!!)
        val publicacionDTO = PublicacionViewMapper(
            publicacion.id!!,
            publicacion.diahora!!,
            publicacion.criptoactivo!!,
            publicacion.cantidad!!,
            publicacion.cotizacion,
            publicacion.monto,
            usuarioViewMapper,
            publicacion.operacion!!
        )
        return publicacionDTO
    }

    fun transaccionToTransaccionViewMapper(transaccion: Transaccion): TransaccionViewMapper {
        val usuarioViewMapper1 = usuarioToUserViewMapper(transaccion.usuario!!)
        val usuarioViewMapper2 = usuarioToUserViewMapper(transaccion.usuarioselector!!)

        val transaccion = TransaccionViewMapper(
            transaccion.id!!,
            transaccion.diahora!!,
            transaccion.criptoactivo,
            transaccion.cantidad!!,
            transaccion.cotizacion,
            transaccion.monto,
            usuarioViewMapper1,
            transaccion.operacion!!,
            transaccion.cantidadoperaciones!!,
            transaccion.reputacion,
            transaccion.direccionEnvio!!,
            transaccion.accion!!,
            usuarioViewMapper2
        )
        return transaccion
    }


    fun walletToWalletMapper(wallet: VirtualWallet): MutableMap<String, MutableList<CriptoActivoWalletMapper>> {
        val usuario = wallet.usuario
        val criptos = wallet.criptoactivos
        if (wallet.criptoactivos.isNotEmpty()) {
            criptos.map { it }
        }
        val resultado: MutableMap<String, MutableList<CriptoActivoWalletMapper>> = HashMap()
        resultado["${usuario.name}"] = criptos
        return resultado
    }

    fun depositoToDepositoView(deposito:Deposito): MutableMap<String, Double> {
        val monto = deposito.monto
        val usuario = deposito.usuario
        val resultado: MutableMap<String, Double> = HashMap()
        resultado["Deposito hecho por : ${usuario.name}"] = monto
        return resultado
    }

    fun  confirmadoToConfirmadoView(confirmado:Boolean): MutableMap<String, String> {
        val resultado: MutableMap<String, String> = HashMap()
        var respuesta  =  "El monto necesario no ha sido aún depositado"
        if (confirmado){
             respuesta = "La confirmación ha sido exitosa, ya se realizó el deposito en su cuenta"
        }
        resultado["Confirmacion del Pago:"] = respuesta
        return resultado
    }


}