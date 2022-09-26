package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.Binance
import ar.edu.unq.desapp.grupoL.criptop2p.getresttemplate
import ar.edu.unq.desapp.grupoL.criptop2p.model.CriptoActivo
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.CriptoActivoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.*

@Service
internal class ConsumerCriptoActivoMicroService {



    @Bean
    fun getresttemplate(): RestTemplate{
        return RestTemplate()
    }

    @Autowired
    private val restTemplate: RestTemplate? = null

    fun consumeCriptoActivos(): List<Binance> {
        val response: ResponseEntity<Array<Binance>> = restTemplate?.getForEntity(
            "https://api1.binance.com/api/v3/ticker/price",
            Array<Binance>::class.java
        )!!
       val criptoActivos = response.body!!.asList()

       return criptoActivos
    }


    fun consumeBySymbol(symbol:String): Binance {
        val response: ResponseEntity<Binance> = restTemplate?.getForEntity(
            "https://api1.binance.com/api/v3/ticker/price?symbol=$symbol",
            Binance::class.java
        )!!
        val criptoActivo = response.body!!

        return  criptoActivo
    }

}