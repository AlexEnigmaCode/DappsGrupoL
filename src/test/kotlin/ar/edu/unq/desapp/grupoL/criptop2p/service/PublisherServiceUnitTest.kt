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
internal class PublisherServiceUnitTest {

    @Autowired
    lateinit var   userService: UserService
    @Autowired
    lateinit var   publisherService: PublisherService
    @Autowired
    lateinit var  repository: PublicacionRepository


    lateinit var user1: UserRegisterMapper
    lateinit var user2: UserRegisterMapper

    var  publicaciones = mutableListOf<IntencionRegisterMapper>()
    lateinit var    publicacionPorDebajo: IntencionRegisterMapper
    lateinit var    publicacionPorEncima : IntencionRegisterMapper
    lateinit var     publicacionDentro : IntencionRegisterMapper
    var cotizacionActual = 10.0


    @BeforeEach
    fun setUp() {

        user1 = UserRegisterMapper( "Ale", "Fariña", "ale@gmail.com", "address1","1", "123", "7"  )

        user2 = UserRegisterMapper( "Ulises", "Lopez","ulisese@gmail.com", "address2","2", "234", "8" )

      publicacionPorDebajo= IntencionRegisterMapper( "A",5, 6.0,30.0, "Ale", "compra")
      publicacionPorEncima= IntencionRegisterMapper ( "B",7, 11.0,77.0, "Uli", "venta")
      publicacionDentro = IntencionRegisterMapper( "A",5, 10.2,51.0, "Ale", "compra")
    }

    @Test
    fun Si_EL_Precio_Del_CriptoActivo_Esta_En_El_Rango_De_Referencia_Por_CotizacionActual_Entonces_Puede_Publicar(){
        val  puedePublicar = publisherService.puedePublicarSegunCotizacionActual( publicacionDentro, cotizacionActual )
        assertTrue( puedePublicar)

    }


    @Test
    fun Si_EL_Precio_Del_CriptoActivo_Esta_Por_Debajo_DelRango_De_Referencia_Por_CotizacionActual_Entonces_No_Puede_Publicar(){
        val  puedePublicar = publisherService.puedePublicarSegunCotizacionActual( publicacionPorEncima, cotizacionActual )
        assertTrue( puedePublicar)

    }

    @Test
    fun Si_EL_Precio_Del_CriptoActivo_Esta_Por_Encima_DelRango_De_Referencia_Por_CotizacionActual_Entonces_No_Puede_Publicar(){
        val  puedePublicar = publisherService.puedePublicarSegunCotizacionActual( publicacionDentro, cotizacionActual )
        assertTrue( puedePublicar)

    }



    @AfterEach
    fun tearDown() {
        repository.deleteAll()
    }

}
