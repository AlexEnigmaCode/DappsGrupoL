package ar.edu.unq.desapp.grupoL.criptop2p.persistence

import ar.edu.unq.desapp.grupoL.criptop2p.CriptoActivoRegisterMapper
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Repository

@Configuration
@Repository
interface RedisRepository {

   fun findAll(): Map<String, CriptoActivoRegisterMapper>
   fun findById(id:String): CriptoActivoRegisterMapper
   fun  save (criptoActivo: CriptoActivoRegisterMapper)
   fun saveAll (criptoActivos: Map<String, CriptoActivoRegisterMapper>)
   fun deleteById(id:String )
   fun listToMap(criptoActivos: List<CriptoActivoRegisterMapper>): Map<String, CriptoActivoRegisterMapper>
   fun count():Long
}