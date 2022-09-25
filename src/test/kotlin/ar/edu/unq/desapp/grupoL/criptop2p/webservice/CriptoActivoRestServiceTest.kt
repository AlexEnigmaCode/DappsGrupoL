package ar.edu.unq.desapp.grupoL.criptop2p.webservice


import ar.edu.unq.desapp.grupoL.criptop2p.model.CriptoActivo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate

@SpringBootTest
internal class CriptoActivoRestServiceTest {
    @Autowired
    lateinit var   criptoActivoRestService: CriptoActivoRestService


    var  criptoActivos =listOf<CriptoActivo>()
    lateinit var   criptoActivo : CriptoActivo
    lateinit var fecha :LocalDate

    @BeforeEach
    fun setUp() {
          fecha = LocalDate.now()
    }

    @Test
    fun allCriptoActivos() {
        criptoActivos  = criptoActivoRestService.allCriptoActivos()
        System.out.println( "cantidad de criptoActivos = ${criptoActivos.size} ")
        assertTrue { criptoActivos.isNotEmpty() }
        assertEquals ( 57,criptoActivos.size)
        assertEquals ("ETHBTC",criptoActivos.get(0).symbol)
        assertEquals ("0.06980400",criptoActivos.get(0).price)
    // assertEquals (fecha,criptoActivos.get(0).fecha )

    }

    /*
    @Test
    fun criptoActivoByName() {
        criptoActivo = criptoActivoRestService.criptoActivoByName("BNBUSDT").body!!
       assertEquals ("BNBUSDT",criptoActivo.symbol)
        assertEquals ("269.00000000",criptoActivo.price )
       // System.out.println( "fecha esperada = $fecha ")
       // System.out.println( "fecha actual = ${criptoActivo.fecha} ")
       // assertEquals (fecha,criptoActivo.fecha )

    }
*/


    @AfterEach
    fun tearDown() {

    }
}