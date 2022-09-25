package ar.edu.unq.desapp.grupoL.criptop2p.model


import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "criptoactivos")
class CriptoActivo {

    @Id
    var symbol: String? = null

    @Column
    var price: String? = null

    constructor() : super() {}
    constructor(symbol: String?, price: String?  ) : super() {
        this.symbol = symbol
        this.price = price
    }

}
//class CriptoActivo (val criptoactivo: String?, val cotizacion: String?, val fecha: LocalDate)

