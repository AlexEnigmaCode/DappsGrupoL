package ar.edu.unq.desapp.grupoL.criptop2p.model


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

     @Transient
    var cantidad: Long? = null

    @Transient
    var monto: Double = 0.0


    constructor() : super() {}
    constructor(id: Long?,criptoactivo: String?, cotizacion: String?, fecha: String?) : super() {
        this.id = id
        this.criptoactivo = criptoactivo
        this.cotizacion = cotizacion
        this.fecha = fecha
    }

}


