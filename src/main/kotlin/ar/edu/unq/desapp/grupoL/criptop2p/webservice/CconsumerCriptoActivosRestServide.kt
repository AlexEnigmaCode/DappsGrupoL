package ar.edu.unq.desapp.grupoL.criptop2p.webservice

import ar.edu.unq.desapp.grupoL.criptop2p.model.CriptoActivo
import ar.edu.unq.desapp.grupoL.criptop2p.service.ConsumerCriptoActivoMicroService
import ar.edu.unq.desapp.grupoL.criptop2p.service.CriptoActivoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@EnableAutoConfiguration
@CrossOrigin("*")
class CconsumerCriptoActivosRestServide {

    @Autowired
    private val consumerService: ConsumerCriptoActivoMicroService? = null
    private val builder: ResponseEntity.BodyBuilder? = null
    private var criptoActivos = listOf<CriptoActivo>()

    @GetMapping("/api/consumecriptoactivos")
    fun consumeAllCriptoActivos(): List<CriptoActivo> {
        val list = consumerService?.consumeCriptoActivos()
        if (list != null) {

            criptoActivos =  list.map { CriptoActivo(it.symbol, it.price) }
        }
        return criptoActivos

    }



}