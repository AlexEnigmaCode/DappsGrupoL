package ar.edu.unq.desapp.grupoL.criptop2p.webservice


import ar.edu.unq.desapp.grupoL.criptop2p.Binance
import ar.edu.unq.desapp.grupoL.criptop2p.CriptoActivoRegisterMapper
import ar.edu.unq.desapp.grupoL.criptop2p.model.CriptoActivo
import ar.edu.unq.desapp.grupoL.criptop2p.service.CriptoActivoService
//import ar.edu.unq.desapp.grupoL.criptop2p.service.SchedulerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@EnableAutoConfiguration
@CrossOrigin("*")
class CriptoActivoRestService {

    // @Autowired
    // lateinit var schedullerService: SchedulerService

    @Autowired
    private val criptoActivoService: CriptoActivoService? = null
    private val builder: ResponseEntity.BodyBuilder? = null
    private var criptoActivos = listOf<CriptoActivo>()

    /*
    @GetMapping("/api/scheduller/criptoactivos")
    fun getAllCriptoActivos(): List<CriptoActivoRegisterMapper> {
        return schedullerService.getCriptoActivos()


    }


    /**Get criptoActivo  by symbol*/
    @GetMapping("/api/scheduller/criptoactivos/{symbol}")
    fun getCriptoActivoBySymbol(@PathVariable("symbol") symbol: String): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        try {

            val criptoActivo =  schedullerService.getBySymbol(symbol)

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

*/
    @PostMapping("/api/criptoactivos/save")
    fun saveCriptoActivo(@RequestBody binance: Binance): CriptoActivo {
        return criptoActivoService!!.save(binance)

    }

    @GetMapping("/api/criptoactivos")
    fun allCriptoActivos(): List<CriptoActivo> {
        return criptoActivoService!!.findAll()

    }

    /** Get criptoActivo  by criptoActivo */
    @GetMapping("api/criptoactivos/{symbol}")
    fun findCriptoActivoBySymbol(@PathVariable("symbol") symbol: String): ResponseEntity<*> {
        var response: ResponseEntity<*>?
        try {

            val criptoActivo = criptoActivoService?.findByCriptoActivo(symbol)

            ResponseEntity.status(200)
            response = ResponseEntity.ok().body(criptoActivo)
        } catch (e: Exception) {
            ResponseEntity.status(404)
            val resultado: MutableMap<String, String> = HashMap()
            resultado["cripto Activo with symbol not found"] = symbol.toString()
            response = ResponseEntity.ok().body<Map<String, String>>(resultado)
        }
        return response!!
    }

}