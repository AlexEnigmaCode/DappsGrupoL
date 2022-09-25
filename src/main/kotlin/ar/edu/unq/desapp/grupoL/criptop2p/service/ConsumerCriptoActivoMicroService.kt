package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.getresttemplate
import ar.edu.unq.desapp.grupoL.criptop2p.model.CriptoActivo
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.CriptoActivoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.*

@Service
internal class ConsumerCriptoActivoMicroService {


    private var restTemplate: RestTemplate = getresttemplate()

    fun consumeCriptoActivos(): List<CriptoActivo> {
        val response: ResponseEntity<Array<CriptoActivo>> = restTemplate.getForEntity(
            "https://api1.binance.com/api/v3/ticker/price/",
           Array<CriptoActivo>::class.java
        )
       val criptoActivos = response.body!!.asList()
       return criptoActivos
    }
}