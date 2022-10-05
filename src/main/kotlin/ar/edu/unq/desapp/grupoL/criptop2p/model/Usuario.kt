package ar.edu.unq.desapp.grupoL.criptop2p.model

import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import kotlin.jvm.Transient
import javax.validation.constraints.*

@Entity
@Table(name = "users")
class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    var id: Long? = null


    @Column(nullable = false)
    @NotNull( message = "El nonbre es obligatorio")
    @Size(min = 3 , max = 30, message = "el campo name debe contener un minimo de 3 y un máximo de 30 caracteres")
    var name: String? = null

    @Column(nullable = false)
    @NotNull(message = "el apellido es obligatorio")
    @Size(min = 3 , max = 30, message = "el campo surname debe contener un minimo de 3 y un máximo de 30 caracteres")
    var surname: String? = null

    @Column(nullable = false, unique = true)
    @NotNull( message = "El mail es obligatorio")
    @Email
    @Pattern(regexp = "^[^@]+@[^@]+\\.[a-zA-Z] {2,}$", message = "El formato de mail no es válido")
    var email: String? = null

    @Column(nullable = false)
    @NotNull( message = "La direccièn es obligatoria")
    @Size(min = 10 , max = 30)
    var address: String? = null

    @Column
    @Size(min = 6)
     //al menos 1 minuscula, 1 mayuscula, 1 carac especial y min 6
    var password: String? = null

    @Column(nullable = false)
    @Size(min = 22 , max = 22)
    @Pattern (regexp= "^\\d{22}$", message="El CVU debe ser de 22 digitos")
    var cvu: String? = null

    @Column(nullable = false)
    @Pattern (regexp= "^\\d{8}$", message="El CVU debe ser de 8 digitos")
    @Size(min = 8 , max = 8)
    var walletAddress: String? = null

   @Transient
    var cantidadOperaciones: Int? = null

   @Transient
    var reputacion: Int? = null

    constructor() : super() {}
    constructor(id: Long?, name: String?, surname: String?, email:String?,address:String?,password:String?,cvu:String?,walletAddress:String?/*,cantidadOperaciones: Int?*/  ) : super() {
        this.id = id
        this.name = name
        this.surname = surname
        this.email = email
        this.address = address
        this.password = password
        this.cvu= cvu
        this.walletAddress = walletAddress
       // this.cantidadOperaciones = cantidadOperaciones
    }

    fun getOperaciones(): Int {
        return cantidadOperaciones!!
    }

    fun icrementarOperqaciones(): Int {
       return   cantidadOperaciones!! + 1
    }

}