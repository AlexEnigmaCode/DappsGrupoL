package ar.edu.unq.desapp.grupoL.criptop2p.persistence

import ar.edu.unq.desapp.grupoL.criptop2p.PublicacionRegisterMapper
import ar.edu.unq.desapp.grupoL.criptop2p.model.CriptoActivo
import ar.edu.unq.desapp.grupoL.criptop2p.model.CriptoActivoRegisterMapper
import ar.edu.unq.desapp.grupoL.criptop2p.model.Publicacion
import org.springframework.context.annotation.Configuration
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository

@Configuration
@Repository
interface CriptoActivoRepository :CrudRepository<CriptoActivo?, Int?> {

    fun save(criptoActivo: CriptoActivoRegisterMapper): CriptoActivo
    override fun findAll(): List<CriptoActivo>



}