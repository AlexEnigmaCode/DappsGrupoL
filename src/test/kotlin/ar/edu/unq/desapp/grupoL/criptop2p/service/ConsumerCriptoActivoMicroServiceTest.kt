package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.Binance
import ar.edu.unq.desapp.grupoL.criptop2p.model.CriptoActivo
import ar.edu.unq.desapp.grupoL.criptop2p.webservice.CriptoActivoRestService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate

@SpringBootTest
internal class ConsumerCriptoActivoMicroServiceTest {

    @Autowired
    lateinit var  consumerService: ConsumerCriptoActivoMicroService


    var  criptoActivos =listOf<CriptoActivo>()
    lateinit var symbol : String

    @BeforeEach
    fun setUp() {
        symbol="BNBUSDT"
    }

    @Test
    fun consumeAllCriptoActivos() {
        criptoActivos  = consumerService.consumeCriptoActivos()
        System.out.println( "cantidad de criptoActivos = ${criptoActivos.size} ")
        assertEquals ("ETHBTC",criptoActivos.get(0).criptoactivo)
        assertTrue { criptoActivos.isNotEmpty() }
    }

    @Test
    fun consumeCriptoActivoBySymbol(){
        val  criptoActivo: CriptoActivo = consumerService.consumeBySymbol(symbol)
        System.out.println( "El cripto Activo es = $criptoActivo ")
        assertEquals (symbol,criptoActivo.criptoactivo)
    }


    @AfterEach
    fun tearDown() {
    }
}