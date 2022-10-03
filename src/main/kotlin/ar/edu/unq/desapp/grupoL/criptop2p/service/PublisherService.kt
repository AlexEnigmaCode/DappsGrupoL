package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.*
import ar.edu.unq.desapp.grupoL.criptop2p.model.Publicacion
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.PublicacionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class PublisherService {
    val publicaciones = listOf<Publicacion>()

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private  lateinit var publisherRepository: PublicacionRepository


    @Transactional
    fun publicar(id:Int, intencion: IntencionRegisterMapper): Publicacion {
        try {
            val user = userService.findByID(id)
            val diahora = LocalDate.now().toString()
            val cantidadoperaciones = user.icrementarOperqaciones()
            val usuario = " ${user.name}" + " " + "${user.surname}"
            val monto =  intencion.cantidad!! * intencion.cotizacion!!
            val reputacion = user.reputacion.toString()
            val publicacion = PublicacionRegisterMapper(
                diahora,
                intencion.criptoactivo,
                intencion.cantidad,
                intencion.cotizacion,
               monto,
               usuario,
               cantidadoperaciones,
               reputacion
            )
            return publisherRepository.save(publicacion)
        } catch (e: Exception) {
            throw ItemNotFoundException("User with Id:  $id not found")
        }
    }


        @Transactional
        fun findAll(): List<Publicacion> {
            val publicaciones =  publisherRepository.findAll()
            return  publicaciones
        }

    }

