package ar.edu.unq.desapp.grupoL.criptop2p.persistence


import ar.edu.unq.desapp.grupoL.criptop2p.PublicacionRegisterMapper
import ar.edu.unq.desapp.grupoL.criptop2p.model.CriptoActivo
import ar.edu.unq.desapp.grupoL.criptop2p.model.Publicacion
import org.springframework.context.annotation.Configuration
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Configuration
@Repository
interface PublicacionRepository : CrudRepository<Publicacion?, Int?> {
    override fun findAll(): List<Publicacion>
    fun save(publicacion: PublicacionRegisterMapper): Publicacion
}