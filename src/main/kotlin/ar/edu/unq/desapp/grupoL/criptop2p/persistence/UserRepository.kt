package ar.edu.unq.desapp.grupoL.criptop2p.persistence

import ar.edu.unq.desapp.grupoL.criptop2p.model.User
import org.springframework.context.annotation.Configuration
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Configuration
@Repository
interface UserRepository : CrudRepository<User?, Int?> {
    override fun findById(id: Int): Optional<User?>
    override fun findAll(): List<User?>
    override fun deleteById(id: Int)
}