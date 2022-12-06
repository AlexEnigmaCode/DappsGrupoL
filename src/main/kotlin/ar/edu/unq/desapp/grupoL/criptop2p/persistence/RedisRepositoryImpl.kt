package ar.edu.unq.desapp.grupoL.criptop2p.persistence
/*
import ar.edu.unq.desapp.grupoL.criptop2p.CriptoActivoRegisterMapper
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import javax.annotation.PostConstruct
*/
/*
@Configuration
@Repository
@RedisHash

class RedisRepositoryImpl () : RedisRepository  {
   val  key = "CriptoActivoRegisterMapper"


  lateinit var  redisTemplate : RedisTemplate<String, CriptoActivoRegisterMapper>

   lateinit var hashOperations : HashOperations<String, String, CriptoActivoRegisterMapper>


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

*/