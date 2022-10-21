package ar.edu.unq.desapp.grupoL.criptop2p.model

import java.time.LocalTime
import javax.persistence.*

@Entity
@Table(name = "publicaciones")
class Publicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_publicacion")
    var id: Long? = null


    @Column
    var diahora: LocalTime? = null

    @Column
    var criptoactivo: String? = null

    @Column
    var cantidad: Long? = null


    @Column(nullable = false)
    var cotizacion: Double = 0.0

    @Column(nullable = false)
    var monto: Double = 0.0


    // @Column
    //var usuario: String? = null

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", nullable = true)
    var usuario: Usuario? = null

    @Column
    var operacion: String? = null

    @Column
    var cantidadoperaciones: Int? = null

    @Column
    var reputacion: String? = null

    @Column
    var cancelada: Boolean? = false

    @Transient
    var direccionEnvio: String? = null

    @Transient
    var accion: Accion? = null

    @Transient
    var usuarioSelector: Usuario? = null

    constructor() : super() {}
    constructor(
        id: Long?,
        diahora: LocalTime?,
        criptoactivo: String?,
        cantidad: Long?,
        cotizacion: Double,
        monto: Double,
        usuario: Usuario?,
        operacion: String?,
        cantidadoperaciones: Int?,
        reputacion: String?,
        cancelada: Boolean?,
        direccionEnvio: String?,
        accion: Accion?,
        usuarioSelector:Usuario?

    ) : super() {
        this.id = id
        this.diahora = diahora
        this.criptoactivo = criptoactivo
        this.cantidad = cantidad
        this.cotizacion = cotizacion
        this.monto = monto
        this.usuario = usuario
        this.operacion = operacion
        this.cantidadoperaciones = cantidadoperaciones
        this.reputacion = reputacion
        this.cancelada = cancelada
        this.direccionEnvio = direccionEnvio
        this.accion = accion
        this.usuarioSelector= usuarioSelector
    }

    fun isCanceled():Boolean{
        return cancelada!!
    }

}


