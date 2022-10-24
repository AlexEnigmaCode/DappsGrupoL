package ar.edu.unq.desapp.grupoL.criptop2p.persistence

import ar.edu.unq.desapp.grupoL.criptop2p.model.Publicacion
import ar.edu.unq.desapp.grupoL.criptop2p.model.Transaccion
import org.springframework.context.annotation.Configuration
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Configuration
@Repository
interface TransaccionRepository: CrudRepository<Transaccion?, Int?> {
    override fun findAll(): List<Transaccion>
    fun save(transaccion: Transaccion): Transaccion

}