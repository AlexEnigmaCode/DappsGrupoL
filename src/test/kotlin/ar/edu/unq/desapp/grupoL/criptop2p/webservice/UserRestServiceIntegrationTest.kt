package ar.edu.unq.desapp.grupoL.criptop2p.webservice

import ar.edu.unq.desapp.grupoL.criptop2p.UserRegisterMapper
import ar.edu.unq.desapp.grupoL.criptop2p.UserUpdateMapper
import ar.edu.unq.desapp.grupoL.criptop2p.UserViewMapper
import ar.edu.unq.desapp.grupoL.criptop2p.model.Usuario
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.UserRepository
import ar.edu.unq.desapp.grupoL.criptop2p.service.UserService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.ResponseEntity
import java.util.*

@SpringBootTest
internal class UserRestServiceIntegrationTest {


    private val builder: ResponseEntity.BodyBuilder? = null

    var  userRestService = UserRestService()


     final val userService =  UserService()


    var  usersMocks = mutableListOf<UserViewMapper>()
    lateinit var  userFoundMock : Optional<Usuario?>

    lateinit var user1: UserRegisterMapper
    lateinit var user2: UserRegisterMapper
    lateinit var userView1: UserViewMapper
    lateinit var userView2: UserViewMapper
    lateinit var updateUser: UserUpdateMapper
    lateinit var userFound: Usuario

   var  userServiceMock: UserService = mock(userService::class.java)

   var  userRepositoryMock: UserRepository = mock(UserRepository::class.java)


    @BeforeEach
    fun setUp() {
        user1 = UserRegisterMapper( "Ale", "Fariña", "ale@gmail.com", "address1","1", "123", "7"  )

        user2 = UserRegisterMapper( "Ulises", "Lopez","ulises@gmail.com", "address2","2", "234", "8" )

        updateUser = UserUpdateMapper( "ale@edu.unq.com", "another_address1","11", "1234", "9"  )
        userView1 = UserViewMapper   ( 1,"Ale", "Fariña", "ale@gmail.com", "address1","123", "7",  5, 20.0  )
        userView2 = UserViewMapper  ( 2, "Ulises", "Lopez","ulises@gmail.com", "address2","234", "8",  2, 50.0 )
        usersMocks.add(userView1)
        usersMocks.add(userView2)

        userFound = Usuario(1,"Ale","Fariña","ale@gmail.com", "address1","1", "123", "7",5,20.0 )

        Mockito.`when`( userServiceMock.findAll()).thenReturn(usersMocks)
        Mockito.`when`( userRepositoryMock.findById(userView1.id!!)).thenReturn(userFoundMock)
        userFound = userFoundMock.get()
        userFound.id=1
        userFound.name= "Ale"
        userFound.surname = "Fariña"
        userFound.email = "ale@gmail.com"
        userFound.address = "address1"
        userFound.password = "1"
        userFound.cvu = "123"
        userFound.walletAddress = "7"
        userFound.cantidadOperaciones =5
        userFound.reputacion =20.0
    }



    @Test
    fun allUsers() {
        val response: ResponseEntity<List<UserViewMapper>> = userRestService.allUsers() as ResponseEntity<List<UserViewMapper>>
        assertEquals (2 , response.body!!.size)
        assertEquals (1, response.body!!.get(0).id)
        assertEquals ("Ale", response.body!!.get(0).name)
        assertEquals (2, response.body!!.get(1).id)
        assertEquals ("Ulises", response.body!!.get(1).name)
        }


    @Test
    fun userById() {
        val response: ResponseEntity<Usuario> = userRestService.userById(userFound.id!!) as ResponseEntity<Usuario>
        assertEquals(  userFound.id, response.body!!.id)
        assertEquals(  userFound.name, response.body!!.name)
        assertEquals(  userFound.surname, response.body!!.surname)
        assertEquals(  userFound.email,response.body!!.email)
        assertEquals(  userFound.cvu ,response.body!!.cvu)
        assertEquals(  userFound.walletAddress, response.body!!.walletAddress)
    }



    @AfterEach
    fun tearDown() {
    }
}