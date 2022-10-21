package ar.edu.unq.desapp.grupoL.criptop2p

import ar.edu.unq.desapp.grupoL.criptop2p.model.Accion
import ar.edu.unq.desapp.grupoL.criptop2p.model.Usuario
import java.time.LocalTime

data class UserRegisterMapper(
    val name: String? ,
    val surname: String?,
    val email:String?,
    val address:String?,
    val password: String?,
    val cvu:String?,
    val walletAddress:String?
)

data class UserLoginMapper(val email: String, val password: String)

data class UserUpdateMapper(val email:String,
                            val address:String,
                            val password: String,
                            val cvu:String,
                            val walletAddress:String)

data class UserViewMapper(val id: Long?,
                          val name: String?,
                          val surname: String?,
                          val email:String?,
                          val address:String?,
                          val cvu:String?,
                          val walletAddress:String?)

data class Binance (val symbol:String?, val price: String?)

data class IntencionRegisterMapper(
    val criptoactivo: String?, val cantidad:Long?,
    val cotizacion: Double, val monto: Double, val usuario: String?, val operacion: String?)




data class PublicacionRegisterMapper(
    val diahora: LocalTime?,
    val criptoactivo: String?,
    val cantidad: Long?,
    val cotizacion: Double,
    val monto: Double,
    val usuario: Usuario?,
    val operacion: String ?,
    val cantidadoperaciones: Int?,
    val reputacion: String?,
    val cancelada : Boolean?,
    val direccionEnvio: String?,
    val accion: Accion?
)


data class CriptoActivoWalletMapper(
    val criptoactivo:String,
    val cotizacion: Double,
    val cantidad: Long,
    var monto : Double)


data class CriptoActivoRegisterMapper(
    val criptoactivo: String?,
    val cotizacion: String?,
    val fecha: String)