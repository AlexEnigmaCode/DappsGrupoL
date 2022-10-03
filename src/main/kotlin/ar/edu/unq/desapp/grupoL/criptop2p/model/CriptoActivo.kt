package ar.edu.unq.desapp.grupoL.criptop2p.model


import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "criptoactivos")
class CriptoActivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_criptoactivo")
    var id: Long? = null

    @Column
    var criptoactivo: String? = null

    @Column
    var cotizacion: String? = null

    @Column
    var fecha: String? = null


    constructor() : super() {}
    constructor(id: Long?,criptoactivo: String?, cotizacion: String?, fecha: String?) : super() {
        this.id = id
        this.criptoactivo = criptoactivo
        this.cotizacion = cotizacion
        this.fecha = fecha
    }

}
data class CriptoActivoRegisterMapper (val criptoactivo: String?, val cotizacion: String?, val fecha: String)

