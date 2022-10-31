package ar.edu.unq.desapp.grupoL.criptop2p.model

import ar.edu.unq.desapp.grupoL.criptop2p.UserRegisterMapper
import org.springframework.stereotype.Service


@Service
data class UsuarioMapper(
    var id: Long?,
    var name: String?,
    var surname: String?,
    var email: String?,
    var address: String?,
    var password: String?,
    var cvu: String?,
    var walletAddress: String?
) {

    companion object {
        fun desdeModelo(user: Usuario): UsuarioMapper {

            return UsuarioMapper(

                user.id,
                user.name!!,
                user.surname!!,
                user.email!!,
                user.address!!,
                user.password!!,
                user.cvu!!,
                user.walletAddress!!
            )
        }


        fun instancia():UsuarioMapper {

            return UsuarioMapper(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
            )

        }
    }



    fun aModelo (user: UserRegisterMapper): Usuario {
        val usuario = Usuario(
           id,
            user.name,
            user.surname,
            user.email,
            user.address,
            user.password!!,
            user.cvu,
            user.walletAddress,
            0,
            0.0
        )
        return usuario
    }

}