package ar.edu.unq.desapp.grupoL.criptop2p.webservice.controller

import ar.edu.unq.desapp.grupoL.criptop2p.service.ConsumerCriptoActivoMicroService
import ar.edu.unq.desapp.grupoL.criptop2p.service.HistoryCriptoActivoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.HashMap


@RestController
@EnableAutoConfiguration
@CrossOrigin("*")

class HistoryCriptoActivoRestService {


    @Autowired
    lateinit var histroryService: HistoryCriptoActivoService

    private val builder: ResponseEntity.BodyBuilder? = null


    /**consume and save all criptoActivos in history */
    @PostMapping("/api/consumecriptoactivos/24hs")
    fun consumeAllCriptoActivos24hs(): ResponseEntity<*> {
        val criptoactivos =  histroryService.consumeCriptoActivos24hs()
        return ResponseEntity.ok().body(criptoactivos)
    }



    /**consume criptoActivo  by symbol avd save in history*/
    @PostMapping("/api/consumecriptoactivos/24hs/{symbol}")
    fun consumeCriptoActivoBySymbol24hs(@PathVariable("symbol") symbol: String): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        try {

            val criptoActivo =  histroryService.consumeBySymbol24hs(symbol)

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


    /**show all criptoActivo in history */
    @GetMapping("/api/consumecriptoactivos/24hs/show")
    fun showAllHistory24hs(): ResponseEntity<*> {
        val criptoactivos =  histroryService.showAllHistory24hs()
        return ResponseEntity.ok().body(criptoactivos)
    }



    /**show criptoActivo  by symbol  in history*/
    @GetMapping("/api/consumecriptoactivos/24hs/show/{symbol}")
    fun showHistoryBySymbol24hs(@PathVariable("symbol") symbol: String): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        try {
            val criptoActivo =  histroryService.showHistory24hsBySymbol(symbol)
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

