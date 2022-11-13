package ar.edu.unq.desapp.grupoL.criptop2p.persistence



import ar.edu.unq.desapp.grupoL.criptop2p.model.Publicacion
import org.springframework.context.annotation.Configuration
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Configuration
@Repository
interface PublicacionRepository : CrudRepository<Publicacion?, Long?> {
    override fun findById(id: Long): Optional<Publicacion?>
    override fun findAll(): List<Publicacion>
    fun save(publicacion: Publicacion): Publicacion
    override fun deleteById(id: Long)
}