package ar.edu.unq.desapp.grupoL.criptop2p.model

import ar.edu.unq.desapp.grupoL.criptop2p.UserViewMapper
import java.io.Serializable


class TokenInfo(val jwtToken: String) : Serializable {

    companion object {
        private const val serialVersionUID = 1L
    }
}
