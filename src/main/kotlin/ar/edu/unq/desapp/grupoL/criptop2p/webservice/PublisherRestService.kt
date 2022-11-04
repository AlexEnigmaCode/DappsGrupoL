package ar.edu.unq.desapp.grupoL.criptop2p.webservice


import ar.edu.unq.desapp.grupoL.criptop2p.IntencionRegisterMapper
import ar.edu.unq.desapp.grupoL.criptop2p.UserViewMapper
import ar.edu.unq.desapp.grupoL.criptop2p.model.Publicacion
import ar.edu.unq.desapp.grupoL.criptop2p.service.ConsumerCriptoActivoMicroService
import ar.edu.unq.desapp.grupoL.criptop2p.service.PublisherService
import ar.edu.unq.desapp.grupoL.criptop2p.service.UserService
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

    @Autowired
    private  lateinit var userService : UserService


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


    /**get publication by id**/
    @GetMapping("/api/publicaciones/{id}/{idUsuario}")
    fun selectById(@PathVariable("id") id: Long,@PathVariable("idUsuario") idUsuario: Long): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        try {
            val newPublication = publisherService.selectByID(id,idUsuario)

            ResponseEntity.status(200)
            response = ResponseEntity.ok().body(newPublication)
        } catch (e: Exception) {
            ResponseEntity.status(404)
            val resultado: MutableMap<String, String> = HashMap()
            resultado["Publication with id not found"] = id.toString()
            response = ResponseEntity.ok().body<Map<String, String>>(resultado)
        }
        return response !!
    }

    /** Confirm  an intention */
    @PostMapping("/api/publicaciones/confirmations/{id}")
    fun confirm (@PathVariable("id") id: Long, @RequestBody publication: Publicacion) :ResponseEntity<*>{
        var response : ResponseEntity<*>?
        try {
            val newUser = userService.findByID(id)
            val newTransaction = publisherService.confirm(newUser,publication)

            ResponseEntity.status(200)
            response = ResponseEntity.ok().body(newTransaction)
        } catch (e: Exception) {
            ResponseEntity.status(404)
            val resultado: MutableMap<String, String> = HashMap()
            resultado["User with id not found"] = id.toString()
            response = ResponseEntity.ok().body<Map<String, String>>(resultado)
        }
        return response !!
    }





}