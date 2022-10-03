package ar.edu.unq.desapp.grupoL.criptop2p

import ar.edu.unq.desapp.grupoL.criptop2p.model.Operacion

data class UserRegisterMapper(
    val name: String? = null,
    val surname: String? = null,
    val email:String?= null,
    val address:String?= null,
    val password: String? = null,
    val cvu:String?= null,
    val walletAddress:String? = null
)

data class UserLoginMapper(val email: String, val password: String)

data class UserUpdateMapper(val email:String,
                            val address:String,
                            val password: String,
                            val cvu:String,
                            val walletAddress:String)

data class UserViewMapper(val id: Int?,
                          val name: String?,
                          val surname: String?,
                          val email:String?,
                          val address:String?,
                          val cvu:String?,
                          val walletAddress:String?)

data class Binance (val symbol:String?, val price: String?)

data class IntencionRegisterMapper  (val criptoactivo: String?, val cantidad:Long?,val cotizacion: Long?, val monto: Long?, val usuario: String?, val operacion: String?)

data class PublicacionRegisterMapper(
   val diahora: String?,
   val criptoactivo: String?,
   val cantidad: Long?,
   val cotizacion: Long?,
   val monto: Long?,
   val usuario: String?,
   val cantidadoperaciones: Int?,
   val reputacion: String?
)