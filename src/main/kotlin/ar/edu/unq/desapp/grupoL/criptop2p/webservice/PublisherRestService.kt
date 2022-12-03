package ar.edu.unq.desapp.grupoL.criptop2p.webservice


import ar.edu.unq.desapp.grupoL.criptop2p.IntencionRegisterMapper
import ar.edu.unq.desapp.grupoL.criptop2p.PublicacionViewMapper
import ar.edu.unq.desapp.grupoL.criptop2p.UserUpdateMapper
import ar.edu.unq.desapp.grupoL.criptop2p.UserViewMapper
import ar.edu.unq.desapp.grupoL.criptop2p.model.Publicacion
import ar.edu.unq.desapp.grupoL.criptop2p.model.Usuario
import ar.edu.unq.desapp.grupoL.criptop2p.service.ConsumerCriptoActivoMicroService
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


    /** Publish an intention for a user*/
    @PostMapping("/api/publicaciones/{id}")
    fun publicar (@PathVariable("id") id: Long, @RequestBody entity: IntencionRegisterMapper): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        try {

            val  criptoActivo = consumer.consumeBySymbol(entity.criptoactivo!!)
            val cotizacion =  criptoActivo.cotizacion!!.toDouble()
            val publicacion = publisherService.publicar(id,entity,cotizacion)
            val publicacionDTO = publicacionToPublicacionViewMapper(publicacion)

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

     fun   usuarioToUserViewMapper(usuario: Usuario): UserViewMapper {
    val userViewMapper =  UserViewMapper(
            usuario.id,
            usuario.name,
            usuario.surname,
            usuario.email,
            usuario.address,
            usuario.cvu,
            usuario.walletAddress,
            usuario.cantidadOperaciones,
            usuario.reputacion)
         return userViewMapper
}

    fun publicacionToPublicacionViewMapper(publicacion:Publicacion):PublicacionViewMapper {
        val  usuarioViewMapper = usuarioToUserViewMapper(publicacion.usuario!!)
        val publicacionDTO =  PublicacionViewMapper (
            publicacion.id!!,
            publicacion.diahora!!,
            publicacion.criptoactivo!!,
            publicacion.cantidad!!,
            publicacion.cotizacion,
            publicacion.monto,
            usuarioViewMapper,
            publicacion.operacion!!
        )
         return publicacionDTO
    }

    @GetMapping("api/publicaciones")
    fun listarPublicaciones(): ResponseEntity<*> {
        val publicaciones = publisherService.findAll()
        val publicacionesDto =  publicaciones.map{ publicacionToPublicacionViewMapper(it)}
        return ResponseEntity.ok().body(publicacionesDto)
    }


    /**get publication by id**/
    @GetMapping("/api/publicaciones/{id}/{idUsuario}")
    fun selectById(@PathVariable("id") id: Long,@PathVariable("idUsuario") idUsuario: Long): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        try {
            val publicacion = publisherService.selectByID(id,idUsuario)
            val publicacionDTO = publicacionToPublicacionViewMapper(publicacion)
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