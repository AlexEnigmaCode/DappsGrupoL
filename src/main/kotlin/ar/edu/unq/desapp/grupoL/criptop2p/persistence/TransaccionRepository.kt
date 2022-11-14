package ar.edu.unq.desapp.grupoL.criptop2p.persistence


import ar.edu.unq.desapp.grupoL.criptop2p.model.Transaccion
import org.springframework.context.annotation.Configuration
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Configuration
@Repository
interface TransaccionRepository: CrudRepository<Transaccion?, Long?> {
    override fun findById(id: Long): Optional<Transaccion?>
    override fun findAll(): List<Transaccion>
    fun save(transaccion: Transaccion): Transaccion
    override fun deleteById(id: Long)

}