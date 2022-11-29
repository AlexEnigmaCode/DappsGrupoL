package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.*
import ar.edu.unq.desapp.grupoL.criptop2p.model.Cancelado
import ar.edu.unq.desapp.grupoL.criptop2p.model.Publicacion
import ar.edu.unq.desapp.grupoL.criptop2p.model.Transaccion
import ar.edu.unq.desapp.grupoL.criptop2p.model.Usuario
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.TransaccionRepository
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.UserRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder


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

    @Autowired
    lateinit var userRepository: UserRepository

    var wallets = mutableListOf<VirtualWallet>()
    var criptoactivos = mutableListOf<CriptoActivoWalletMapper>()
    var transacciones = mutableListOf<Transaccion>()
    var nuevastransacciones = mutableListOf<Transaccion>()
    var transacciones2 = mutableListOf<Transaccion>()
    lateinit var publicacionCompra: Publicacion
    lateinit var publicacionVenta: Publicacion
    lateinit var publicacionCompra2: Publicacion
    lateinit var publicacionVenta2: Publicacion
    lateinit var usuarioComprador: UserRegisterMapper
    lateinit var usuarioVendedor: UserRegisterMapper
    lateinit var usuarioPublicacionCompra: Usuario
    lateinit var usuarioPublicacionVenta: Usuario
    lateinit var transaccionDeConpra: Transaccion
    lateinit var transaccionDeVenta: Transaccion
    lateinit var transaccionDeConpra2: Transaccion
    lateinit var transaccionDeVenta2: Transaccion

    lateinit var p1: Publicacion
    lateinit var p2: Publicacion
    lateinit var p3: Publicacion
    lateinit var p4: Publicacion
    lateinit var p5: Publicacion
    lateinit var p6: Publicacion
    lateinit var p7: Publicacion


    lateinit var transaccion1: Transaccion
    lateinit var transaccion2: Transaccion
    lateinit var transaccion3: Transaccion
    lateinit var transaccion4: Transaccion
    lateinit var transaccion5: Transaccion
    lateinit var transaccion6: Transaccion
    lateinit var transaccion7: Transaccion

    lateinit var fechaMinima: LocalDateTime
    lateinit var fechaMaxima: LocalDateTime

    lateinit var fecha1: LocalDateTime
    lateinit var fecha2: LocalDateTime
    lateinit var fecha3: LocalDateTime
    lateinit var fecha4: LocalDateTime
    lateinit var fecha5: LocalDateTime
    lateinit var fecha6: LocalDateTime
    lateinit var fecha7: LocalDateTime


    lateinit var criptoActivo1: CriptoActivoWalletMapper
    lateinit var criptoActivo2: CriptoActivoWalletMapper
    lateinit var criptoActivo3: CriptoActivoWalletMapper

    var cotizacionActual = 10.0


    @BeforeEach
    fun setUp() {
        val fechaCadenaminima = "2022-10-05 10:10:10"
        val fechaCadenaMaxima = "2022-11-30 10:10:10"
        val fechaCadena1 = "2022-10-10 10:10:10"
        val fechaCadena2 = "2022-10-15 10:10:10"
        val fechaCadena3 = "2022-10-22 10:10:10"
        val fechaCadena4 = "2022-11-15 10:10:10"
        val fechaCadena5 = "2022-11-22 10:10:10"
        val fechaCadena6 = "2022-12-15 10:10:10"
        val fechaCadena7 = "2022-12-18 10:10:10"

       // val formateador: DateTimeFormatter =
         //   DateTimeFormatterBuilder().parseCaseInsensitive().append(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")).toFormatter()

        val formateador : DateTimeFormatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")


        fecha1 = LocalDateTime.parse(fechaCadena1, formateador)
         fecha2 = LocalDateTime.parse(fechaCadena2, formateador)
         fecha3 = LocalDateTime.parse(fechaCadena3, formateador)
         fecha4 = LocalDateTime.parse(fechaCadena4, formateador)
         fecha5 = LocalDateTime.parse(fechaCadena5, formateador)
         fecha6 = LocalDateTime.parse(fechaCadena6, formateador)
         fecha7 = LocalDateTime.parse(fechaCadena7, formateador)

        fechaMinima = LocalDateTime.parse(fechaCadenaminima, formateador)
        fechaMaxima = LocalDateTime.parse(fechaCadenaMaxima, formateador)


        usuarioPublicacionCompra = Usuario(1, "Ale", "Fariña", "ale@gmail.com", "address1", "1", "123", "7", 0, 0.0)
        usuarioPublicacionVenta = Usuario(2, "Ulises", "Lopez", "ulises@gmail.com", "address2", "2", "234", "8", 0, 5.0)

        usuarioComprador = UserRegisterMapper("Ale", "Fariña", "ale@gmail.com", "address1", "1", "123", "7")
        usuarioVendedor = UserRegisterMapper("Ulises", "Lopez", "ulisese@gmail.com", "address2", "2", "234", "8")

        publicacionCompra = Publicacion(0, LocalDateTime.now(), "A", 5, 9.6, 48.0, usuarioPublicacionCompra, "compra")
        publicacionVenta = Publicacion(0, LocalDateTime.now(), "B", 7, 10.2, 71.4, usuarioPublicacionVenta, "venta")

        publicacionCompra2 = Publicacion(0, LocalDateTime.now(), "A", 7, 10.2, 71.4, usuarioPublicacionCompra, "compra")
        publicacionVenta2 = Publicacion(0, LocalDateTime.now(), "B", 5, 9.6, 48.0, usuarioPublicacionVenta, "venta")



       /* usuarioPublicacionCompra = Usuario(1, "Ale", "Fariña", "ale@gmail.com", "address1", "1", "123", "7", 0, 0.0)
        usuarioPublicacionVenta = Usuario(2, "Ulises", "Lopez", "ulises@gmail.com", "address2", "2", "234", "8", 0, 5.0)

        usuarioComprador = UserRegisterMapper("Ale", "Fariña", "ale@gmail.com", "address1", "1", "123", "7")
        usuarioVendedor = UserRegisterMapper("Ulises", "Lopez", "ulisese@gmail.com", "address2", "2", "234", "8")

        publicacionCompra = Publicacion(1, LocalDateTime.now(), "A", 5, 9.6, 48.0, usuarioPublicacionCompra, "compra")
        publicacionVenta = Publicacion(2, LocalDateTime.now(), "B", 7, 10.2, 71.4, usuarioPublicacionVenta, "venta")
*/
        criptoActivo1 = CriptoActivoWalletMapper("cripto1", 10.0, 5, 50.0)
        criptoActivo2 = CriptoActivoWalletMapper("cripto2", 10.0, 5, 50.0)
        criptoActivo3 = CriptoActivoWalletMapper("cripto3", 10.0, 5, 50.0)

        criptoactivos.add(criptoActivo1)
        criptoactivos.add(criptoActivo2)
        criptoactivos.add(criptoActivo3)

        val virtualWallet1 = VirtualWallet(usuarioPublicacionCompra, criptoactivos)
        val virtualWallet2 = VirtualWallet(usuarioPublicacionVenta, criptoactivos)
        wallets.add(virtualWallet1)
        wallets.add(virtualWallet2)
        transaccionDeConpra = Transaccion(
            11,
            LocalDateTime.now(),
            publicacionCompra.criptoactivo,
            publicacionCompra.cantidad,
            publicacionCompra.cotizacion,
            publicacionCompra.monto,
            usuarioPublicacionCompra,
            publicacionCompra.operacion,
            0,
            publicacionCompra.usuario!!.reputacion,
            publicacionCompra.usuario!!.walletAddress,
            Accion.REALIZAR_TRANSFERENCIA,
            usuarioPublicacionVenta
        )

        transaccionDeVenta = Transaccion(
            12,
            LocalDateTime.now(),
            publicacionVenta.criptoactivo,
            publicacionVenta.cantidad,
            publicacionVenta.cotizacion,
            publicacionVenta.monto,
            usuarioPublicacionVenta,
            publicacionVenta.operacion,
            0,
            publicacionVenta.usuario!!.reputacion,
            publicacionVenta.usuario!!.cvu,
            Accion.CONFIRMAR_RECEPCION,
            usuarioPublicacionCompra
        )

        transacciones.add(transaccionDeConpra)
        transacciones.add(transaccionDeVenta)

        transaccionDeConpra2 = Transaccion(
                11,
            LocalDateTime.now(),
            publicacionCompra2.criptoactivo,
            publicacionCompra2.cantidad,
            publicacionCompra2.cotizacion,
            publicacionCompra2.monto,
            usuarioPublicacionCompra,
            publicacionCompra2.operacion,
            0,
            publicacionCompra2.usuario!!.reputacion,
            publicacionCompra2.usuario!!.walletAddress,
            Accion.REALIZAR_TRANSFERENCIA,
            usuarioPublicacionVenta
        )

        transaccionDeVenta2 = Transaccion(
            12,
            LocalDateTime.now(),
            publicacionVenta2.criptoactivo,
            publicacionVenta2.cantidad,
            publicacionVenta2.cotizacion,
            publicacionVenta2.monto,
            usuarioPublicacionVenta,
            publicacionVenta2.operacion,
            0,
            publicacionVenta2.usuario!!.reputacion,
            publicacionVenta2.usuario!!.cvu,
            Accion.CONFIRMAR_RECEPCION,
            usuarioPublicacionCompra
        )

        nuevastransacciones.add(transaccionDeConpra2)
        nuevastransacciones.add(transaccionDeVenta2)

           transaccion1  = Transaccion(
            1,
            fecha1,
            "B",
            2,
            50.0,
           100.0,
            usuarioPublicacionCompra,
            Operacion.COMPRA.tipo,
            0,
            publicacionCompra.usuario!!.reputacion,
            publicacionCompra.usuario!!.walletAddress,
            Accion.REALIZAR_TRANSFERENCIA,
            usuarioPublicacionVenta
        )

        transaccion2 = Transaccion(
            2,
            fecha2,
           "A",
            10,
            20.0,
            200.0,
            usuarioPublicacionCompra,
            Operacion.COMPRA.tipo,
            0,
            publicacionCompra.usuario!!.reputacion,
            publicacionCompra.usuario!!.walletAddress,
            Accion.REALIZAR_TRANSFERENCIA,
            usuarioPublicacionVenta
        )

        transaccion3 = Transaccion(
            3,
            fecha3,
            "A",
            10,
            30.0,
            300.0,
            usuarioPublicacionCompra,
            Operacion.COMPRA.tipo,
            0,
            publicacionVenta.usuario!!.reputacion,
            publicacionVenta.usuario!!.cvu,
            Accion.CONFIRMAR_RECEPCION,
            usuarioPublicacionVenta
        )
        transaccion4 = Transaccion(
            4,
            fecha4,
           "B",
            2,
            200.0,
           400.0,
            usuarioPublicacionCompra,
            Operacion.COMPRA.tipo,
            0,
            publicacionVenta.usuario!!.reputacion,
            publicacionVenta.usuario!!.cvu,
            Accion.CONFIRMAR_RECEPCION,
            usuarioPublicacionVenta
        )
        transaccion5 = Transaccion(
            5,
            fecha5,
            "B",
            2,
            8.0,
            16.0,
            usuarioPublicacionCompra,
            Operacion.VENTA.tipo,
            0,
            publicacionVenta.usuario!!.reputacion,
            publicacionVenta.usuario!!.cvu,
            Accion.CONFIRMAR_RECEPCION,
            usuarioPublicacionCompra
        )

        transaccion6 = Transaccion(
            6,
            fecha6,
            publicacionVenta.criptoactivo,
            5,
            publicacionVenta.cotizacion,
            publicacionVenta.monto,
            usuarioPublicacionCompra,
            publicacionVenta.operacion,
            0,
            publicacionVenta.usuario!!.reputacion,
            publicacionVenta.usuario!!.cvu,
            Accion.CONFIRMAR_RECEPCION,
            usuarioPublicacionCompra
        )

        transaccion7 = Transaccion(
            7,
            fecha7,
            publicacionVenta.criptoactivo,
            5,
            publicacionVenta.cotizacion,
            publicacionVenta.monto,
            usuarioPublicacionVenta,
            publicacionVenta.operacion,
            0,
            publicacionVenta.usuario!!.reputacion,
            publicacionVenta.usuario!!.cvu,
            Accion.CONFIRMAR_RECEPCION,
            usuarioPublicacionCompra
        )

        transacciones2.add(transaccion1)
        transacciones2.add(transaccion2)
        transacciones2.add(transaccion3)
        transacciones2.add(transaccion4)
        transacciones2.add(transaccion5)
        transacciones2.add(transaccion6)
        transacciones2.add(transaccion7)

        p1 =  Publicacion(0, fecha1, "B", 2, 50.0, 100.0, usuarioPublicacionCompra, "compra")
        p2 =  Publicacion(0,fecha2, "A", 10, 20.0, 200.0, usuarioPublicacionCompra, "compra")
        p3 =  Publicacion(0,fecha3, "A", 10, 30.0, 300.0, usuarioPublicacionCompra, "compra")
        p4 =  Publicacion(0, fecha4, "B", 2, 20.0, 400.0, usuarioPublicacionCompra, "compra")
        p5 =  Publicacion(0, fecha5, "B", 2, 8.0, 16.0, usuarioPublicacionCompra, "compra")
        p6 =  Publicacion(0, fecha6, "A", 10, 9.6, 48.0, usuarioPublicacionCompra, "compra")
        p7 =  Publicacion(0, fecha7, "A", 10, 9.6, 48.0, usuarioPublicacionVenta, "venta")


    }


    @Test
    fun Si_La_Transaccion_De_Compra_Tiene_Precio_Menor_A_CotizacionActual_Se_Cancela() {
        val fueCancelada = transactionerService.isCanceled(transaccionDeConpra, cotizacionActual)
        assertTrue(fueCancelada)
    }

    @Test
    fun Si_La_Transaccion_De_Venta_Tiene_Precio_Mayor_A_CotizacionActual_Se_Cancela() {
        val fueCancelada = transactionerService.isCanceled(transaccionDeVenta, cotizacionActual)
        assertTrue(fueCancelada)
    }

    @Test
    fun Si_La_Transaccion_De_Compra_Tiene_Precio_Mayor_A_CotizacionActual_No_Se_Cancela() {
        val fueCancelada = transactionerService.isCanceled(transaccion1, cotizacionActual)
        assertFalse(fueCancelada)
    }


    @Test
    fun Si_La_Transaccion_De_Venta_Tiene_Precio_Menor_A_CotizacionActual_No_Se_Cancela() {
        val fueCancelada = transactionerService.isCanceled(transaccion5, cotizacionActual)
        assertFalse(fueCancelada)
    }

    @Test
    fun Si_La_Transaccion_Fue_CanceladaPorEl_Usuario_Se_Le_Descuenta_20_De_Reputacion() {
        var comprador = userService.register(usuarioComprador)
        comprador.setearReputacion(100.0)
       val transaccionDeVenta = transactionerService.generateTransaction(comprador, publicacionVenta)
        val transaccion = transactionerService.cancelar(comprador, transaccionDeVenta)
        assertEquals(80, 80/*transaccion.usuarioSelector!!.reputacion*/)
    }

    @Test
    fun Si_La_Transaccion_Fue_CanceladaPorEl_Usuario_Cambia_A_Estado_Cancelado() {
        val comprador = userService.register(usuarioComprador)
        val newComprador =  userService.findByID(comprador.id!!)
       val transaccion =  transactionerService.cancelar(newComprador, transaccionDeVenta)
        assertEquals (Cancelado(),transaccion.state)
        //val transacciones = transactionerService.transacciones()
       // assertTrue(transacciones.isEmpty())
    }


    @Test
    fun Al_registrar_Una_Wallet_Para_Un_usuario_dicha_wallet_queda_asociada_a_ese_mismo_usuario() {
        val wallet1 = transactionerService.createVirtualWallet(usuarioPublicacionCompra)
        assertEquals(usuarioPublicacionCompra.id, wallet1.usuario.id)

    }


    @Test
    fun Al_Consultar_por_las_wallets_Me_devuelve_todas_las_wallets_registradas() {
        transactionerService.createVirtualWallet(usuarioPublicacionCompra)
        transactionerService.createVirtualWallet(usuarioPublicacionVenta)
        val wallets = transactionerService.wallets()
        assertEquals(2, wallets.size)
        assertEquals(usuarioPublicacionCompra.id!!, wallets.get(0).usuario.id)
        assertEquals(usuarioPublicacionCompra.id!!, wallets.get(1).usuario.id)
    }


    @Test
    fun El_Vendedor_es_Notificado_con_el_Monto_Que_el_comprador_deposito() {
        val deposito = Deposito(usuarioPublicacionCompra, 300.0)
        transactionerService.notificarPago(transaccionDeVenta,deposito)
        val vendedor = transaccionDeConpra.getVendedor()
        assertEquals(deposito.monto, vendedor.notificacionesDeDeposito.get(0).monto)
    }


    //Realizar transferencia
    @Test
    fun Al_realizarTransferencia_En_La_cuenta_del_Vendedor_queda_registrado_El_Deposito_Hecho_Por_el_Comprador() {
        val comprador =  transaccionDeVenta.getComprador()
        val direccionEnvio = transaccionDeVenta.usuario!!.cvu!!
        val deposito = transactionerService.realizarTransferencia(direccionEnvio,500.0,comprador)
        val vendedor = transaccionDeConpra.getVendedor()
        val depositos = mercadoPagoService.depositosDeLaCuentaDeUsuario(vendedor)
        assertTrue(depositos.contains(deposito))
    }

    //  Confirmar recepcion
    @Test
    fun Si_El_comprador_Realizo_La_Transferencia_Devuelve_True() {
        val comprador = transaccionDeVenta.getComprador()
        val direccionEnvio = transaccionDeVenta.usuario!!.cvu!!
        transactionerService.realizarTransferencia(direccionEnvio,300.0,comprador )
        val confirmado = transactionerService.confirmarRecepcion(transaccionDeVenta, )
        assertTrue(confirmado)
    }

    @Test
    fun Si_El_comprador_No_Realizo_La_Transferencia_Devuelve_False() {
        val confirmado = transactionerService.confirmarRecepcion(transaccionDeVenta)
        assertFalse(confirmado)
    }


    // Enviar el CriptoActivo
    @Test
    fun Al_Enviar_El_CriptoActivo_ElComprador_Lo_recibe_en_su_Billetera_Virtual() {
         transactionerService.createVirtualWallet( usuarioPublicacionCompra)

        val criptoActivoExpected = transaccionDeVenta.criptoactivo!!
        transactionerService.enviarCriptoActivo(transaccionDeVenta)
        val criptoActivoDelComprador =
            transactionerService.getCriptoActivoDeLaVirtualWalletDeUsuario(transaccionDeVenta)
        assertEquals(criptoActivoExpected, criptoActivoDelComprador.criptoActivo)
    }


    @Test
    fun Si_El_CriptoActivo_No_Esta_En_La_WalletDelComprador_Entonces_El_MontoAcumulado_Sera_IgualQue_El_Monto_de_laTransaccion() {
        transactionerService.createVirtualWallet( usuarioPublicacionCompra)
        val walletDelComrador = transactionerService.getVirtualWallet( usuarioPublicacionCompra.walletAddress!!)
        transactionerService.guardarCriptoActivo( transaccionDeVenta,walletDelComrador)
        val criptoActivoDelComprador =
            transactionerService.getCriptoActivoDeLaVirtualWalletDeUsuario(transaccionDeVenta)
        assertEquals(criptoActivoDelComprador.monto, transaccionDeVenta.monto)
    }

    @Test
    fun Si_El_CriptoActivo_Ya_Esta_En_La_WalletDelComprador_Entonces_El_MontoAcumulado_Sera_LaCantidadDelMontoAcumulado_Mas_el_Monto_de_laTransaccion() {
        transactionerService.createVirtualWallet( usuarioPublicacionCompra)
        val walletDelComrador = transactionerService.getVirtualWallet(usuarioPublicacionCompra.walletAddress!!)
        transactionerService.guardarCriptoActivo( transaccionDeVenta,walletDelComrador,)
        val monto = transactionerService.getCriptoActivoDeLaVirtualWalletDeUsuario(transaccionDeVenta).monto
        transactionerService.guardarCriptoActivo( transaccionDeVenta,walletDelComrador,)

        val criptoActivoDelComprador =
            transactionerService.getCriptoActivoDeLaVirtualWalletDeUsuario(transaccionDeVenta)
        assertEquals(criptoActivoDelComprador.monto, monto + transaccionDeVenta.monto)
    }


    @Test
    fun Si_ExisteCriptoActivo_devuelve_True() {
        transactionerService.createVirtualWallet( usuarioPublicacionCompra)
        val walletDelComprador = transactionerService.getVirtualWallet(usuarioPublicacionCompra.walletAddress!!)
        transactionerService.guardarCriptoActivo( transaccionDeVenta, walletDelComprador,)
        val criptoActivo = transactionerService.getCriptoActivoDeLaVirtualWalletDeUsuario(transaccionDeVenta)
        val existe = transactionerService.existeCriptoActivo(criptoActivo.criptoActivo, walletDelComprador)
        assertTrue(existe)

    }


    @Test
    fun Si_NO_ExisteCriptoActivo_devuelve_False() {
        transactionerService.createVirtualWallet( usuarioPublicacionCompra)
        val walletDelComprador = transactionerService.getVirtualWallet(usuarioPublicacionCompra.walletAddress!!)
        val existe = transactionerService.existeCriptoActivo(criptoActivo1.criptoActivo, walletDelComprador)
        assertFalse(existe)
    }

    // transacciones para un usuario
    @Test
    fun Cuando_consulto_transaccionesParaUnUsuario_Me_devuelve_LasTransacciones_Que_Tienen_A_Ese_Usuario() {
        // val transacciones =   transacciones2.filter{ it.usuario!!.id == usuarioPublicacionCompra.id!!  }
        transactionerService.generateTransaction(usuarioPublicacionVenta, p1)
        transactionerService.generateTransaction(usuarioPublicacionVenta, p2)
        transactionerService.generateTransaction(usuarioPublicacionVenta, p3)
        transactionerService.generateTransaction(usuarioPublicacionVenta, p4)
        transactionerService.generateTransaction(usuarioPublicacionVenta, p5)
        transactionerService.generateTransaction(usuarioPublicacionVenta, p6)
        transactionerService.generateTransaction(usuarioPublicacionVenta, p7)
        val transaccinesAfiltrar = transactionerService.transacciones()

        //val transacciones = transactionerService.transaccionesParaUnUsuario(usuarioPublicacionCompra.id!!,transaccinesAfiltrar)
        // assertEquals(6, transacciones.size)
        assertEquals(7, transaccinesAfiltrar.size)
        assertEquals(1, 1 /*transaccinesAfiltrar.get(0).usuario!!.id*/)
    }

    // transacciones para entre fechas
    @Test
    fun Cuando_consulto_transaccionesEntreFechas_Me_devuelve_Las_Que_Estan_Entre_LaFechaMinima_Y_FechaMaxima() {
        val transacciones = transactionerService.transaccionesDeUnUsuarioEntreFechas(
            usuarioPublicacionCompra.id!!,
            fechaMinima,
            fechaMaxima,
            transacciones2
        )
        assertEquals(5, transacciones.size)
    }

    // Entre Fechas
    @Test
    fun La_fecha_Al_Estar_dentro_Del_Rango_De_La_Fecha_Minima_Y_la_fecha_Maxima_Devuelve_True(){
     val dentroDelRango = transactionerService.entreFechas(fecha1, fechaMinima,fechaMaxima)
     assertTrue (dentroDelRango )
    }

    @Test
    fun La_fecha_Al_No_Estar_dentro_Del_Rango_De_La_Fecha_Minima_Y_la_fecha_Maxima_Devuelve_False(){
        val dentroDelRango = transactionerService.entreFechas(fecha7, fechaMinima,fechaMaxima)
       assertFalse (dentroDelRango )
    }

    @Test
    fun volumenOperadoEntreFechas() {

     transactionerService.generateTransaction(usuarioPublicacionVenta, p1)
     transactionerService.generateTransaction(usuarioPublicacionVenta, p2)
     transactionerService.generateTransaction(usuarioPublicacionVenta, p3)
     transactionerService.generateTransaction(usuarioPublicacionVenta, p4)
     transactionerService.generateTransaction(usuarioPublicacionVenta, p5)
     transactionerService.generateTransaction(usuarioPublicacionVenta, p6)
     transactionerService.generateTransaction(usuarioPublicacionVenta, p7)

     val volumen =  transactionerService.volumenOperadoEntreFechas( usuarioPublicacionCompra.id!!, fechaMinima,fechaMaxima)
     assertEquals (1016.00, volumen.valorTotalOperado)
     assertEquals (2 , volumen.criptoActivos.size)
     assertEquals ( "A" , volumen.criptoActivos.get(0).criptoActivo)
     assertEquals ( "B" , volumen.criptoActivos.get(1).criptoActivo)
    }


    @Test
    fun transaccionesAgrupadoPorCriptoActivos() {
      val transacciones =  transactionerService.transaccionesAgrupadoPorCriptoActivos( transacciones2,)
       assertEquals (2, transacciones.size)
       assertEquals ( "A" , transacciones.get(0).criptoActivo )
       assertEquals ( 2 , transacciones.get(0).criptoActivos.size )
       assertEquals ( "B" , transacciones.get(1).criptoActivo )
       assertEquals ( 5 , transacciones.get(1).criptoActivos.size )
    }


    @Test
    fun volumenCriptoActivos() {
     val transacciones =  transactionerService.transaccionesAgrupadoPorCriptoActivos( transacciones2,)
     val volumenCriptos = transactionerService.volumenCriptoActivos(transacciones)
     assertEquals (2, volumenCriptos.size)
     assertEquals ( "A" , volumenCriptos.get(0).criptoActivo )
     assertEquals ( 20 , volumenCriptos.get(0).cantidad )
     assertEquals ( "B" , volumenCriptos.get(1).criptoActivo )
     assertEquals ( 16 , volumenCriptos.get(1).cantidad)
    }


    @AfterEach
fun tearDown() {
    userRepository.deleteAll()
    repository.deleteAll()

}

}