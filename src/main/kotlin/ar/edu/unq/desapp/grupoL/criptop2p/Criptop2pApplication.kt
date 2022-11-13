package ar.edu.unq.desapp.grupoL.criptop2p


import ar.edu.unq.desapp.grupoL.criptop2p.persistence.RedisRepository
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime


@SpringBootApplication
@EnableCaching
class Criptop2pApplication : CommandLineRunner {


	private val LOG = LogFactory.getLog(javaClass)
	lateinit var redisRepository: RedisRepository


	@Autowired
	fun Criptop2pApplicationc(redisRepository: RedisRepository) {
		this.redisRepository = redisRepository
	}


	@Bean
	fun getresttemplate(): RestTemplate {
		return RestTemplate()
	}

	fun main(args: Array<String>) {
		runApplication<Criptop2pApplication>(*args)
	}

	override fun run(vararg args: String?) {

		LOG.info("Saving cripto activos. Current cripto activos count is  ${redisRepository.count()} " )
		val fecha = LocalDateTime.now()
		val cripto1 = CriptoActivoRegisterMapper("A", "100", fecha)
		val cripto2 = CriptoActivoRegisterMapper("B", "200",fecha)
		val cripto3 = CriptoActivoRegisterMapper("C", "300)",fecha)

		redisRepository!!.save(cripto1)
		redisRepository!!.save(cripto2)
		redisRepository!!.save(cripto3)
		LOG.info("Done saving cripto Activos. ${redisRepository.findAll()} ")
	}

}


