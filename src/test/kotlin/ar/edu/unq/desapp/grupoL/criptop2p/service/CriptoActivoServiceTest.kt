package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.Binance
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


    var  criptoActivos = mutableListOf<CriptoActivo>()
    lateinit var   criptoActivo1 : CriptoActivo
    lateinit var   criptoActivo2 : CriptoActivo
    lateinit var symbol : String

    @BeforeEach
    fun setUp() {

        criptoActivo1 = CriptoActivo("A", "1" , "fecha1")
        criptoActivo2 = CriptoActivo("B", "2" , "fecha2")
        criptoActivos.add(criptoActivo1)
        criptoActivos.add(criptoActivo2)

    }

    @Test
    fun saveAll() {
       val criptos  =  criptoactivoService.saveAll(criptoActivos)
        assertEquals (criptos.size, criptoActivos.size)

        assertEquals (criptos.get(0).criptoactivo,criptoActivos.get(0).criptoactivo)
        assertEquals (criptos.get(0).cotizacion,criptoActivos.get(0).cotizacion)
        assertEquals (criptos.get(0).fecha,criptoActivos.get(0).fecha)

        assertEquals (criptos.get(1).criptoactivo,criptoActivos.get(1).criptoactivo)
        assertEquals (criptos.get(1).cotizacion,criptoActivos.get(1).cotizacion)
        assertEquals (criptos.get(1).fecha,criptoActivos.get(1).fecha)

    }



    @Test
    fun findAll() {
        val criptos  =  criptoactivoService.saveAll(criptoActivos)
        criptoActivos  =  criptoactivoService.findAll().toMutableList()
        System.out.println( "cantidad de criptoActivos = ${criptoActivos.size} ")
        assertTrue { criptoActivos.isNotEmpty() }
        assertEquals (criptos.size, criptoActivos.size)

        assertEquals (criptos.get(0).criptoactivo,criptoActivos.get(0).criptoactivo)
        assertEquals (criptos.get(0).cotizacion,criptoActivos.get(0).cotizacion)
        assertEquals (criptos.get(0).fecha,criptoActivos.get(0).fecha)

        assertEquals (criptos.get(1).criptoactivo,criptoActivos.get(1).criptoactivo)
        assertEquals (criptos.get(1).cotizacion,criptoActivos.get(1).cotizacion)
        assertEquals (criptos.get(1).fecha,criptoActivos.get(1).fecha)



    }



    @Test
    fun findByCriptoActivo() {
         criptoactivoService.saveAll(criptoActivos)
        val criptoActivo = criptoactivoService.findByCriptoActivo(criptoActivo1.criptoactivo!!)
        assertEquals (   criptoActivo1.criptoactivo,criptoActivo.criptoactivo)
        assertEquals (   criptoActivo1.cotizacion,criptoActivo.cotizacion)
        assertEquals (   criptoActivo1.fecha,criptoActivo.fecha)


    }




    @AfterEach
    fun tearDown() {
        repository.deleteAll()
    }

}