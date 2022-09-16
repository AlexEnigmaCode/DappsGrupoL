package ar.edu.unq.desapp.grupoL.criptop2p.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "users")
class User {
    @Id
    var id: Int? = null

    @Column
    var name: String? = null

    @Column
    var surname: String? = null

    @Column
    var email: String? = null

    @Column
    var address: String? = null

    @Column
    var password: String? = null

    @Column
    var cvu: Int? = null

    @Column
    var walletAddress: Int? = null

    constructor() : super() {}
    constructor(id: Int?, name: String?, surname: String?, email:String?,address:String?,password:String?,cvu:Int?,walletAddress:Int?) : super() {
        this.id = id
        this.name = name
        this.surname = surname
        this.email = email
        this.address = address
        this.password = password
        this.cvu= cvu
        this.walletAddress = walletAddress
    }
}