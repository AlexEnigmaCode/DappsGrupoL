package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.*
import ar.edu.unq.desapp.grupoL.criptop2p.model.HistoryCriptoActivo
import ar.edu.unq.desapp.grupoL.criptop2p.model.Transaccion
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.CriptoActivoRepository
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.HistoryCriptoRepository
//import ar.edu.unq.desapp.grupoL.criptop2p.model.CriptoActivo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Service
class ConsumerCriptoActivoMicroService {

    @Bean
    fun getresttemplate(): RestTemplate{
        return RestTemplate()
    }

    @Autowired
    private val restTemplate: RestTemplate? = null


    @Transactional
    fun consumeCriptoActivos(): List<CriptoActivoRegisterMapper> {
        var criptoActivos = listOf<CriptoActivoRegisterMapper>()
        val response: ResponseEntity<Array<Binance>> = restTemplate?.getForEntity(
            "https://api1.binance.com/api/v3/ticker/price",
            Array<Binance>::class.java
        )!!
        val list = response.body?.asList()
        if (list != null) {
            val fecha:LocalDateTime = LocalDateTime.now()
            criptoActivos =  list.map { CriptoActivoRegisterMapper ( it.symbol, it.price, fecha)}
        }
        return criptoActivos
    }


    @Transactional
    fun consumeBySymbol(symbol:String): CriptoActivoRegisterMapper {
        val response: ResponseEntity<Binance> = restTemplate?.getForEntity(
            "https://api1.binance.com/api/v3/ticker/price?symbol=$symbol",
            Binance::class.java
        )!!
        val binance = response.body
        if (binance == null)
        {throw ItemNotFoundException ("Cripto Activo with symbol:  $symbol not found")}
        val fecha:LocalDateTime = LocalDateTime.now()
        val  criptoActivo = CriptoActivoRegisterMapper(
            binance.symbol,
            binance.price,
            fecha
        )
        return  criptoActivo
    }




}