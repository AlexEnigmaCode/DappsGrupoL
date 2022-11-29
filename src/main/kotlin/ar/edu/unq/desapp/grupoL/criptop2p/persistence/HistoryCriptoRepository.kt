package ar.edu.unq.desapp.grupoL.criptop2p.persistence

import ar.edu.unq.desapp.grupoL.criptop2p.model.HistoryCriptoActivo
import org.springframework.context.annotation.Configuration
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Configuration
@Repository
interface HistoryCriptoRepository : CrudRepository<HistoryCriptoActivo?, Long?> {

   fun save(historyoryCriptoActivo: HistoryCriptoActivo): HistoryCriptoActivo
   fun saveAll (criptoactivos : List<HistoryCriptoActivo>): List<HistoryCriptoActivo>
   override fun findAll(): List<HistoryCriptoActivo>

    }
