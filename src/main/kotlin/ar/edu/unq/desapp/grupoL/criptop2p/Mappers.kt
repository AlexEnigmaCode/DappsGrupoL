package ar.edu.unq.desapp.grupoL.criptop2p

import ar.edu.unq.desapp.grupoL.criptop2p.model.Usuario
import java.io.Serializable
import java.time.LocalDateTime
import java.time.LocalTime

data class UserRegisterMapper(
    val name: String? ,
    val surname: String?,
    val email:String?,
    val address:String?,
    val password: String?,
    val cvu:String?,
    val walletAddress:String?
):Serializable

data class UserLoginMapper(val email: String, val password: String):Serializable

data class UserUpdateMapper(val email:String,
                            val address:String,
                            val password: String,
                            val cvu:String,
                            val walletAddress:String):Serializable

data class UserViewMapper(val id: Long?,
                          val name: String?,
                          val surname: String?,
                          val email:String?,
                          val address:String?,
                          val cvu:String?,
                          val walletAddress:String?,
                          val cantidadOperaciones: Long,
                          var reputacion: Double):Serializable



data class PublicacionViewMapper (val id: Long,
                                  val diahora: LocalDateTime,
                                  val criptoactivo: String,
                                  val cantidad: Long,
                                  val cotizacion: Double,
                                  val monto: Double,
                                  val usuario: UserViewMapper,
                                  val operacion: String):Serializable





data class TransaccionViewMapper (val id: Long,
val diahora: LocalDateTime,
val criptoactivo: String?,
val cantidad: Long,
val cotizacion: Double,
val monto: Double,
val usuario: UserViewMapper,
val operacion: String,
val cantidadoperaciones: Long,
val reputacion: Double?,
val direccionEnvio: String,
val accion: Accion,
val usuarioSelector: UserViewMapper
):Serializable

data class Binance (val symbol:String?, val price: String?):Serializable


data class Binance24hs (val symbol:String,
val priceChange: String,
val priceChangePercent: String,
val weightedAvgPrice: String,
val prevClosePrice: String,
val lastPrice: String,
val lastQty: String,
val bidPrice: String,
val bidQty: String,
val askPrice: String,
val askQty: String,
val openPrice: String,
val highPrice: String,
val lowPrice: String,
val volume: String,
val quoteVolume: String,
val openTime: Long,
val closeTime: Long,
val firstId: Long,
val lastId: Long,
val count:Long ):Serializable

data class HistoricoCotizaciones24hs (val symbol:String, val  cotizaciones :List <Binance24hsMapper> ):Serializable

data class  Binance24hsMapper (val lastPrice: String, val hour: LocalDateTime):Serializable



data class IntencionRegisterMapper(
    val criptoactivo: String?, val cantidad:Long?,
    val cotizacion: Double, val monto: Double, val usuario: String?, val operacion: String?):Serializable


data class InformeUsuarioMapper(
    val name:String,
    val surname:String,
    val cantidadOperaciones: Long,
    val Rewputacion: Double
):Serializable

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
):Serializable


data class CriptoActivoWalletMapper (
    val criptoActivo:String,
    val cotizacion: Double,
    val cantidad: Long,
    var monto : Double): Serializable


data class CriptoActivoRegisterMapper(
    val criptoActivo: String?,
    val cotizacion: String?,
    val fecha: LocalDateTime)


data class  TransaccionCriptoActivoMapper(
    val criptoActivo :String,
    val criptoActivos: MutableList<CriptoActivoWalletMapper>
):Serializable

data class VolumenCriptoActivoOperadoMapper(
    val diahora: LocalDateTime,
    val  valorTotalOperado:Double,
    val criptoActivos: MutableList<CriptoActivoWalletMapper>

):Serializable


data class  VolumenPorOperacionesMapper(
    val diahora: LocalDateTime,
    val usuario :UserViewMapper,
    val  valorTotalOperados:Double,
    val compras: MutableList<CriptoActivoWalletMapper>,
    val ventas : MutableList<CriptoActivoWalletMapper>

):Serializable

data class  BetweenDates(
  val fecha1: LocalDateTime,
  val fecha2: LocalDateTime
):Serializable

data class CuentaCVU (val usuario:Usuario,val depositos: MutableList<Deposito>): Serializable

data class Deposito (val usuario:Usuario, var monto: Double ):Serializable

data  class VirtualWallet  (val usuario:Usuario, val criptoactivos: MutableList<CriptoActivoWalletMapper>): Serializable
data class Transferencia (val direccionEnvio:String, val monto:Double): Serializable