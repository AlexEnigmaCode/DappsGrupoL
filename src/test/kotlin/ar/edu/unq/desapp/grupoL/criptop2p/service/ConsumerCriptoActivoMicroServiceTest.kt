package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.model.CriptoActivo
import ar.edu.unq.desapp.grupoL.criptop2p.webservice.CriptoActivoRestService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

internal class ConsumerCriptoActivoMicroServiceTest {

    @Autowired
    lateinit var  consumerService: ConsumerCriptoActivoMicroService


    var  criptoActivos =listOf<CriptoActivo>()
    lateinit var   criptoActivo : CriptoActivo
    lateinit var fecha : LocalDate

    @BeforeEach
    fun setUp() {
        fecha = LocalDate.now()
    }

    @Test
    fun consumeAllCriptoActivos() {
        criptoActivos  = consumerService.consumeCriptoActivos()
        System.out.println( "cantidad de criptoActivos = ${criptoActivos.size} ")
        assertTrue { criptoActivos.isNotEmpty() }
        assertEquals ( 57,criptoActivos.size)
        assertEquals ("ETHBTC",criptoActivos.get(0).symbol)
        assertEquals ("0.06980400",criptoActivos.get(0).price)
        // assertEquals (fecha,criptoActivos.get(0).fecha )

    }

    @AfterEach
    fun tearDown() {
    }
}