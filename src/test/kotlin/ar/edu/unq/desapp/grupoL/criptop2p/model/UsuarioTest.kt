package ar.edu.unq.desapp.grupoL.criptop2p.model


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

    @BeforeEach
    fun setUp() {
        user1 = Usuario (1, "Ale", "Fariña", "ale@gmail.com", "address1","1", "123", "7" ,0,0.0 )
        user2 = Usuario(2, "Ulises", "Lopez","ulises@gmail.com", "address2","2", "234", "8" ,0, 5.0 )
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