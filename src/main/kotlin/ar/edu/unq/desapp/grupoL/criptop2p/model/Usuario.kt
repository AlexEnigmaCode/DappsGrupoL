package ar.edu.unq.desapp.grupoL.criptop2p.model



import ar.edu.unq.desapp.grupoL.criptop2p.service.PublisherService
import org.springframework.beans.factory.annotation.Autowired
import javax.persistence.*
import kotlin.jvm.Transient
import javax.validation.constraints.*

@Entity
@Table(name = "usuarios")
class Usuario:  EntidadValidable {


   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id_usuario")
    var id: Int?= null


    @Column(nullable = false)
    @NotNull(message = "El nonbre es obligatorio")
    @Size(min = 1, max = 30, message = "el campo name debe contener un minimo de 3 y un máximo de 30 caracteres")
    var name: String? = null

    @Column(nullable = false)
    @NotNull(message = "el apellido es obligatorio")
    @Size(min = 1, max = 30, message = "el campo surname debe contener un minimo de 3 y un máximo de 30 caracteres")
    var surname: String? = null

    @Column(nullable = false, unique = true)
    @NotNull(message = "El mail es obligatorio")
    @Email(message = "El formato de mail no es válido")
    // @Pattern(regexp = "^[^@]+@[^@]+\\.[a-zA-Z] {2,}$", message = "El formato de mail no es válido")
    var email: String? = null

    @Column(nullable = false)
    @NotNull(message = "La direccièn es obligatoria")
    @Size(min = 1, max = 30)
    var address: String? = null

    @Column
    @NotNull(message = "El password es obligatorio")
    //@Size(min = 6)
    @Size(min = 1)
    //al menos 1 minuscula, 1 mayuscula, 1 carac especial y min 6
    var password: String = ""


    @Column(nullable = false)
    @Size(min = 1, max = 22, message = "El CVU debe ser de 22 digitos")
    // @Pattern(regexp = "^\\d{22}$", message = "El CVU debe ser de 22 digitos")
    var cvu: String? = null

    @Column(nullable = false)
    // @Pattern(regexp = "^\\d{8}$", message = "El Wallet Address debe ser de 8 digitos")
    @Size(min = 1, max = 8, message = "ElWallet Address debe ser de 8 digitos")
    var walletAddress: String? = null

    @Transient
    var cantidadOperaciones: Int = 0

    @Transient
    var reputacion: Int = 0

    @Transient
    var notificacionesDeDeposito = mutableListOf<Deposito>()


    constructor() : super() {}
    constructor(
        id: Int?,
        name: String?,
        surname: String?,
        email: String?,
        address: String?,
        password: String,
        cvu: String?,
        walletAddress: String?/*,cantidadOperaciones: Int?*/
    ) : super() {
        this.id = id
        this.name = name
        this.surname = surname
        this.email = email
        this.address = address
        this.password = password
        this.cvu = cvu
        this.walletAddress = walletAddress
        // this.cantidadOperaciones = cantidadOperaciones
    }



    fun setpassword(password: String){
       this.password = password
}


  fun getpassword ():String {
     return password
  }

    fun getOperaciones(): Int {
        return cantidadOperaciones!!
    }

    fun icrementarOperqaciones(){
         cantidadOperaciones += 1
    }

    fun descontarReputacion(numero :Int){
        reputacion -= numero
    }

    fun incrementarReputacion(numero :Int){
        reputacion += numero
    }




  }







