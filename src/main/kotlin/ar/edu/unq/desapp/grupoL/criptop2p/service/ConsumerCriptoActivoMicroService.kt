package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.*
import ar.edu.unq.desapp.grupoL.criptop2p.model.CriptoActivo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
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

    fun consumeCriptoActivos24hs(): List<HistoricoCotizaciones24hs> {
        var historicos = listOf<HistoricoCotizaciones24hs >()
        val response: ResponseEntity<Array<Binance24hs>> = restTemplate?.getForEntity(
            "https://api1.binance.com/api/v3/ticker/24hr",
            Array<Binance24hs>::class.java
        )!!
        val list = response.body?.asList()
        if (list != null) {

            historicos =  list.map { binance24hsToHistorico (it) }
        }
        return  historicos
    }



    fun consumeBySymbol24hs(symbol:String): HistoricoCotizaciones24hs {
       val response: ResponseEntity<Binance24hs> = restTemplate?.getForEntity(
            "https://api1.binance.com/api/v3/ticker/24hr?symbol=$symbol", Binance24hs::class.java
        )!!
        val binance24hs:Binance24hs? = response.body
        if (binance24hs == null)
        {throw ItemNotFoundException ("Cripto Activo with symbol:  $symbol not found")}
        return  binance24hsToHistorico (binance24hs)

    }

    fun binance24hsToHistorico (binance24hs: Binance24hs): HistoricoCotizaciones24hs{
        val cotizaciones24hs = mutableListOf<Binance24hsMapper>()
        for  ( seconds in binance24hs.openTime..binance24hs.closeTime){
            val date = LocalDateTime.ofInstant(Instant.ofEpochSecond(seconds), ZoneId.systemDefault())
            val binance24hsMapper = Binance24hsMapper (binance24hs.lastPrice, date)
            cotizaciones24hs.add(binance24hsMapper)
        }

        val historico = HistoricoCotizaciones24hs(binance24hs.symbol, cotizaciones24hs)
        return  historico
    }

}