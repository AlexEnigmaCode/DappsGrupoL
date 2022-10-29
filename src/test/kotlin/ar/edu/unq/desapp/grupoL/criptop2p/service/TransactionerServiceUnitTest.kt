package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.*
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
    lateinit var mercadoPagoService: MercadoPagoService

    @Autowired
    lateinit var transactionerService: TransactionerService

    @Autowired
    lateinit var repository: TransaccionRepository

    var wallets = mutableListOf<VirtualWallet>()
    var criptoactivos = mutableListOf <CriptoActivoWalletMapper>()
    var transacciones = mutableListOf<Transaccion>()
    lateinit var publicacionCompra: Publicacion
    lateinit var publicacionVenta: Publicacion
    lateinit var publicacionCompra2: Publicacion
    lateinit var publicacionVenta2: Publicacion
    lateinit var usuarioComprador: UserRegisterMapper
    lateinit var usuarioVendedor: UserRegisterMapper
    lateinit var usuarioPublicacionCompra: Usuario
    lateinit var usuarioPublicacionVenta: Usuario
    lateinit var  transaccionDeConpra : Transaccion
    lateinit var  transaccionDeVenta : Transaccion

    lateinit var criptoActivo1 : CriptoActivoWalletMapper
    lateinit var criptoActivo2 : CriptoActivoWalletMapper
    lateinit var criptoActivo3 : CriptoActivoWalletMapper

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
            usuarioPublicacionCompra,
            publicacionCompra.operacion,
            0,
            publicacionCompra.usuario!!.reputacion!!,
            publicacionCompra.usuario!!.walletAddress,
            Accion.REALIZAR_TRANSFERENCIA,
            usuarioPublicacionVenta)

    transaccionDeVenta = Transaccion(
    0,
    LocalDateTime.now(),
        publicacionVenta.criptoactivo,
        publicacionVenta.cantidad,
        publicacionVenta.cotizacion,
        publicacionVenta.monto,
         usuarioPublicacionVenta,
        publicacionVenta.operacion,
    0,
    publicacionVenta.usuario!!.reputacion!!,
    publicacionVenta.usuario!!.cvu,
    Accion.CONFIRMAR_RECEPCION,
    usuarioPublicacionCompra)

        transacciones.add(transaccionDeConpra)
        transacciones.add(transaccionDeVenta)

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


    @Test
    fun Al_registrar_Una_Wallet_Para_Un_usuario_dicha_wallet_queda_asociada_a_ese_mismo_usuario() {
        val wallet1 = transactionerService.createVirtualWallet(usuarioPublicacionCompra)
        assertEquals (usuarioPublicacionCompra.id, wallet1.usuario.id)

    }


    @Test
    fun Al_Consultar_por_las_wallets_Me_devuelve_todas_las_wallets_registradas() {
        transactionerService.createVirtualWallet(usuarioPublicacionCompra)
        transactionerService.createVirtualWallet(usuarioPublicacionVenta)
        val wallets =   transactionerService.wallets()
        assertEquals (2,wallets.size)
        assertEquals (usuarioPublicacionCompra.id!!,wallets.get(0).usuario.id)
        assertEquals (usuarioPublicacionCompra.id!!,wallets.get(1).usuario.id)
    }


    @Test
    fun El_Vendedor_es_Notificado_con_el_Monto_Que_el_comprador_deposito() {
        val deposito = Deposito  (usuarioPublicacionCompra ,300.0)
        transactionerService.notificarPago(transaccionDeConpra,deposito)
        val vendedor = transaccionDeConpra.getVendedor()
        assertEquals ( deposito.monto, vendedor.notificacionesDeDeposito.get(0).monto )
    }


    //Realizar transferencia
    @Test
    fun Al_realizarTransferencia_En_La_cuenta_del_Vendedor_queda_registrado_El_Deposito_Hecho_Por_el_Comprador(){
        val deposito =  transactionerService.realizarTransferencia( transaccionDeVenta)
        val vendedor = transaccionDeConpra.getVendedor()
        val depositos = mercadoPagoService.depositosDeLaCuentaDeUsuario(vendedor)
        assertTrue( depositos.contains(deposito))
    }

    //  Confirmar recepcion
    @Test
    fun Si_El_comprador_Realizo_La_Transferencia_Devuelve_True(){
         transactionerService.realizarTransferencia( transaccionDeVenta)
         val confirmado =  transactionerService.confirmarRecepcion(transaccionDeVenta)
         assertTrue (confirmado)
    }

    @Test
    fun Si_El_comprador_No_Realizo_La_Transferencia_Devuelve_False(){
        val confirmado =  transactionerService.confirmarRecepcion(transaccionDeVenta)
        assertFalse (confirmado)
    }


    // Enviar el CriptoActivo
    @Test
    fun Al_Enviar_El_CriptoActivo_ElComprador_Lo_recibe_en_su_Billetera_Virtual() {
        val criptoActivoExpected = transaccionDeVenta.criptoactivo!!
        transactionerService.enviarCriptoActivo(transaccionDeVenta)
       val  criptoActivoDelComprador =  transactionerService.getCriptoActivoDeLaVirtualWalletDeUsuario(transaccionDeVenta)
      assertEquals (criptoActivoExpected,  criptoActivoDelComprador.criptoactivo  )
    }


    @Test
    fun Si_El_CriptoActivo_No_Esta_En_La_WalletDelComprador_Entonces_El_MontoAcumulado_Sera_IgualQue_El_Monto_de_laTransaccion() {

        val walletDelComrador = transactionerService.getVirtualWallet(transaccionDeVenta.direccionEnvio!!)
        transactionerService.guardarCriptoActivo(walletDelComrador,transaccionDeVenta)
       val criptoActivoDelComprador =  transactionerService.getCriptoActivoDeLaVirtualWalletDeUsuario(transaccionDeVenta)
        assertNotEquals ( criptoActivoDelComprador.monto,  transaccionDeVenta.monto )
    }

    @Test
    fun Si_El_CriptoActivo_Ya_Esta_En_La_WalletDelComprador_Entonces_El_MontoAcumulado_Sera_LaCantidadDelMontoAcumulado_Mas_el_Monto_de_laTransaccion() {

        val walletDelComrador = transactionerService.getVirtualWallet(transaccionDeVenta.direccionEnvio!!)
        transactionerService.guardarCriptoActivo(walletDelComrador,transaccionDeVenta)
        val monto = transactionerService.getCriptoActivoDeLaVirtualWalletDeUsuario(transaccionDeVenta).monto
        transactionerService.guardarCriptoActivo(walletDelComrador,transaccionDeVenta)

        val criptoActivoDelComprador =  transactionerService.getCriptoActivoDeLaVirtualWalletDeUsuario(transaccionDeVenta)
        assertEquals ( criptoActivoDelComprador.monto, monto + transaccionDeVenta.monto )
    }



    @Test
    fun Si_ExisteCriptoActivo_devuelve_True() {
       val criptoActivo = criptoActivo1.criptoactivo
        val walletDelComprador = transactionerService.getVirtualWallet(transaccionDeVenta.direccionEnvio!!)
        val criptoActivos =  walletDelComprador.criptoactivos
        criptoActivos.add(criptoActivo1)
        val existe = transactionerService.existeCriptoActivo(criptoActivo,walletDelComprador)
        assertTrue (existe)

    }


    @Test
    fun Si_NO_ExisteCriptoActivo_devuelve_False() {
        val walletDelComprador = transactionerService.getVirtualWallet(transaccionDeVenta.direccionEnvio!!)
        val existe = transactionerService.existeCriptoActivo(criptoActivo1.criptoactivo,walletDelComprador)
        assertFalse (existe)
    }







    @Test
    fun volumenOperadoEntreFechas() {
    }

    @Test
    fun informeData() {
    }



    @Test
    fun transaccionesParaUnUsuario() {
    }

    @Test
    fun transaccionesDeUnUsuarioEntreFechas() {
    }

    @Test
    fun transaccionesAgrupadoPorCriptoActivos() {
    }

    @Test
    fun volumenCriptoActivos() {
    }





    @AfterEach
fun tearDown() {
    repository.deleteAll()
}

}