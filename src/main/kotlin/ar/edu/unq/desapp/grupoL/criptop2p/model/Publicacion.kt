package ar.edu.unq.desapp.grupoL.criptop2p.model

import javax.persistence.*

@Entity
@Table(name = "publicaciones")
class Publicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_publicacion")
    var id: Long? = null

    @Column
    var diahora: String? = null

    @Column
    var criptoactivo: String? = null

    @Column
    var cantidad: Long? = null

    @Column
    var cotizacion: Long? = null

    @Column
    var monto: Long? = null

    @Column
    var usuario: String? = null

    @Column
    var cantidadoperaciones: Int? = null

    @Column
    var reputacion: String? = null

    constructor() : super() {}
    constructor(
        id: Long?,
        diahora: String?,
        criptoactivo: String?,
        cantidad: Long?,
        cotizacion: Long?,
        monto: Long?,
        usuario: String?,
        cantidadoperaciones: Int?,
        reputacion: String?
    ) : super() {
        this.id = id
        this.diahora = diahora
        this.criptoactivo = criptoactivo
        this.cantidad = cantidad
        this.cotizacion = cotizacion
        this.monto = monto
        this.usuario = usuario
        this.cantidadoperaciones = cantidadoperaciones
        this.reputacion = reputacion
    }

}


