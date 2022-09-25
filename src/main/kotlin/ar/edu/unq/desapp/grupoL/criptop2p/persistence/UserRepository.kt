package ar.edu.unq.desapp.grupoL.criptop2p.persistence

import ar.edu.unq.desapp.grupoL.criptop2p.model.Usuario
import org.springframework.context.annotation.Configuration
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Configuration
@Repository
interface UserRepository : CrudRepository<Usuario?, Int?> {
    override fun findById(id: Int): Optional<Usuario?>
   // override fun findAll(): MutableIterable<User?>
     override fun findAll(): List<Usuario>
     override fun deleteById(id: Int)
}