package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.Binance
import ar.edu.unq.desapp.grupoL.criptop2p.CriptoActivoRegisterMapper
import ar.edu.unq.desapp.grupoL.criptop2p.ItemNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime
import java.util.*

@Service
class ConsumerCriptoActivoMicroService {



    @Bean
    fun getresttemplate(): RestTemplate{
        return RestTemplate()
    }

    @Autowired
    private val restTemplate: RestTemplate? = null



    fun consumeCriptoActivos(): List<CriptoActivoRegisterMapper> {
        var criptoActivos = listOf<CriptoActivoRegisterMapper>()
        val response: ResponseEntity<Array<Binance>> = restTemplate?.getForEntity(
            "https://api1.binance.com/api/v3/ticker/price",
            Array<Binance>::class.java
        )!!
        val list = response.body?.asList()
        if (list != null) {
            val fecha:String = LocalDateTime.now().toString()
            criptoActivos =  list.map { CriptoActivoRegisterMapper(it.symbol, it.price, fecha )}
        }

        return criptoActivos

    }


    fun consumeBySymbol(symbol:String): CriptoActivoRegisterMapper {
        val response: ResponseEntity<Binance> = restTemplate?.getForEntity(
            "https://api1.binance.com/api/v3/ticker/price?symbol=$symbol",
            Binance::class.java
        )!!
        val binance = response.body
        if (binance == null)
        {throw ItemNotFoundException ("Cripto Activo with symbol:  $symbol not found")}
        val fecha:String = LocalDateTime.now().toString()
        val  criptoActivo = CriptoActivoRegisterMapper(
            binance.symbol,
            binance.price,
            fecha
        )
        return  criptoActivo
    }

}