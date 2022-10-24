package ar.edu.unq.desapp.grupoL.criptop2p.webservice.controller

import ar.edu.unq.desapp.grupoL.criptop2p.BetweenDates
import ar.edu.unq.desapp.grupoL.criptop2p.service.TransactionerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.HashMap


@RestController
@EnableAutoConfiguration
@CrossOrigin("*")

class TransactionerRestService {

    @Autowired
    private  lateinit var  transactionerService: TransactionerService

    @PutMapping("/api/transacciones/{id}")
    fun volumenOperadoEntreFechas (@PathVariable("id") id: Long,@RequestBody entity: BetweenDates): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        try {
            val userview =  transactionerService.volumenOperadoEntreFechas(id,entity.fecha1,entity.fecha2)
            ResponseEntity.status(200)
            response = ResponseEntity.ok().body(userview)
        } catch (e: Exception) {
            ResponseEntity.status(404)

            val resultado: MutableMap<String, String> = HashMap()
            resultado["usuario con id no encontrado"] = id.toString()
            response = ResponseEntity.ok().body<Map<String, String>>(resultado)
        }
        return response !!
    }
}
