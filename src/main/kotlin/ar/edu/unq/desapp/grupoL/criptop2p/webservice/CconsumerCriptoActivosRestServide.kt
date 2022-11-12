package ar.edu.unq.desapp.grupoL.criptop2p.webservice

import ar.edu.unq.desapp.grupoL.criptop2p.CriptoActivoRegisterMapper
import ar.edu.unq.desapp.grupoL.criptop2p.HistoricoCotizaciones24hs
import ar.edu.unq.desapp.grupoL.criptop2p.service.ConsumerCriptoActivoMicroService
import ar.edu.unq.desapp.grupoL.criptop2p.service.CriptoActivoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.HashMap


@RestController
@EnableAutoConfiguration
@CrossOrigin("*")
class CconsumerCriptoActivosRestServide {

    @Autowired
    lateinit var consumerService: ConsumerCriptoActivoMicroService
    private val builder: ResponseEntity.BodyBuilder? = null


    @Autowired
    private  lateinit var criptoActivoService: CriptoActivoService


    @GetMapping("/api/consumecriptoactivos")
    fun consumeAllCriptoActivos(): List<CriptoActivoRegisterMapper> {
        return consumerService.consumeCriptoActivos()

    }


    /**consume criptoActivo  by symbol*/
    @GetMapping("/api/consumecriptoactivos/{symbol}")
    fun consumeCriptoActivoBySymbol(@PathVariable("symbol") symbol: String): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        try {

            val criptoActivo =  consumerService.consumeBySymbol(symbol)

            ResponseEntity.status(200)
            response = ResponseEntity.ok().body(criptoActivo)
        } catch (e: Exception) {
            ResponseEntity.status(404)
            val resultado: MutableMap<String, String> = HashMap()
            resultado["cripto Activo with symbol not found"] = symbol
            response = ResponseEntity.ok().body<Map<String, String>>(resultado)
        }
        return response !!
    }

    @GetMapping("/api/consumecriptoactivos")
    fun consumeAllCriptoActivos24hs(): List<HistoricoCotizaciones24hs> {
        return consumerService.consumeCriptoActivos24hs()

    }


    /**consume criptoActivo  by symbol*/
    @GetMapping("/api/consumecriptoactivos/{symbol}")
    fun consumeCriptoActivoBySymbol24hs(@PathVariable("symbol") symbol: String): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        try {

            val criptoActivo =  consumerService.consumeBySymbol24hs(symbol)

            ResponseEntity.status(200)
            response = ResponseEntity.ok().body(criptoActivo)
        } catch (e: Exception) {
            ResponseEntity.status(404)
            val resultado: MutableMap<String, String> = HashMap()
            resultado["cripto Activo with symbol not found"] = symbol
            response = ResponseEntity.ok().body<Map<String, String>>(resultado)
        }
        return response !!
    }


}