package ar.edu.unq.desapp.grupoL.criptop2p.model


import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "criptoactivos")
class CriptoActivo : Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_criptoactivo")
    var id: Long? = null

    @Column
    var criptoactivo: String? = null

    @Column
    var cotizacion: String? = null

    @Column
    var diahora: LocalDateTime? = null

     @Transient
    var cantidad: Long? = null

    @Transient
    var monto: Double = 0.0


    constructor() : super() {}
    constructor(id: Long?,criptoactivo: String?, cotizacion: String?, diahora: LocalDateTime?) : super() {
        this.id = id
        this.criptoactivo = criptoactivo
        this.cotizacion = cotizacion
        this.diahora = diahora
    }

}


