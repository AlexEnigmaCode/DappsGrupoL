package ar.edu.unq.desapp.grupoL.criptop2p

data class UserRegisterMapper(
    val name: String? = null,
    val surname: String? = null,
    val email:String?= null,
    val adress:String?= null,
    val password: String? = null,
    val cvu:Int?= null,
    val walletAdress:Int? = null
)

data class UserLoginMapper(val email: String, val password: String)

data class UserUpdateMapper(val email:String,
                            val adress:String,
                            val password: String,
                            val cvu:Int,
                            val walletAdress:Int?)

data class UserViewMapper(val id: Int?,
                          val name: String?,
                          val surname: String?,
                          val email:String?,
                          val adress:String?,
                          val cvu:Int?,
                          val walletAdress:Int?)
