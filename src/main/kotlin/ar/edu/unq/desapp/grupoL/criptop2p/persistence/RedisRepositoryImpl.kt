package ar.edu.unq.desapp.grupoL.criptop2p.persistence

import ar.edu.unq.desapp.grupoL.criptop2p.CriptoActivoRegisterMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.data.keyvalue.repository.KeyValueRepository
import org.springframework.data.redis.core.HashOperations
//import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct
/*
@Configuration
//@Repository
//@RedisHash

 */
/*
abstract class RedisRepositoryImpl ( final val redisTemplate: RedisTemplate<String, CriptoActivoRegisterMapper>) : /*RedisRepository ,*/ KeyValueRepository<String,CriptoActivoRegisterMapper> {
   val  key = "CriptoActivoRegisterMapper"


 // lateinit var  redisTemplate : RedisTemplate<String, CriptoActivoRegisterMapper>

   lateinit var hashOperations : HashOperations<String, String, CriptoActivoRegisterMapper>

   // fun RedisRepositoryInpl ( redisTemplate: RedisTemplate<String, CriptoActivoRegisterMapper>){
     //   this.redisTemplate = redisTemplate
  // }



    @PostConstruct
     fun init(){
        hashOperations = redisTemplate.opsForHash()
     }

/*
    override fun findAll(): Map<String, CriptoActivoRegisterMapper> {
      return  hashOperations.entries(key)
    }
*/

      fun getAll(): HashMap<String, CriptoActivoRegisterMapper> {
        return  hashOperations.entries(key) as HashMap<String, CriptoActivoRegisterMapper>
    }

    /*
    override fun findById(id: String): CriptoActivoRegisterMapper {
          return hashOperations.get(key,id)!!
    }
*/


   fun findById(id: String): CriptoActivoRegisterMapper {
      return hashOperations.get(key, id)!!
  }
/*
override fun save(criptoActivo: CriptoActivoRegisterMapper) {
 hashOperations.put(key, criptoActivo.criptoActivo!!/*UUID.randomUUID().toString()*/,criptoActivo)
}
*/
    fun save(criptoActivo: CriptoActivoRegisterMapper) {
    hashOperations.put(key, criptoActivo.criptoActivo!!/*UUID.randomUUID().toString()*/,criptoActivo)
}

 /*
override fun saveAll(criptoActivos: Map<String, CriptoActivoRegisterMapper>) {
  hashOperations.putAll(key,criptoActivos)
}
*/
    fun saveAll(criptoActivos: Map<String, CriptoActivoRegisterMapper>) {
     hashOperations.putAll(key,criptoActivos)
 }
  /*
override fun deleteById(id: String) {
   hashOperations.delete(key,id)
}
*/

     fun deleteById(id: String) {
        hashOperations.delete(key,id)
    }

override fun count():Long{
   return hashOperations.entries(key).size.toLong()
}
/*
override fun listToMap(criptoActivos: List<CriptoActivoRegisterMapper>): Map<String, CriptoActivoRegisterMapper>{
  val criptosMap =  hashMapOf<String, CriptoActivoRegisterMapper>()
  for (criptoActivo in criptoActivos){
      criptosMap.put(criptoActivo.criptoActivo!!, criptoActivo )
  }
    return criptosMap
}

 */

     fun listToMap(criptoActivos: List<CriptoActivoRegisterMapper>): Map<String, CriptoActivoRegisterMapper>{
        val criptosMap =  hashMapOf<String, CriptoActivoRegisterMapper>()
        for (criptoActivo in criptoActivos){
            criptosMap.put(criptoActivo.criptoActivo!!, criptoActivo )
        }
        return criptosMap
    }
}

*/