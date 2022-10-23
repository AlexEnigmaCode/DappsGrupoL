package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.*
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.UserRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
internal class UserServiceTest {
    @Autowired
   lateinit var   userService: UserService
    @Autowired
   lateinit var  repository: UserRepository


    var  users =listOf<UserViewMapper>()

    lateinit var user1: UserRegisterMapper
    lateinit var user2: UserRegisterMapper
    lateinit var updateUser: UserUpdateMapper

    @BeforeEach
    fun setUp() {

        user1 = UserRegisterMapper( "Ale", "Fariña", "ale@gmail.com", "address1","1", "123", "7"  )

        user2 = UserRegisterMapper( "Ulises", "Lopez","ulises@gmail.com", "address2","2", "234", "8" )

        updateUser = UserUpdateMapper( "ale@edu.unq.com", "another_address1","11", "1234", "9"  )
    }

    /**getUsers*/
    @Test
    fun al_solicitar_a_una_DB_sin_usuarios_no_devuelve_ningun_usuario() {
        users = userService.findAll()

        assertTrue( users.isEmpty() )
    }

    @Test
    fun al_solicitar_a_La_DB_todos_los_usuarios_Devuelve_la_cantidad_de_usuarios_registrados() {
        userService.register(user1)
        userService.register(user2)
        users = userService.findAll()
        assertTrue( users.isNotEmpty() )
        assertEquals(2, users.size)
    }

   /**Register */

    @Test
    fun el_usuario_registrado_mantiene_las_mismas_propiedades() {
        val newser1 = userService.register(user1)
        assertEquals(user1.name, newser1.name)
        assertEquals(user1.surname, newser1.surname)
        assertEquals(user1.email, newser1.email)
        assertEquals(user1.address, newser1.address)
       assertEquals(user1.cvu, newser1.cvu)
        assertEquals(user1.walletAddress, newser1.walletAddress)
    }


    @Test
    fun al_Registrar_un_usuario_se_incrementa_la_cantidad_en_una_unidad() {
        userService.register(user1)
        val users = userService.findAll()
        assertEquals(1, users.size)
    }

    @Test
    fun al_Intentar_Registrar_un_usuario_con_mail_o_password_existente_lanza_excepcion() {
        userService.register(user1)
        assertThrows<UsernameExistException> {  userService.register(user1) }
    }


    /**logginr*/
    @Test
    fun al_intentar_loguearse_un_usuario_no_registrado_Lanza_excepcion() {
        userService.register(user1)
      assertThrows<ItemNotFoundException> {  userService.login( "ulisese@gmail.com", "2") }
    }

    @Test
    fun un_usuario_se_loguea_si_esta_registrado() {
        val registered= userService.register(user1)
        val logged= userService.login( registered.email!!, "1")
        assertEquals(registered.id,  logged.id)
        assertEquals(registered.name,  logged.name)
        assertEquals(registered.surname, logged.surname)
        assertEquals(registered.email,  logged.email)
        assertEquals(registered.address,  logged.address)
        assertEquals(registered.cvu,  logged.cvu)
        assertEquals(registered.walletAddress,  logged.walletAddress)
    }


  /** findById*/
    @Test
    fun al_intentar_buscar_un_usuario_con_id_no_existente_Lanza_excepcion() {
     val  saved = userService.register(user1)
     //assertEquals (1 ,saved.id)
     assertThrows<ItemNotFoundException> {  userService.findByID( 200) }
    }


    @Test
    fun Si_el_id_es_existente_Retorna_el_usuario_asociado_con_ese_id_() {
        val newuser =userService.register(user1)
        val userFound = userService.findByID(newuser.id!!)
        assertEquals( newuser.id, userFound.id)
        assertEquals( newuser.name, userFound.name)
        assertEquals( newuser.surname, userFound.surname)
        assertEquals( newuser.email, userFound.email)
       assertEquals( newuser.cvu ,userFound.cvu)
        assertEquals( newuser.walletAddress, userFound.walletAddress)
    }


    /** update* */
    @Test
    fun al_intentar_actualizar_un_usuario_con_id_no_existente_Lanza_excepcion() {
        userService.register(user1)
        assertThrows<ItemNotFoundException> {  userService.update(2, updateUser)}
    }

    @Test
    fun Si_el_id_es_existente_Actualiza_el_usuario_asociado_con_ese_id_() {
        val newuser = userService.register(user1)
        val updated = userService.update(newuser.id!!, updateUser)
        val restored= userService.findByID(newuser.id!!)
        assertEquals( updated.id, restored.id)
        assertEquals( updated.name, restored.name)
        assertEquals( updated.surname, restored.surname)
        assertEquals( updated.email, restored.email)
        assertEquals( updated.address, restored.address)
        assertEquals( updated.cvu ,restored.cvu)
        assertEquals(updated.walletAddress , restored.walletAddress)
    }


    /** DeleteById */
    @Test
    fun al_intentar_borrar_un_usuario_con_id_no_existente_lanza_excepcionm_y_La_DB_se_mantiene_sin_alterar() {
        userService.register(user1)
        assertThrows<ItemNotFoundException> {  userService.deleteById(2)}
        val users = userService.findAll()
        assertTrue( users.isNotEmpty() )
    }

    @Test
    fun Si_el_id_es_existente_el_usuario_asociado_con_ese_id_es_eliminado_() {
        val newUser = userService.register(user1)
        userService.deleteById(newUser.id!!)
        val users = userService.findAll()
        assertTrue( users.isEmpty() )
    }


    @AfterEach
    fun tearDown() {
        repository.deleteAll()
    }

}

