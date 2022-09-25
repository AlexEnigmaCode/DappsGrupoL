package ar.edu.unq.desapp.grupoL.criptop2p

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate



@SpringBootApplication
class Criptop2pApplication

@Bean
fun getresttemplate(): RestTemplate{
	return RestTemplate()
}

fun main(args: Array<String>) {
	runApplication<Criptop2pApplication>(*args)
}
