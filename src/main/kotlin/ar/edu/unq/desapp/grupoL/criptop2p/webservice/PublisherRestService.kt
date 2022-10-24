package ar.edu.unq.desapp.grupoL.criptop2p.webservice


import ar.edu.unq.desapp.grupoL.criptop2p.IntencionRegisterMapper
import ar.edu.unq.desapp.grupoL.criptop2p.service.ConsumerCriptoActivoMicroService
import ar.edu.unq.desapp.grupoL.criptop2p.service.PublisherService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.HashMap


@RestController
@EnableAutoConfiguration
@CrossOrigin("*")
class PublisherRestService {

    @Autowired
    private lateinit var  publisherService: PublisherService

    @Autowired
    private  lateinit var consumer : ConsumerCriptoActivoMicroService



    /** Publish an intention for a user*/
    @PostMapping("/api/publicaciones/{id}")
    fun publicar (@PathVariable("id") id: Long, @RequestBody entity: IntencionRegisterMapper): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        try {

            val  criptoActivo = consumer.consumeBySymbol(entity.criptoactivo!!)
            val cotizacion =  criptoActivo.cotizacion!!.toDouble()
            val publicacion = publisherService!!.publicar(id,entity,cotizacion)

            ResponseEntity.status(200)
            response = ResponseEntity.ok().body(publicacion)
        } catch (e: Exception) {
            ResponseEntity.status(404)

            val resultado: MutableMap<String, String> = HashMap()
            resultado["usuario con id no encontrado"] = id.toString()
            response = ResponseEntity.ok().body<Map<String, String>>(resultado)
        }
        return response !!
    }

    @GetMapping("api/publicaciones")
    fun listarPublicaciones(): ResponseEntity<*> {
        val publicaciones = publisherService.findAll()

        return ResponseEntity.ok().body(publicaciones)

    }

}