package ar.edu.unq.desapp.grupoL.criptop2p.webservice



import ar.edu.unq.desapp.grupoL.criptop2p.UserViewMapper
import ar.edu.unq.desapp.grupoL.criptop2p.model.CriptoActivo
import ar.edu.unq.desapp.grupoL.criptop2p.service.CriptoActivoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*


@RestController
@EnableAutoConfiguration
@CrossOrigin("*")
class CriptoActivoRestService {

    @Autowired
    private val criptoActivoService: CriptoActivoService? = null
    private val builder: ResponseEntity.BodyBuilder? = null
    private var criptoActivos = listOf<CriptoActivo>()

    @GetMapping("/api/criptoactivos")
    fun allCriptoActivos(): List<CriptoActivo> {
        val list = criptoActivoService?.findAll()
        if (list != null) {
            criptoActivos =  list.map { CriptoActivo(it.criptoactivo, it.cotizacion, it.fecha ) }
        }
        return criptoActivos

    }


     /** Get criptoActivo  by criptoActivo */
    @GetMapping("api/criptoactivos/{symbol}")
    fun findCriptoActivoBySymbol(@PathVariable("symbol") symbol: String): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        try {

            val criptoActivo =  criptoActivoService?.findByCriptoActivo(symbol)

            ResponseEntity.status(200)
            response = ResponseEntity.ok().body(criptoActivo)
        } catch (e: Exception) {
            ResponseEntity.status(404)
            val resultado: MutableMap<String, String> = HashMap()
            resultado["cripto Activo with symbol not found"] = symbol.toString()
            response = ResponseEntity.ok().body<Map<String, String>>(resultado)
        }
        return response !!
    }






}