package ar.edu.unq.desapp.grupoL.criptop2p.webservice.controller


import ar.edu.unq.desapp.grupoL.criptop2p.LogExecutionTimeAuditory
import ar.edu.unq.desapp.grupoL.criptop2p.service.InformerService
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@EnableAutoConfiguration
@CrossOrigin("*")
class InformerRestService {

    @Autowired
    private  lateinit var  informerService: InformerService
    private val builder: ResponseEntity.BodyBuilder? = null

    var logger: Logger = LoggerFactory.getLogger(LogExecutionTimeAuditory::class.java)

    @GetMapping("/api/informes/users")
    fun listadoUsuariosDePlataforma(): ResponseEntity<*> {
        val users = informerService.listadoUsuariosDePlataforma()
        return ResponseEntity.ok().body(users)
    }

}