package ar.edu.unq.desapp.grupoL.criptop2p.model


import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "criptoactivos")
class CriptoActivo {

    @Id
    var criptoactivo: String? = null

    @Column
    var cotizacion: String? = null

    @Column
    var fecha: String? = null


    constructor() : super() {}
    constructor(criptoactivo: String?, cotizacion: String?, fecha: String?) : super() {
        this.criptoactivo = criptoactivo
        this.cotizacion = cotizacion
        this.fecha = fecha
    }

}
//class CriptoActivo (val criptoactivo: String?, val cotizacion: String?, val fecha: LocalDate)

