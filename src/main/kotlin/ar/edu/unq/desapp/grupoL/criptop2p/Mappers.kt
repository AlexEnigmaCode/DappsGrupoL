package ar.edu.unq.desapp.grupoL.criptop2p

data class UserRegisterMapper(
    val name: String? = null,
    val surname: String? = null,
    val email:String?= null,
    val address:String?= null,
    val password: String? = null,
    val cvu:Int?= null,
    val walletAddress:Int? = null
)

data class UserLoginMapper(val email: String, val password: String)

data class UserUpdateMapper(val email:String,
                            val address:String,
                            val password: String,
                            val cvu:Int,
                            val walletAddress:Int?)

data class UserViewMapper(val id: Int?,
                          val name: String?,
                          val surname: String?,
                          val email:String?,
                          val address:String?,
                          val cvu:Int?,
                          val walletAddress:Int?)
