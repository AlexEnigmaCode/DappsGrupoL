package ar.edu.unq.desapp.grupoL.criptop2p.model

import ar.edu.unq.desapp.grupoL.criptop2p.Accion
import ar.edu.unq.desapp.grupoL.criptop2p.Operacion
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.time.LocalDateTime

internal class TransaccionTest {
    lateinit var comprador: Usuario
    lateinit var vendedor: Usuario
    lateinit var transaccionDeCompra: Transaccion
    lateinit var transaccionDeVenta: Transaccion

    @BeforeEach
    fun setUp() {
        comprador= Usuario (1, "Ale", "Fariña", "ale@gmail.com", "address1","1", "123", "7" ,0,0.0 )
        vendedor = Usuario (2, "Ulises", "Lopez","ulises@gmail.com", "address2","2", "234", "8" ,0, 5.0 )

        transaccionDeCompra= Transaccion(
            1,
            LocalDateTime.now(),
            "A",
            5,
            20.0,
            100.0,
            comprador,
            Operacion.COMPRA.tipo,
            0,
            2.0,
            comprador.walletAddress,
            Accion.REALIZAR_TRANSFERENCIA,
            vendedor)


        transaccionDeVenta= Transaccion(
            2,
            LocalDateTime.now(),
            "B",
            5,
            20.0,
            100.0,
            vendedor,
            Operacion.VENTA.tipo,
            0,
            2.0,
           vendedor.cvu,
            Accion.CONFIRMAR_RECEPCION,
            comprador)
    }

   @Test
    fun AL_pedir_A_la_transaccion_El_Comprador_Si_la_Operacion_es_Compra_Devuelve_Comprador() {
        val compradorObtenido = transaccionDeCompra.getComprador()
        assertEquals  ( comprador.name, compradorObtenido.name)
    }

    @Test
    fun AL_pedir_A_la_transaccion_El_Comprador_Si_la_Operacion_es_Venta_Devuelve_Comprador() {
        val compradorObtenido = transaccionDeVenta.getComprador()
        assertEquals  ( comprador.name, compradorObtenido.name)
    }

    @Test
    fun AL_pedir_A_la_transaccion_El_Vendedor_Si_la_Operacion_es_Venta_Devuelve_Vendedor() {
        val vendedorObtenido = transaccionDeVenta.getVendedor()
        assertEquals  ( vendedor.name,vendedorObtenido.name)
    }

    @Test
    fun AL_pedir_A_la_transaccion_El_Vendedor_Si_la_Operacion_es_Compra_Devuelve_Vendedor() {
        val vendedorObtenido = transaccionDeCompra.getVendedor()
        assertEquals  ( vendedor.name, vendedorObtenido.name)
    }

    @AfterEach
    fun tearDown() {
    }
}