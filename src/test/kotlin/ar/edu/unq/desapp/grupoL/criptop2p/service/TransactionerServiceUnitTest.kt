package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.UserRegisterMapper
import ar.edu.unq.desapp.grupoL.criptop2p.model.Publicacion
import ar.edu.unq.desapp.grupoL.criptop2p.model.Transaccion
import ar.edu.unq.desapp.grupoL.criptop2p.model.Usuario
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.TransaccionRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime


@SpringBootTest
class TransactionerServiceUnitTest {

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var transactionerService: TransactionerService

    @Autowired
    lateinit var repository: TransaccionRepository


    var transacciones = mutableListOf<Transaccion>()
    lateinit var publicacionCompra: Publicacion
    lateinit var publicacionVenta: Publicacion
    lateinit var publicacionCompra2: Publicacion
    lateinit var publicacionVenta2: Publicacion
    lateinit var usuarioComprador: UserRegisterMapper
    lateinit var usuarioVendedor: UserRegisterMapper
    lateinit var usuarioPublicacionCompra: Usuario
    lateinit var usuarioPublicacionVenta: Usuario
    var cotizacionActual = 10.0


    @BeforeEach
    fun setUp() {
        usuarioPublicacionCompra = Usuario(1, "Ale", "Fariña", "ale@gmail.com", "address1", "1", "123", "7", 0, 0.0)
        usuarioPublicacionVenta = Usuario(2, "Ulises", "Lopez", "ulises@gmail.com", "address2", "2", "234", "8", 0, 5.0)

        usuarioComprador = UserRegisterMapper("Ale", "Fariña", "ale@gmail.com", "address1", "1", "123", "7")
        usuarioVendedor = UserRegisterMapper("Ulises", "Lopez", "ulisese@gmail.com", "address2", "2", "234", "8")

        publicacionCompra = Publicacion(1, LocalDateTime.now(), "A", 5, 9.6, 48.0, usuarioPublicacionCompra, "compra")
        publicacionVenta = Publicacion(2, LocalDateTime.now(), "B", 7, 10.2, 71.4, usuarioPublicacionVenta, "venta")

        publicacionCompra2 = Publicacion(1, LocalDateTime.now(), "A", 7, 10.2, 71.4, usuarioPublicacionCompra, "compra")
        publicacionVenta2 = Publicacion(2, LocalDateTime.now(), "B", 5, 9.6, 48.0, usuarioPublicacionVenta, "venta")
    }

    @Test
    fun Si_La_Publicacion_De_Compra_Tiene_Precio_Menor_A_CotizacionActual_Se_Cancela() {
       val fueCancelada =  transactionerService.isCanceled(publicacionCompra,cotizacionActual)
      assertTrue( fueCancelada )
}

    @Test
    fun Si_La_Publicacion_De_Venta_Tiene_Precio_Mayor_A_CotizacionActual_Se_Cancela() {
        val fueCancelada = transactionerService.isCanceled(publicacionVenta, cotizacionActual)
        assertTrue(fueCancelada)
    }

    @Test
    fun Si_La_Publicacion_De_Compra_Tiene_Precio_Mayor_A_CotizacionActual_No_Se_Cancela() {
            val fueCancelada =  transactionerService.isCanceled(publicacionCompra,cotizacionActual)
            assertFalse( fueCancelada )
        }


    @Test
    fun Si_La_Publicacion_De_Venta_Tiene_Precio_Menor_A_CotizacionActual_No_Se_Cancela(){
       val fueCancelada = transactionerService.isCanceled(publicacionVenta, cotizacionActual)
        assertFalse( fueCancelada )
    }

    @Test
    fun Si_La_Transaccion_Fue_CanceladaPorEl_Usuario_Se_Le_Descuenta_20_De_Reputacion(){
        var comprador = userService.register(usuarioComprador)
        comprador.reputacion = 100.0
        val transaccion = transactionerService.cancelar(usuarioPublicacionCompra.id!! , publicacionVenta )
        assertEquals(20, transaccion.usuarioSelector!!.reputacion)
    }

    @Test
    fun Si_La_Transaccion_Fue_CanceladaPorEl_Usuario_No_se_Guuarda_En_La_BaseDatos_De_Transacciones(){
        val comprador = userService.register(usuarioComprador)
        transactionerService.cancelar(comprador.id!! , publicacionVenta )
        val transacciones = transactionerService.transacciones()
        assertTrue( transacciones.isEmpty())
    }


@AfterEach
fun tearDown() {
    repository.deleteAll()
}

}