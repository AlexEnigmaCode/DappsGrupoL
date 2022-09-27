package ar.edu.unq.desapp.grupoL.criptop2p.webservice

import ar.edu.unq.desapp.grupoL.criptop2p.UserLoginMapper
import ar.edu.unq.desapp.grupoL.criptop2p.UserRegisterMapper
import ar.edu.unq.desapp.grupoL.criptop2p.UserUpdateMapper
import ar.edu.unq.desapp.grupoL.criptop2p.UserViewMapper

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.beans.factory.annotation.Autowired
import ar.edu.unq.desapp.grupoL.criptop2p.service.UserService

import org.springframework.http.ResponseEntity

import org.springframework.web.bind.annotation.*
import java.util.HashMap

@RestController
@EnableAutoConfiguration
class UserRestService {
    @Autowired
    private val userService: UserService? = null
    private val builder: ResponseEntity.BodyBuilder? = null

    @GetMapping("/api/users")
    fun allUsers(): ResponseEntity<*> {
       lateinit var users: List<UserViewMapper>
       val list = userService?.findAll()
        if (list != null) {

                users =  list.map { UserViewMapper(it.id, it.name, it.surname, it.email, it.address, it.cvu, it.walletAddress) }
            }
           return ResponseEntity.ok().body(users)
        }



    /**register a user*/
    @PostMapping("/api/register")
    fun register(@RequestBody user: UserRegisterMapper): ResponseEntity<*> {
        var response : ResponseEntity<*>?

        try {
            val newUser = userService!!.register(user)
            val userview = UserViewMapper(
                newUser.id,
                newUser.name,
                newUser.surname,
                newUser.email,
                newUser.address,
                newUser.cvu,
                newUser.walletAddress
            )

            ResponseEntity.status(201)
           response =  ResponseEntity.ok().body(userview)
        } catch (e: Exception) {
            ResponseEntity.status(404)

            val resultado: MutableMap<String, String> = HashMap()
            resultado["email of user already exits"] = user.email.toString()
            response = ResponseEntity.ok().body<Map<String, String>>(resultado)
        }
        return response!!
    }

    /**login a user*/
    @PostMapping("/api/login")
    fun login(@RequestBody user: UserLoginMapper): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        try {
            val newUser = userService!!.login(user.email, user.password)
            val userview = UserViewMapper(newUser.id,newUser.name,newUser.surname,newUser.email,newUser.address,newUser.cvu,newUser.walletAddress)
            ResponseEntity.status(200)
           response = ResponseEntity.ok().body(userview)
        }
        catch (e:Exception) {
            ResponseEntity.status(404)
           val resultado: MutableMap<String, String> = HashMap()
            resultado["user not found"] = user.email
            response = ResponseEntity.ok().body<Map<String, String>>(resultado)
        }
          return response!!
    }


    /**get user by id**/
    @GetMapping("/api/users/{id}")
    fun userById(@PathVariable("id") id: Int): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        try {
            val newUser = userService!!.findByID(id)
            val userview = UserViewMapper(
                newUser.id,
                newUser.name,
                newUser.surname,
                newUser.email,
                newUser.address,
                newUser.cvu,
                newUser.walletAddress
            )
            ResponseEntity.status(200)
            response = ResponseEntity.ok().body(userview)
        } catch (e: Exception) {
            ResponseEntity.status(404)
          val resultado: MutableMap<String, String> = HashMap()
            resultado["user with id not found"] = id.toString()
            response = ResponseEntity.ok().body<Map<String, String>>(resultado)
        }
    return response !!
    }



    /** Update*/
    @PutMapping("/api/users/{id}")
    fun update (@PathVariable("id") id: Int,@RequestBody entity: UserUpdateMapper): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        try {
            val newUser = userService!!.update(id,entity)
            val userview = UserViewMapper(
                newUser.id,
                newUser.name,
                newUser.surname,
                newUser.email,
                newUser.address,
                newUser.cvu,
                newUser.walletAddress
            )
            ResponseEntity.status(200)
            response = ResponseEntity.ok().body(userview)
        } catch (e: Exception) {
            ResponseEntity.status(404)

            val resultado: MutableMap<String, String> = HashMap()
            resultado["usuario con id no encontrado"] = id.toString()
            response = ResponseEntity.ok().body<Map<String, String>>(resultado)
        }
        return response !!
    }

    /**Delete user by id*/
    @DeleteMapping("/api/users/{id}")
    fun deleteUserById(@PathVariable("id") id: Int): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        try {
            userService!!.deleteById(id)
            val resultado: MutableMap<String, Int> = HashMap()
            resultado["succesfully user deleted with iD"] = id
            response = ResponseEntity.ok().body<Map<String, Int>>(resultado)

    } catch (e: Exception) {
        ResponseEntity.status(404)

            val resultado: MutableMap<String, String> = HashMap()
            resultado["user with id not found"] = id.toString()
            response = ResponseEntity.ok().body<Map<String, String>>(resultado)
    }
       return  response!!
    }


     /**version*/
    @get:ResponseBody
    @get:RequestMapping(value = ["/api/version"], method = [RequestMethod.GET])
    val version: ResponseEntity<*>
        get() {
            val version = "0.2.2"
            val resultado: MutableMap<String, String> = HashMap()
            resultado["version"] = version
            return ResponseEntity.ok().body<Map<String, String>>(resultado)
        }


}