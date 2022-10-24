package ar.edu.unq.desapp.grupoL.criptop2p.webservice.controller

import ar.edu.unq.desapp.grupoL.criptop2p.BetweenDates
import ar.edu.unq.desapp.grupoL.criptop2p.IntencionRegisterMapper
import ar.edu.unq.desapp.grupoL.criptop2p.model.Publicacion
import ar.edu.unq.desapp.grupoL.criptop2p.model.Transaccion
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.TransaccionRepository
import ar.edu.unq.desapp.grupoL.criptop2p.service.ConsumerCriptoActivoMicroService
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

    @Autowired
    private  lateinit var consumer : ConsumerCriptoActivoMicroService

    @Autowired
    private lateinit var transactionerRepository: TransaccionRepository


    @PutMapping("/api/transacciones/volumen/{id}")
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


    /** Proccess a transaction for a user*/
    @PostMapping("/api/transacciones/{id}")
    fun procesarTransaccion (@PathVariable("id") id: Long, @RequestBody entity: Publicacion): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        try {

            val  criptoActivo = consumer.consumeBySymbol(entity.criptoactivo!!)
            val cotizacion =  criptoActivo.cotizacion!!.toDouble()
            val transaccion =  transactionerService.procesarTransaccion(id,entity,cotizacion)

            ResponseEntity.status(200)
            response = ResponseEntity.ok().body(transaccion)
        } catch (e: Exception) {
            ResponseEntity.status(404)

            val resultado: MutableMap<String, String> = HashMap()
            resultado["usuario con id no encontrado"] = id.toString()
            response = ResponseEntity.ok().body<Map<String, String>>(resultado)
        }
        return response !!
    }

    @GetMapping("api/transacciones")
    fun listarPublicaciones(): ResponseEntity<*> {
        val publicaciones = transactionerService.transacciones()

        return ResponseEntity.ok().body(publicaciones)

    }




}
