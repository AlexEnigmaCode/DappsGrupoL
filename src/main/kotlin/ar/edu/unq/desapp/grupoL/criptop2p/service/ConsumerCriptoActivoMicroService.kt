package ar.edu.unq.desapp.grupoL.criptop2p.service



import ar.edu.unq.desapp.grupoL.criptop2p.service.CriptoActivoService
import ar.edu.unq.desapp.grupoL.criptop2p.Binance
import ar.edu.unq.desapp.grupoL.criptop2p.ItemNotFoundException
import ar.edu.unq.desapp.grupoL.criptop2p.getresttemplate
import ar.edu.unq.desapp.grupoL.criptop2p.model.CriptoActivo
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.CriptoActivoRepository
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

    @Autowired
    private val criptoActivoService: CriptoActivoService? = null


    fun consumeCriptoActivos(): List<CriptoActivo> {
        var criptoActivos = listOf<CriptoActivo>()
        val response: ResponseEntity<Array<Binance>> = restTemplate?.getForEntity(
            "https://api1.binance.com/api/v3/ticker/price",
            Array<Binance>::class.java
        )!!
        val list = response.body?.asList()
        if (list != null) {
            val fecha:String = LocalDateTime.now().toString()
            criptoActivos =  list.map { CriptoActivo(it.symbol, it.price, fecha )}
        }
        //criptoActivoService?.saveAll(criptoActivos)

        return criptoActivos

    }


    fun consumeBySymbol(symbol:String): CriptoActivo {
        val response: ResponseEntity<Binance> = restTemplate?.getForEntity(
            "https://api1.binance.com/api/v3/ticker/price?symbol=$symbol",
            Binance::class.java
        )!!
        val binance = response.body
        if (binance == null)
        {throw ItemNotFoundException ("Cripto Activo with symbol:  $symbol not found")}
        val fecha:String = LocalDateTime.now().toString()
        val  criptoActivo = CriptoActivo(
            binance.symbol,
            binance.price,
            fecha
        )
        return  criptoActivo
    }

}