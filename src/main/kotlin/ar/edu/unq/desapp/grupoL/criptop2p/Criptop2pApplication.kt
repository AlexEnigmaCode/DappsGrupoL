package ar.edu.unq.desapp.grupoL.criptop2p


//import ar.edu.unq.desapp.grupoL.criptop2p.persistence.RedisRepository
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate


@SpringBootApplication
//@EnableCaching

class Criptop2pApplication /*: CommandLineRunner*/ {


	//private val LOG = LogFactory.getLog(javaClass)
	/*
	lateinit var redisRepository: RedisRepository


	@Autowired
	fun Criptop2pApplicationc(redisRepository: RedisRepository) {
		this.redisRepository = redisRepository
	}

	@Autowired
	lateinit var redisRepository: RedisRepository
*/
/*
	@Bean
	fun getresttemplate(): RestTemplate {
		return RestTemplate()
	}

*/


	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
		SpringApplication.run(ar.edu.unq.desapp.grupoL.criptop2p.Criptop2pApplication::class.java,*args)
		}
	}



}


