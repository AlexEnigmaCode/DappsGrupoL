package ar.edu.unq.desapp.grupoL.criptop2p.model

import ar.edu.unq.desapp.grupoL.criptop2p.Accion
import ar.edu.unq.desapp.grupoL.criptop2p.Operacion
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.time.LocalDateTime

internal class TransaccionTest {
    lateinit var user1: Usuario
    lateinit var user2: Usuario
    lateinit var transaccionDeCompra: Transaccion
    lateinit var transaccionDeVenta: Transaccion

    @BeforeEach
    fun setUp() {
        user1 = Usuario (1, "Ale", "Fariña", "ale@gmail.com", "address1","1", "123", "7" ,0,0.0 )
        user2 = Usuario (2, "Ulises", "Lopez","ulises@gmail.com", "address2","2", "234", "8" ,0, 5.0 )

        transaccionDeCompra= Transaccion(
            1,
            LocalDateTime.now(),
            "A",
            5,
            20.0,
            100.0,
            user1,
            Operacion.COMPRA.tipo,
            0,
            2.0,
            user1.walletAddress,
            Accion.REALIZAR_TRANSFERENCIA,
            user2)


        transaccionDeVenta= Transaccion(
            2,
            LocalDateTime.now(),
            "B",
            5,
            20.0,
            100.0,
            user2,
            Operacion.VENTA.tipo,
            0,
            2.0,
            user2.cvu,
            Accion.CONFIRMAR_RECEPCION,
            user1)
    }



    @Test
    fun AL_pedir_A_la_transaccion_El_Comprador_Si_la_Operacion_es_Compra_Devuelve_User1() {
        val comprador = transaccionDeCompra.getComprador()
        assertEquals  ( user1.name, comprador.name)
    }

    @Test
    fun AL_pedir_A_la_transaccion_El_Comprador_Si_la_Operacion_es_Venta_Devuelve_User2() {
        val comprador = transaccionDeCompra.getComprador()
        assertEquals  ( user2.name, comprador.name)
    }

    @Test
    fun AL_pedir_A_la_transaccion_El_Vendedor_Si_la_Operacion_es_Venta_Devuelve_User2() {
        val vendedor = transaccionDeCompra.getVendedor()
        assertEquals  ( user2.name,vendedor.name)
    }

    @Test
    fun AL_pedir_A_la_transaccion_El_Vendedor_Si_la_Operacion_es_Compra_Devuelve_User1() {
        val vendedor = transaccionDeCompra.getVendedor()
        assertEquals  ( user1.name, vendedor.name)
    }

    @AfterEach
    fun tearDown() {
    }
}