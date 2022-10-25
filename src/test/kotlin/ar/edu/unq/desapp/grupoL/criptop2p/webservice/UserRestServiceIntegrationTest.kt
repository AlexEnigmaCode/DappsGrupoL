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
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.ResponseEntity
import java.lang.Class
import java.util.*

@SpringBootTest
internal class UserRestServiceIntegrationTest {


    private val builder: ResponseEntity.BodyBuilder? = null

    var  userRestService = UserRestService()


     final val userService =  UserService()

    lateinit var  repository: UserRepository


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
       val  responseService : ResponseEntity<MutableList<UserViewMapper>>
       val users: List<UserViewMapper> = userRestService.allUsers()
       assertEquals (2 , users.size)
       assertEquals (1, users.get(0).id)
        assertEquals ("Ale", users.get(0).name)
        assertEquals (2, users.get(1).id)
        assertEquals ("Ulises", users.get(1).name)
        }


    @Test
    fun userById() {

        val newUser = userService.findByID(userFound.id!!)
        assertEquals(  userFound.id, newUser.id)
        assertEquals(  userFound.name, newUser.name)
        assertEquals(  userFound.surname, newUser.surname)
        assertEquals(  userFound.email,newUser.email)
        assertEquals(  userFound.cvu ,newUser.cvu)
        assertEquals(  userFound.walletAddress, newUser.walletAddress)
    }



    @AfterEach
    fun tearDown() {
    }
}