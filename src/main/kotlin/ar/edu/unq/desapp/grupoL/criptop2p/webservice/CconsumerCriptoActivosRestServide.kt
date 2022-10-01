package ar.edu.unq.desapp.grupoL.criptop2p.webservice

import ar.edu.unq.desapp.grupoL.criptop2p.UserViewMapper
import ar.edu.unq.desapp.grupoL.criptop2p.model.CriptoActivo
import ar.edu.unq.desapp.grupoL.criptop2p.service.ConsumerCriptoActivoMicroService
import ar.edu.unq.desapp.grupoL.criptop2p.service.CriptoActivoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.HashMap


@RestController
@EnableAutoConfiguration
@CrossOrigin("*")
class CconsumerCriptoActivosRestServide {

    @Autowired
    lateinit var consumerService: ConsumerCriptoActivoMicroService
    private val builder: ResponseEntity.BodyBuilder? = null
    private var criptoActivos = listOf<CriptoActivo>()

    @Autowired
    private  lateinit var criptoActivoService: CriptoActivoService


    @GetMapping("/api/consumecriptoactivos")
    fun consumeAllCriptoActivos(): List<CriptoActivo> {
        return consumerService.consumeCriptoActivos()
        /*
            val list = consumerService?.consumeCriptoActivos()
                  if (list != null) {
                 val fecha:String = LocalDateTime.now().toString()
                criptoActivos =  list.map { CriptoActivo(it.symbol, it.price, fecha )}
            }
              criptoActivoService.saveAll(criptoActivos)
            return criptoActivos
        */
    }



    /**consume criptoActivo  by symbol*/
    @GetMapping("/api/consumecriptoactivos/{symbol}")
    fun consumeCriptoActivoBySymbol(@PathVariable("symbol") symbol: String): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        try {
            val fecha = LocalDateTime.now().toString()
            val criptoActivo =  consumerService.consumeBySymbol(symbol)
            /*
             val criptoActivo = CriptoActivo(
                 newCriptoActivo!!.symbol,
                 newCriptoActivo.price,
                 fecha
             )

             */
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