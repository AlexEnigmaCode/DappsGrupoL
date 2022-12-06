package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.ItemNotFoundException
import ar.edu.unq.desapp.grupoL.criptop2p.model.Usuario
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.UserRepository
import ar.edu.unq.desapp.grupoL.criptop2p.service.JwtUtilService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import java.util.HashMap
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import java.util.stream.Collectors
import org.springframework.security.core.userdetails.UserDetailsService
import kotlin.Throws
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.Map



@Service

class UsuarioDetailsService : UserDetailsService {

    @Autowired
    private lateinit var userRepository: UserRepository


    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {

        val users: List<Usuario> = userRepository.findAll()
        val user: Usuario = users.find { (it.name == username) } ?: throw ItemNotFoundException("Not found user")

        val rol = "USER"
        val userBuilder = User.withUsername(username)
        // "secreto" => [BCrypt] => $2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7iXinRR1iiq
        val encryptedPassword = "$2a$10$56VCAiApLO8NQYeOPiu2De/EBC5RWrTZvLl7uoeC3r7iXinRR1iiq"
        userBuilder.password(encryptedPassword).roles(rol)
        return userBuilder.build()
    }

}

/*
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String?): UserDetails {
        val users: List<Usuario> = userRepository.findAll()
        val user: Usuario = users.find { (it.name == username) } ?: throw ItemNotFoundException("Not found user")
        var roles: List<GrantedAuthority> = listOf()
        roles.toMutableList().add(SimpleGrantedAuthority("USER"))

        val userDet: UserDetails = User(user.name, user.password, roles)
        return userDet

    }

}
*/

