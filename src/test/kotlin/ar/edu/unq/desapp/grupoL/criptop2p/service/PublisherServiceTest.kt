package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.*
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.PublicacionRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class PublisherServiceTest {

    @Autowired
    lateinit var   userService: UserService
    @Autowired
    lateinit var   publisherService: PublisherService
    @Autowired
    lateinit var  repository: PublicacionRepository


    lateinit var user1: UserRegisterMapper
    lateinit var user2: UserRegisterMapper

    var  publicaciones = mutableListOf<IntencionRegisterMapper>()
    lateinit var   publicacion1 : IntencionRegisterMapper
    lateinit var   publicacion2 : IntencionRegisterMapper

    @BeforeEach
    fun setUp() {

        user1 = UserRegisterMapper( "Ale", "Fariña", "ale@gmail.com", "address1","1", "123", "7"  )

        user2 = UserRegisterMapper( "Ulises", "Lopez","ulisese@gmail.com", "address2","2", "234", "8" )

      publicacion1= IntencionRegisterMapper( "A",5, 6.0,30.0, "Ale", "compra")
      publicacion2= IntencionRegisterMapper ( "B",7, 10.0,70.0, "Uli", "venta")

    }



    @Test
    fun publicar() {
       val newUser1 = userService.register(user1)
       val publi1  =  publisherService.publicar(newUser1.id!!,publicacion1)
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
         publisherService.publicar(newUser1.id!!,publicacion1)
           publisherService.publicar(newUser2.id!!,publicacion2)

        val publicaciones =  publisherService.findAll().toMutableList()
        assertTrue { publicaciones.isNotEmpty() }
        assertEquals (2, publicaciones.size)

        assertEquals( publicacion1.criptoactivo, publicaciones.get(0).criptoactivo)
        assertEquals( publicacion1.cantidad, publicaciones.get(0).cantidad)
        assertEquals( publicacion1.cotizacion, publicaciones.get(0).cantidad)
        assertEquals( publicacion1.monto, publicaciones.get(0).monto)
        assertEquals( newUser1.id!!, publicaciones.get(0).usuario!!.id)
        assertEquals( "Ale", publicaciones.get(0).usuario!!.name)
        assertEquals( "Fariña", publicaciones.get(0).usuario!!.surname)



        assertEquals( publicacion1.criptoactivo, publicaciones.get(1).criptoactivo)
        assertEquals( publicacion1.cantidad, publicaciones.get(1).cantidad)
        assertEquals( publicacion1.cotizacion, publicaciones.get(1).cantidad)
        assertEquals( publicacion1.monto, publicaciones.get(1).monto)
        assertEquals( newUser2.id!!, publicaciones.get(1).usuario!!.id)
        assertEquals( "Ulises", publicaciones.get(1).usuario!!.name)
        assertEquals( "Lopez", publicaciones.get(1).usuario!!.surname)

    }




    @AfterEach
    fun tearDown() {
        repository.deleteAll()
    }

}
