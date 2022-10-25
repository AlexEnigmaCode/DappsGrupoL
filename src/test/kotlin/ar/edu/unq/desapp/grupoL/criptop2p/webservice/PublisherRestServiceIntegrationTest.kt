package ar.edu.unq.desapp.grupoL.criptop2p.webservice


import ar.edu.unq.desapp.grupoL.criptop2p.UserRegisterMapper
import ar.edu.unq.desapp.grupoL.criptop2p.model.Publicacion
import ar.edu.unq.desapp.grupoL.criptop2p.model.Usuario
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.PublicacionRepository
import ar.edu.unq.desapp.grupoL.criptop2p.service.PublisherService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

@SpringBootTest
internal class PublisherRestServiceIntegrationTest {



    var   publisherService = PublisherService()

    lateinit var user1: UserRegisterMapper
    lateinit var user2: UserRegisterMapper
    lateinit var usuarioPublicacionCompra: Usuario
    lateinit var usuarioPublicacionVenta: Usuario
    var  publicacionesMock = mutableListOf<Publicacion>()
    lateinit var   publicacionCompra : Publicacion
    lateinit var   publicacionVenta : Publicacion

    var cotizacionActual = 10.0

    var  publicacionRepositoryMock: PublicacionRepository = Mockito.mock(PublicacionRepository::class.java)

    @BeforeEach
    fun setUp() {

       usuarioPublicacionCompra = Usuario (1, "Ale", "Fariña", "ale@gmail.com", "address1","1", "123", "7" ,0,0.0 )
       usuarioPublicacionVenta = Usuario(2, "Ulises", "Lopez","ulises@gmail.com", "address2","2", "234", "8" ,0, 5.0 )


        publicacionCompra= Publicacion (1, LocalDateTime.now(), "A",5, 9.6,48.0,  usuarioPublicacionCompra, "compra")
        publicacionVenta = Publicacion (2, LocalDateTime.now(), "B",7, 10.2,71.4,        usuarioPublicacionVenta, "venta")


        publicacionesMock.add( publicacionCompra)
        publicacionesMock.add(publicacionVenta)

        Mockito.`when`( publicacionRepositoryMock.findAll()).thenReturn(publicacionesMock)

    }



    @Test
    fun findAll() {

        val publicaciones =  publisherService.findAll().toMutableList()
        Assertions.assertTrue { publicaciones.isNotEmpty() }
        Assertions.assertEquals(2, publicaciones.size)

        Assertions.assertEquals( publicacionesMock.get(0).criptoactivo, publicaciones.get(0).criptoactivo)
        Assertions.assertEquals(publicacionesMock.get(0).cantidad, publicaciones.get(0).cantidad)
        Assertions.assertEquals(publicacionesMock.get(0).cotizacion, publicaciones.get(0).cantidad)
        Assertions.assertEquals(publicacionesMock.get(0).monto, publicaciones.get(0).monto)
        Assertions.assertEquals(usuarioPublicacionCompra.id!!, publicaciones.get(0).usuario!!.id)
        Assertions.assertEquals(usuarioPublicacionCompra.name, publicaciones.get(0).usuario!!.name)
        Assertions.assertEquals(usuarioPublicacionCompra.surname, publicaciones.get(0).usuario!!.surname)



        Assertions.assertEquals( publicacionesMock.get(1).criptoactivo, publicaciones.get(1).criptoactivo)
        Assertions.assertEquals(publicacionesMock.get(1).cantidad, publicaciones.get(1).cantidad)
        Assertions.assertEquals(publicacionesMock.get(1).cotizacion, publicaciones.get(1).cantidad)
        Assertions.assertEquals(publicacionesMock.get(1).monto, publicaciones.get(1).monto)
        Assertions.assertEquals(  usuarioPublicacionVenta .id!!, publicaciones.get(1).usuario!!.id)
        Assertions.assertEquals(  usuarioPublicacionVenta .name, publicaciones.get(1).usuario!!.name)
        Assertions.assertEquals(  usuarioPublicacionVenta .surname, publicaciones.get(1).usuario!!.surname)

    }




    @AfterEach
    fun tearDown() {

    }

}

