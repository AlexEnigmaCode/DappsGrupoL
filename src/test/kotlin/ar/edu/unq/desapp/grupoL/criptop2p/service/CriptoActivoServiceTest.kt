package ar.edu.unq.desapp.grupoL.criptop2p.service
/*
import ar.edu.unq.desapp.grupoL.criptop2p.Binance
import ar.edu.unq.desapp.grupoL.criptop2p.CriptoActivoRegisterMapper
import ar.edu.unq.desapp.grupoL.criptop2p.model.CriptoActivo
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.CriptoActivoRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class CriptoActivoServiceTest {

    @Autowired
    lateinit var  criptoactivoService: CriptoActivoService

    @Autowired
    lateinit var  repository: CriptoActivoRepository


    var  binances = mutableListOf<Binance>()
    lateinit var   binance1 : Binance
    lateinit var   binance2 : Binance
    lateinit var symbol : String

    @BeforeEach
    fun setUp() {

        binance1 = Binance("A", "1")
        binance2 = Binance("B", "2")
        binances.add( binance1 )
        binances.add( binance2 )

    }

    @Test
    fun save() {
        val cripto  =  criptoactivoService.save(binance1)

        assertEquals (binance1.symbol, cripto.criptoactivo,)
        assertEquals (binance1.price, cripto.cotizacion)


    }


    @Test
    fun saveAll() {
       val criptos  =  criptoactivoService.saveAll(binances)
        assertEquals (binances.size, criptos.size)

        assertEquals (binances.get(0).symbol,  criptos.get(0).criptoactivo)
        assertEquals (binances.get(0).price, criptos.get(0).cotizacion )


        assertEquals (binances.get(1).symbol,criptos.get(1).criptoactivo)
        assertEquals (binances.get(1).price,criptos.get(1).cotizacion)


    }



    @Test
    fun findAll() {
        val criptos  =  criptoactivoService.saveAll(binances)
        val criptoActivos  =  criptoactivoService.findAll().toMutableList()

        assertTrue { criptoActivos.isNotEmpty() }
        assertEquals (criptos.size, criptoActivos.size)

        assertEquals (criptos.get(0).criptoactivo,criptoActivos.get(0).criptoactivo)
        assertEquals (criptos.get(0).cotizacion,criptoActivos.get(0).cotizacion)


        assertEquals (criptos.get(1).criptoactivo,criptoActivos.get(1).criptoactivo)
        assertEquals (criptos.get(1).cotizacion,criptoActivos.get(1).cotizacion)

    }



    @Test
    fun findByCriptoActivo() {
         criptoactivoService.saveAll(binances)
        val criptoActivo = criptoactivoService.findByCriptoActivo(binance1.symbol!!)
        assertEquals (   binance1.symbol, criptoActivo.criptoactivo)
        assertEquals (   binance1.price, criptoActivo.cotizacion)



    }




    @AfterEach
    fun tearDown() {
        repository.deleteAll()
    }

} */
