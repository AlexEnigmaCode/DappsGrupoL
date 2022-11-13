package ar.edu.unq.desapp.grupoL.criptop2p.persistence

import ar.edu.unq.desapp.grupoL.criptop2p.CriptoActivoRegisterMapper
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate
import javax.annotation.PostConstruct

class RedisRepositoryInpl : RedisRepository  {
   val  key = "CriptoActivoRegisterMapper"

   lateinit var  redisTemplate : RedisTemplate <String, CriptoActivoRegisterMapper>
   lateinit var hashOperations : HashOperations <String,String, CriptoActivoRegisterMapper>

    fun RedisRepositoryInpl ( redisTemplate: RedisTemplate<String, CriptoActivoRegisterMapper>){
        this.redisTemplate = redisTemplate
    }

    @PostConstruct
     fun init(){
        hashOperations = redisTemplate.opsForHash()
     }


    override fun findAll(): Map<String, CriptoActivoRegisterMapper> {
      return  hashOperations.entries(key)
    }

    override fun findById(id: String): CriptoActivoRegisterMapper {
          return hashOperations.get(key,id)!!
    }

    override fun save(criptoActivo: CriptoActivoRegisterMapper) {
      hashOperations.put(key, criptoActivo.criptoActivo!!/*UUID.randomUUID().toString()*/,criptoActivo)
    }

    override fun saveAll(criptoActivos: Map<String, CriptoActivoRegisterMapper>) {
        hashOperations.putAll(key,criptoActivos)
    }

    override fun deleteById(id: String) {
        hashOperations.delete(key,id)
    }

    override fun count():Long{
        return hashOperations.entries(key).size.toLong()
    }

     override fun listToMap(criptoActivos: List<CriptoActivoRegisterMapper>): Map<String, CriptoActivoRegisterMapper>{
       val criptosMap =  hashMapOf<String, CriptoActivoRegisterMapper>()
       for (criptoActivo in criptoActivos){
           criptosMap.put(criptoActivo.criptoActivo!!, criptoActivo )
       }
         return criptosMap
     }
}