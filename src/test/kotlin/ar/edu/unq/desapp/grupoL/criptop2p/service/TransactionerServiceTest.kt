package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.*
import ar.edu.unq.desapp.grupoL.criptop2p.model.Publicacion
import ar.edu.unq.desapp.grupoL.criptop2p.model.Transaccion
import ar.edu.unq.desapp.grupoL.criptop2p.model.Usuario
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.TransaccionRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime


@SpringBootTest
internal class TransactionerServiceTest {

    @Autowired
    lateinit var   userService: UserService
    @Autowired
    lateinit var   transactionerService: TransactionerService
    @Autowired
    lateinit var  repository: TransaccionRepository

    var wallets = mutableListOf<VirtualWallet>()
    var  transacciones = mutableListOf<Transaccion>()
    var criptoactivos = mutableListOf <CriptoActivoWalletMapper>()
    lateinit var   publicacionCompra : Publicacion
    lateinit var   publicacionVenta : Publicacion
    lateinit var  transaccionDeConpra : Transaccion
    lateinit var  transaccionDeVenta : Transaccion
    lateinit var usuarioComprador: UserRegisterMapper
    lateinit var usuarioVendedor: UserRegisterMapper
    lateinit var usuarioPublicacionCompra: Usuario
    lateinit var usuarioPublicacionVenta: Usuario

    lateinit var criptoActivo1 : CriptoActivoWalletMapper
    lateinit var criptoActivo2 : CriptoActivoWalletMapper
    lateinit var criptoActivo3 : CriptoActivoWalletMapper

    var cotizacionActual = 10.0


    @BeforeEach
    fun setUp() {
       usuarioPublicacionCompra = Usuario (1, "Ale", "Fariña", "ale@gmail.com", "address1","1", "123", "7" ,0,0.0 )
        usuarioPublicacionVenta = Usuario (2, "Ulises", "Lopez","ulises@gmail.com", "address2","2", "234", "8" ,0, 5.0 )

        usuarioComprador = UserRegisterMapper( "Ale", "Fariña", "ale@gmail.com", "address1","1", "123", "7"  )
       usuarioVendedor = UserRegisterMapper( "Ulises", "Lopez","ulisese@gmail.com", "address2","2", "234", "8" )

        publicacionCompra= Publicacion (1, LocalDateTime.now(), "A",5, 9.6,48.0,  usuarioPublicacionCompra, "compra")
        publicacionVenta = Publicacion (2,LocalDateTime.now(), "B",7, 10.2,71.4,        usuarioPublicacionVenta, "venta")

        criptoActivo1 = CriptoActivoWalletMapper("cripto1", 10.0, 5,50.0)
        criptoActivo2 = CriptoActivoWalletMapper( "cripto2", 10.0, 5,50.0)
        criptoActivo3 =  CriptoActivoWalletMapper("cripto3", 10.0, 5,50.0)

        criptoactivos.add(criptoActivo1)
        criptoactivos.add(criptoActivo2)
        criptoactivos.add(criptoActivo3)

     val virtualWallet1 = VirtualWallet  (  usuarioPublicacionCompra ,criptoactivos )
     val virtualWallet2 = VirtualWallet  (usuarioPublicacionVenta ,criptoactivos )
      wallets.add(virtualWallet1)
      wallets.add(virtualWallet2)
        transaccionDeConpra = Transaccion(
          0,
          LocalDateTime.now(),
            publicacionCompra.criptoactivo,
            publicacionCompra.cantidad,
            publicacionCompra.cotizacion,
            publicacionCompra.monto,
            publicacionCompra.usuario,
            publicacionCompra.operacion,
          0,
            publicacionCompra.usuario!!.reputacion!!,
            publicacionCompra.usuario!!.walletAddress,
            Accion.REALIZAR_TRANSFERENCIA,
            usuarioPublicacionVenta)

    }


    @Test
    fun procesarTransaccion() {
        val comprador = userService.register(usuarioComprador)
        val transaccion  =  transactionerService.procesarTransaccion(comprador.id!!,publicacionVenta, cotizacionActual )
        assertEquals( publicacionVenta.criptoactivo, transaccion.criptoactivo)
        assertEquals(  publicacionVenta.cantidad, transaccion.cantidad)
        assertEquals(  publicacionVenta.cotizacion, transaccion.cotizacion,)
        assertEquals(  publicacionVenta.monto,transaccion.monto)

        assertEquals( usuarioPublicacionVenta.id,transaccion.usuario!!.id)
        assertEquals( usuarioPublicacionVenta.name, transaccion.usuario!!.name)

        assertEquals( comprador.id,transaccion.usuarioSelector!!.id)
        assertEquals( comprador.name, transaccion.usuarioSelector!!.name)

    }

    @Test
    fun transacciones() {
        val comprador = userService.register(usuarioComprador)
        val vendedor = userService.register(usuarioVendedor)

        val transaccion1 = transactionerService.procesarTransaccion(comprador.id!!,publicacionVenta, cotizacionActual )
        val transaccion2 = transactionerService.procesarTransaccion(vendedor.id!!,publicacionCompra, cotizacionActual )


        val transacciones =   transactionerService.transacciones()
        assertTrue { transacciones.isNotEmpty() }
        assertEquals (2, transacciones.size)

        assertEquals( publicacionVenta.criptoactivo, transaccion1.criptoactivo)
        assertEquals(  publicacionVenta.cantidad,transaccion1.cantidad)
        assertEquals(  publicacionVenta.cotizacion, transaccion1.cotizacion,)
        assertEquals(  publicacionVenta.monto,transaccion1.monto)

        assertEquals( usuarioPublicacionVenta.id,transaccion1.usuario!!.id)
        assertEquals( usuarioPublicacionVenta.name, transaccion1.usuario!!.name)

        assertEquals( comprador.id,transaccion1.usuarioSelector!!.id)
        assertEquals( comprador.name, transaccion1.usuarioSelector!!.name)


        assertEquals( publicacionCompra.criptoactivo, transaccion2.criptoactivo)
        assertEquals(  publicacionCompra.cantidad,transaccion2.cantidad)
        assertEquals( publicacionCompra.cotizacion, transaccion2.cotizacion,)
        assertEquals(  publicacionCompra.monto,transaccion2.monto)

        assertEquals(  usuarioPublicacionCompra.id,transaccion2.usuario!!.id)
        assertEquals(  usuarioPublicacionCompra.name, transaccion2.usuario!!.name)

        assertEquals( vendedor.id,transaccion2.usuarioSelector!!.id)
        assertEquals( vendedor.name, transaccion2.usuarioSelector!!.name)

    }

    @Test
    fun Al_Finalizar_Una_Transaccion_Ya_No_estara_Incluida_Em_La_Base_De_Daytos_De_Transacciones() {
        val comprador = userService.register(usuarioComprador)
        val vendedor = userService.register(usuarioVendedor)

        val transaccion1 = transactionerService.procesarTransaccion(comprador.id!!,publicacionVenta, cotizacionActual )
        val transaccion2 = transactionerService.procesarTransaccion(vendedor.id!!,publicacionCompra, cotizacionActual )

        val transacciones =   transactionerService.transacciones()
        transactionerService.finalizarTransaccion(transaccion1)
        assertFalse (transacciones.contains(transaccion1) )
    }



    @AfterEach
    fun tearDown() {
        repository.deleteAll()
    }

}
