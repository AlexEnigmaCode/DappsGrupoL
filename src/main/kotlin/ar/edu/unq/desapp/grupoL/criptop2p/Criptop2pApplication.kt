package ar.edu.unq.desapp.grupoL.criptop2p

import ar.edu.unq.desapp.grupoL.criptop2p.persistence.CriptoActivoRepository
import ar.edu.unq.desapp.grupoL.criptop2p.service.CriptoActivoService
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate


@SpringBootApplication
@EnableCaching
class Criptop2pApplication : CommandLineRunner {


	private val LOG = LogFactory.getLog(javaClass)
	private var criptoService: CriptoActivoService? = null


	@Autowired
	fun Criptop2pApplicationc(criptoActivoService: CriptoActivoService) {
		criptoService = criptoActivoService
	}


	@Bean
	fun getresttemplate(): RestTemplate {
		return RestTemplate()
	}

	fun main(args: Array<String>) {
		runApplication<Criptop2pApplication>(*args)
	}

	override fun run(vararg args: String?) {

		LOG.info("Saving cripto activos. Current cripto activos count is  ${criptoService?.count()} " )
		val cripto1 = Binance("A", "100")
		val cripto2 = Binance("B", "200")
		val cripto3 = Binance("C", "300)")

		criptoService!!.save(cripto1)
		criptoService!!.save(cripto2)
		criptoService!!.save(cripto3)
		LOG.info("Done saving cripto Activos. ${criptoService?.findAll()} ")
	}

}


