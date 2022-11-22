package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.CriptoActivoRegisterMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
internal class ConsumerCriptoActivoMicroServiceTest {

    @Autowired
    lateinit var  consumerService: ConsumerCriptoActivoMicroService


    var  criptoActivos =listOf<CriptoActivoRegisterMapper>()
    lateinit var symbol : String

    @BeforeEach
    fun setUp() {
        symbol="BNBUSDT"
    }

    @Test
    fun consumeAllCriptoActivos() {
        criptoActivos  = consumerService.consumeCriptoActivos()
        // assertEquals ("ETHBTC",criptoActivos.get(0).criptoActivo)
        assertTrue { criptoActivos.isNotEmpty() }
    }

    @Test
    fun consumeCriptoActivoBySymbol(){
        val  criptoActivo: CriptoActivoRegisterMapper = consumerService.consumeBySymbol(symbol)
        assertEquals (symbol,criptoActivo.criptoActivo)
    }


    @AfterEach
    fun tearDown() {
    }
}