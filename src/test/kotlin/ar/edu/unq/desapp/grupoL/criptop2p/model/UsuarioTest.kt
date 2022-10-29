package ar.edu.unq.desapp.grupoL.criptop2p.model


import ar.edu.unq.desapp.grupoL.criptop2p.Deposito
import ar.edu.unq.desapp.grupoL.criptop2p.Operacion
import ar.edu.unq.desapp.grupoL.criptop2p.UserRegisterMapper
import ar.edu.unq.desapp.grupoL.criptop2p.model.Usuario
import ar.edu.unq.desapp.grupoL.criptop2p.UserViewMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
internal class UsuarioTest {
    var  users =listOf<Usuario>()

    lateinit var user1: Usuario
    lateinit var user2: Usuario
    lateinit var deposito: Deposito
    var notificaciones = mutableListOf<Deposito>()

    @BeforeEach
    fun setUp() {
        user1 = Usuario (1, "Ale", "Fariña", "ale@gmail.com", "address1","1", "123", "7" ,0,0.0 )
        user2 = Usuario (2, "Ulises", "Lopez","ulises@gmail.com", "address2","2", "234", "8" ,0, 5.0 )
       deposito = Deposito (user2 ,300.0)
           }



    @Test
    fun Si_El_usuario_no_tiene_Operaciones_devuelve_Cero(){
     val operaciones = user1.getOperaciones()
     assertEquals( 0,operaciones)
    }

    @Test
    fun Si_El_usuario_tiene_Operaciones_devuelve_dicha_cantidad_De_operaciones(){
        val operaciones = user1.icrementarOperqaciones()
        assertEquals( 1,operaciones)
    }

    @Test
   fun Si_El_usuario_no_tiene_Nootificaciones_devuelve_Cero(){
        val notificaciones = user1.getNotificaciones()
        assertEquals( 0, notificaciones)

    }

    @Test
    fun Si_El_usuario_tiene_Nootificaciones_devuelve_Tales_Notificaciones(){
        user1.notificar(deposito)
        val notificaciones = user1.getNotificaciones()
        assertEquals( 1, notificaciones)
    }



    @Test
    fun Al_incrementar_La_Operacion_De_Un_Usuario_Tiene_Una_Operacion_mas() {
       val cantidadOperaciones = user1.icrementarOperqaciones()
       assertEquals(1,cantidadOperaciones)
    }

    @Test
    fun Si_La_Reputacion_Actual_Es_Mayor_A_La_Reputacion_A_Descontar_Devuelve_La_Cantidad_Descontada() {
        val reputacion = user2.descontarReputacion(2.0)
        assertEquals(3.0,reputacion)
    }

    @Test
    fun Si_La_Reputacion_Actual_Es_Menor_A_La_Reputacion_A_Descontar_Devuelve_Cero() {
        val reputacion = user2.descontarReputacion(10.0)
        assertEquals(0.0,reputacion)
    }

    @Test
    fun Al_Incrementar_La_Repuutacion_Actual_El_Valor_Actual_De_La_Reputacion_Se_Incrementa_Con_el_Valor_Ingresado() {
        val reputacion = user1.incrementarReputacion(2.0)
        assertEquals(2.0,reputacion)
    }


    @AfterEach
    fun tearDown() {
    }
}