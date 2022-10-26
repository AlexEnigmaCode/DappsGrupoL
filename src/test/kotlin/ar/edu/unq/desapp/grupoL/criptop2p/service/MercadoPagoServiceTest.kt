package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.CuentaCVU
import ar.edu.unq.desapp.grupoL.criptop2p.Deposito
import ar.edu.unq.desapp.grupoL.criptop2p.ItemNotFoundException
import ar.edu.unq.desapp.grupoL.criptop2p.model.Usuario
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class MercadoPagoServiceTest {

    @Autowired
    lateinit var mercadoPagoService: MercadoPagoService

    val cuentas = mutableListOf<CuentaCVU>()
    val depositosusuario1 = mutableListOf<Deposito>()
    val depositosusuario2 = mutableListOf<Deposito>()
    lateinit var cuentausuario1: CuentaCVU
    lateinit var cuentausuario2 : CuentaCVU
    lateinit var deposito1: Deposito
    lateinit var deposito2: Deposito
    lateinit var deposito3: Deposito

    lateinit var usuario1: Usuario
    lateinit var usuario2:  Usuario
    lateinit var usuario3:  Usuario

    @BeforeEach
    fun setUp() {
        usuario1 = Usuario (1, "Ale", "Fariña", "ale@gmail.com", "address1","1", "123", "7" ,0,0.0 )
        usuario2 = Usuario(2, "Ulises", "Lopez","ulises@gmail.com", "address2","2", "234", "8" ,0, 5.0 )
        usuario3 = Usuario(3, "User3", "UserApellido","user3@gmail.com", "address3","3", "567", "9" ,0, 5.0 )


       deposito1 = Deposito (usuario2,400.0)
       deposito2 = Deposito (usuario3,500.0)
       deposito3 = Deposito (usuario1,300.0)

        depositosusuario1.add(deposito1)
        depositosusuario1.add(deposito2)
        depositosusuario2.add(deposito2)
        depositosusuario2.add(deposito3)

        cuentausuario1 = CuentaCVU(usuario1, depositosusuario1)
        cuentausuario2 = CuentaCVU(usuario2, depositosusuario2)

        cuentas.add(cuentausuario1)
        cuentas.add(cuentausuario2)


        }



    @Test
    fun cuentas() {
        mercadoPagoService.setCuentasClientes(cuentas)
        val cuentas = mercadoPagoService.cuentas()
        assertEquals (2, cuentas.size)
        assertEquals(cuentausuario1.usuario.name, cuentas.get(0).usuario.name)
        assertEquals(cuentausuario2.usuario.name, cuentas.get(1).usuario.name)
    }

    @Test
    fun depositos() {
     mercadoPagoService.setDepositoCuentas(depositosusuario1)
     val depositos = mercadoPagoService.depositos()
     assertEquals (deposito1.usuario.name, depositos.get(0).usuario.name)
     assertEquals (deposito2.usuario.name, depositos.get(1).usuario.name )
    }

    @Test
    fun Al_crear_una_cuenta_para_Un_Nuevo_cliente_Se_registra_una_nueva_cuenta_MercadoPago() {
     mercadoPagoService.crearCuentaParaCliente(usuario1)
     val cuentas = mercadoPagoService.cuentas()
     assertEquals (1,cuentas.size)
    }

    @Test
    fun Si_el_CVU_A_buscar_es_inexistente_Lanza_ItemNotFoundException() {
      assertThrows<ItemNotFoundException> { mercadoPagoService.getCuenta(usuario1.cvu!!) }

    }

    @Test
    fun Si_el_CVU_A_buscar_existe_Me_devuelve_la_cuenta_asociada_a_dicho_CVU() {

        mercadoPagoService.crearCuentaParaCliente(usuario1)
        val  cuenta = mercadoPagoService.getCuenta(usuario1.cvu!!)
        assertEquals(usuario1.cvu, cuenta.usuario.cvu)

    }



    @Test
    fun depositar() {
        mercadoPagoService.crearCuentaParaCliente(usuario1)
       val  cuenta = mercadoPagoService.getCuenta(usuario1.cvu!!)
       val deposito = mercadoPagoService.depositar(cuenta,300.0,usuario2)
       assertEquals( 1,cuenta.depositos.size)
       assertEquals (300, deposito.monto)



    }

    @Test
    fun Al_Actualizar_Deposito_Si_eL_deposito_Para_la_cuenta_No_existe_Se_crea_Un_NuevoDeposito() {
        mercadoPagoService.crearCuentaParaCliente(usuario1)
        val  cuenta = mercadoPagoService.getCuenta(usuario1.cvu!!)
        val deposito = Deposito (usuario2,300.0,  )
        val depositoActualizado = mercadoPagoService.actualizarDeposito(cuenta,deposito,)
        assertEquals( 1,cuenta.depositos.size)
        assertEquals (300.0, depositoActualizado.monto)

    }


    @Test
    fun Al_Actualizar_Deposito_Si_eL_deposito_Para_la_cuenta_existe_Se_Suma_El_momto_Ingresado_AlDeposito() {
        mercadoPagoService.crearCuentaParaCliente(usuario1)
        val  cuenta = mercadoPagoService.getCuenta(usuario1.cvu!!)
        val deposito = mercadoPagoService.depositar(cuenta,300.0,usuario2)
        val depositoActualizado = mercadoPagoService.actualizarDeposito(cuenta,deposito,)
        assertEquals( 1,cuenta.depositos.size)
        assertEquals (600.0, depositoActualizado.monto)

    }

    @Test
    fun Si_el_usuario_deposito_en_La_cuenta_Me_devuelve_el_Monto_Al_hacer_La_consulta() {
        mercadoPagoService.crearCuentaParaCliente(usuario1)
        val  cuenta = mercadoPagoService.getCuenta(usuario1.cvu!!)
        val deposito = mercadoPagoService.depositar(cuenta,300.0,usuario2)
        val monto = mercadoPagoService.consultarMonto(cuenta,usuario2)
        assertEquals (deposito.monto, monto)
    }

    @Test
    fun Si_el_usuario_No_deposito_en_La_cuenta_Lanza_ItemNotFoundException_Al_hacer_La_consulta() {
        mercadoPagoService.crearCuentaParaCliente(usuario1)
        val  cuenta = mercadoPagoService.getCuenta(usuario1.cvu!!)
       assertThrows<ItemNotFoundException> { mercadoPagoService.consultarMonto(cuenta,usuario2) }
    }

    @AfterEach
    fun tearDown() {
    }
}