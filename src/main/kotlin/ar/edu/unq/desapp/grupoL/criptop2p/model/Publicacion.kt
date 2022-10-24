package ar.edu.unq.desapp.grupoL.criptop2p.model

import java.time.LocalDateTime
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


    ) : super() {
        this.id = id
        this.diahora = diahora
        this.criptoactivo = criptoactivo
        this.cantidad = cantidad
        this.cotizacion = cotizacion
        this.monto = monto
        this.usuario = usuario
        this.operacion = operacion

    }


}


