package ar.edu.unq.desapp.grupoL.criptop2p.model

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "transacciones")
class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaccion")
    var id: Long? = null


    @Column
    var diahora: LocalDateTime? = null

    @Column
    var criptoactivo: String? = null

    @Column
    var cantidad: Long? = null


    @Column(nullable = false)
    var cotizacion: Double = 0.0

    @Column(nullable = false)
    var monto: Double = 0.0


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", nullable = true)
    var usuario: Usuario? = null

    @Column
    var operacion: String? = null

    @Column
    var cantidadoperaciones: Long? = null

    @Column
    var reputacion: Double? = null


    @Transient
    var direccionEnvio: String? = null

    @Transient
    var accion: Accion? = null

    @Transient
    var usuarioSelector: Usuario? = null

    constructor() : super() {}
    constructor(
        id: Long?,
        diahora: LocalDateTime?,
        criptoactivo: String?,
        cantidad: Long?,
        cotizacion: Double,
        monto: Double,
        usuario: Usuario?,
        operacion: String?,
        cantidadoperaciones: Long?,
        reputacion: Double?,
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
        this.direccionEnvio = direccionEnvio
        this.accion = accion
        this.usuarioSelector= usuarioSelector
    }



}



