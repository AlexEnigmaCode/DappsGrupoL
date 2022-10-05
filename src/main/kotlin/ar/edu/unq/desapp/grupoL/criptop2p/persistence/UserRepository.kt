package ar.edu.unq.desapp.grupoL.criptop2p.persistence


import ar.edu.unq.desapp.grupoL.criptop2p.UserRegisterMapper
import ar.edu.unq.desapp.grupoL.criptop2p.model.Usuario
import org.springframework.context.annotation.Configuration
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Configuration
@Repository
interface UserRepository : CrudRepository<Usuario?, Long?> {
    override fun findById(id: Long): Optional<Usuario?>
    fun save(usuario: UserRegisterMapper): Usuario
     override fun findAll(): List<Usuario>
     override fun deleteById(id: Long)
}