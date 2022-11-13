package ar.edu.unq.desapp.grupoL.criptop2p.persistence


import ar.edu.unq.desapp.grupoL.criptop2p.model.CriptoActivo
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository


@Configuration
@Repository
interface CriptoActivoRepository :JpaRepository<CriptoActivo?, Long?> {

    fun save(criptoActivo: CriptoActivo): CriptoActivo
    override fun findAll(): List<CriptoActivo>



}