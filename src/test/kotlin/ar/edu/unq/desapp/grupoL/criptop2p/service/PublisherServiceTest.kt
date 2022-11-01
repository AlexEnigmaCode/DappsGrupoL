package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.*
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.PublicacionRepository
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.UserRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.math.roundToLong

@SpringBootTest
internal class PublisherServiceTest {

    @Autowired
    lateinit var   userService: UserService
    @Autowired
    lateinit var   publisherService: PublisherService
    @Autowired
    lateinit var  repository: PublicacionRepository

    @Autowired
    lateinit var  userRepository: UserRepository


    lateinit var user1: UserRegisterMapper
    lateinit var user2: UserRegisterMapper

    var  publicaciones = mutableListOf<IntencionRegisterMapper>()
    lateinit var   publicacion1 : IntencionRegisterMapper
    lateinit var   publicacion2 : IntencionRegisterMapper
     var cotizacionActual = 10.0


    @BeforeEach
    fun setUp() {

        user1 = UserRegisterMapper( "Ale", "Fariña", "ale@gmail.com", "address1","1", "123", "7"  )

        user2 = UserRegisterMapper( "Ulises", "Lopez","ulisese@gmail.com", "address2","2", "234", "8" )

      publicacion1= IntencionRegisterMapper( "A",5, 9.6,48.0, "Ale", "compra")
      publicacion2= IntencionRegisterMapper ( "B",7, 10.2,71.4, "Uli", "venta")

    }



    @Test
    fun publicar() {
       val newUser1 = userService.register(user1)
       val publi1  =  publisherService.publicar(newUser1.id!!,publicacion1, cotizacionActual )
       assertEquals( publicacion1.criptoactivo, publi1.criptoactivo)
        assertEquals( publicacion1.cantidad, publi1.cantidad)
        assertEquals( publicacion1.cotizacion, publi1.cotizacion,)
        assertEquals( publicacion1.monto,publi1.monto)
        assertEquals( newUser1.id!!,publi1.usuario!!.id!!)
        assertEquals( "Ale",publi1.usuario!!.name)
        assertEquals( "Fariña",publi1.usuario!!.surname)



    }

    @Test
    fun findAll() {
        val newUser1 = userService.register(user1)
        val newUser2 = userService.register(user2)
         publisherService.publicar(newUser1.id!!,publicacion1,cotizacionActual)
         publisherService.publicar(newUser2.id!!,publicacion2,cotizacionActual)

        val publicaciones =  publisherService.findAll().toMutableList().sortedBy { it.criptoactivo }
        assertTrue { publicaciones.isNotEmpty() }
        assertEquals (2, publicaciones.size)

        assertEquals( publicacion1.criptoactivo, publicaciones.get(0).criptoactivo)
        assertEquals( publicacion1.cantidad, publicaciones.get(0).cantidad)
        assertEquals( publicacion1.cotizacion, publicaciones.get(0).cotizacion)
        assertEquals( publicacion1.monto.roundToLong(), publicaciones.get(0).monto.roundToLong())
        assertEquals( newUser1.id!!, publicaciones.get(0).usuario!!.id)
        assertEquals( "Ale", publicaciones.get(0).usuario!!.name)
        assertEquals( "Fariña", publicaciones.get(0).usuario!!.surname)



        assertEquals( publicacion2.criptoactivo, publicaciones.get(1).criptoactivo)
        assertEquals( publicacion2.cantidad, publicaciones.get(1).cantidad)
        assertEquals( publicacion2.cotizacion, publicaciones.get(1).cotizacion)
        assertEquals( publicacion2.monto.roundToLong(), publicaciones.get(1).monto.roundToLong())
        assertEquals( newUser2.id!!, publicaciones.get(1).usuario!!.id)
        assertEquals( "Ulises", publicaciones.get(1).usuario!!.name)
        assertEquals( "Lopez", publicaciones.get(1).usuario!!.surname)

    }




    @AfterEach
    fun tearDown() {
        repository.deleteAll()
        userRepository.deleteAll()
    }

}
