package ar.edu.unq.desapp.grupoL.criptop2p.webservice


import ar.edu.unq.desapp.grupoL.criptop2p.*
import ar.edu.unq.desapp.grupoL.criptop2p.model.Publicacion
import ar.edu.unq.desapp.grupoL.criptop2p.model.Transaccion
import ar.edu.unq.desapp.grupoL.criptop2p.model.Usuario
import ar.edu.unq.desapp.grupoL.criptop2p.service.ConsumerCriptoActivoMicroService
import ar.edu.unq.desapp.grupoL.criptop2p.service.DTOService
import ar.edu.unq.desapp.grupoL.criptop2p.service.PublisherService
import ar.edu.unq.desapp.grupoL.criptop2p.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.HashMap


@RestController
@EnableAutoConfiguration
@CrossOrigin("*")
class PublisherRestService   {

    @Autowired
    private lateinit var  publisherService: PublisherService

    @Autowired
    private  lateinit var consumer : ConsumerCriptoActivoMicroService

    @Autowired
    private  lateinit var userService : UserService

    @Autowired
    private  lateinit var dtoService : DTOService


    /** Publish an intention for a user*/
    @PostMapping("/api/publicaciones/{id}")
    fun publicar (@PathVariable("id") id: Long, @RequestBody entity: IntencionRegisterMapper): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        try {

            val  criptoActivo = consumer.consumeBySymbol(entity.criptoactivo!!)
            val cotizacion =  criptoActivo.cotizacion!!.toDouble()
            val publicacion = publisherService.publicar(id,entity,cotizacion)
            val publicacionDTO = dtoService.publicacionToPublicacionViewMapper(publicacion)

            ResponseEntity.status(200)


              response = ResponseEntity.ok().body(publicacionDTO)

            } catch (e: Exception) {
            ResponseEntity.status(404)

            val resultado: MutableMap<String, String> = HashMap()
            resultado["Error"] = e.message.toString()
            response = ResponseEntity.ok().body<Map<String, String>>(resultado)
        }
        return response !!
    }



    @GetMapping("api/publicaciones")
    fun listarPublicaciones(): ResponseEntity<*> {
        val publicaciones = publisherService.findAll()
        val publicacionesDto =  publicaciones.map{ dtoService.publicacionToPublicacionViewMapper(it)}
        return ResponseEntity.ok().body(publicacionesDto)
    }



    /**get publication by id**/
    @GetMapping("/api/publicaciones/{id}/{idUsuario}")
    fun selectById(@PathVariable("idUsuario") idUsuario: Long, @PathVariable("id") idPublicacion: Long): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        try {
            val publicacion = publisherService.selectByID(idPublicacion,idUsuario)
            val publicacionDTO = dtoService.publicacionToPublicacionViewMapper(publicacion)
            ResponseEntity.status(200)
            response = ResponseEntity.ok().body(publicacionDTO)
        } catch (e: Exception) {
            ResponseEntity.status(404)
            val resultado: MutableMap<String, String> = HashMap()
            resultado["Error"] = e.message.toString()
            response = ResponseEntity.ok().body<Map<String, String>>(resultado)
        }
        return response !!
    }

    /** Confirm  an intention */
    @PostMapping("/api/publicaciones/confirmations/{id}/{idPublicacion}")
    fun confirm (@PathVariable("id") id: Long, @PathVariable("idPublicacion") idPublicacion: Long /*@RequestBody publication: Publicacion*/) :ResponseEntity<*>{
        var response : ResponseEntity<*>?
        try {
            val newUser = userService.findByID(id)
            val publicacion = publisherService.selectByID(idPublicacion,newUser.id!!)
            val transaccion = publisherService.confirm(newUser,publicacion)

            val newTransaction = dtoService.transaccionToTransaccionViewMapper(transaccion)

            ResponseEntity.status(200)
            response = ResponseEntity.ok().body(newTransaction)
        } catch (e: Exception) {
            ResponseEntity.status(404)
            val resultado: MutableMap<String, String> = HashMap()
            resultado["Error"] = e.message.toString()
            response = ResponseEntity.ok().body<Map<String, String>>(resultado)
        }
        return response !!
    }



}