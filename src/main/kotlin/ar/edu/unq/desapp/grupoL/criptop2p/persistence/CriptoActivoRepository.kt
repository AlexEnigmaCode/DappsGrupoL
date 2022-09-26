package ar.edu.unq.desapp.grupoL.criptop2p.persistence

import ar.edu.unq.desapp.grupoL.criptop2p.model.CriptoActivo
import org.springframework.context.annotation.Configuration
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository

@Configuration
@Repository
interface CriptoActivoRepository :JpaRepository<CriptoActivo?, Int?> {

    override fun findAll(): List<CriptoActivo>

    fun findByCriptoactivo(symbol:String): CriptoActivo
}