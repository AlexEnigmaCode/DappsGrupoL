package ar.edu.unq.desapp.grupoL.criptop2p.webservice

import ar.edu.unq.desapp.grupoL.criptop2p.UserLoginMapper
import ar.edu.unq.desapp.grupoL.criptop2p.UserRegisterMapper
import ar.edu.unq.desapp.grupoL.criptop2p.UserUpdateMapper
import ar.edu.unq.desapp.grupoL.criptop2p.UserViewMapper
//import ar.edu.unq.desapp.grupoL.criptop2p.service.JwtUtilService
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.beans.factory.annotation.Autowired
import ar.edu.unq.desapp.grupoL.criptop2p.service.UserService
//import ar.edu.unq.desapp.grupoL.criptop2p.service.filter.JwtRequestFilter
import io.jsonwebtoken.Jwts
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
//import org.springframework.security.core.userdetails.UserDetails
//import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.*
import java.nio.file.Files.setAttribute
import java.util.HashMap
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController
@EnableAutoConfiguration
@CrossOrigin("*")

class UserRestService {
    @Autowired
    private  lateinit var  userService: UserService
    private val builder: ResponseEntity.BodyBuilder? = null

  /*
    @Autowired
    private lateinit var jwtuUtilSerice : JwtUtilService

    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    @Autowired
    private lateinit var  jutRequestFilter: JwtRequestFilter

*/

    @GetMapping("/api/users")
    fun allUsers(): ResponseEntity<*>{
       val users = userService.findAll()
       return ResponseEntity.ok().body(users)

    }


    /**register a user*/
    @PostMapping("/api/register")
    fun register(/*@Valid*/ @RequestBody user: UserRegisterMapper): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        try {
           val  savedUser = userService.register(user)
           val  userview = UserViewMapper(
                savedUser.id,
                savedUser.name,
                savedUser.surname,
                savedUser.email,
                savedUser.address,
                savedUser.cvu,
                savedUser.walletAddress,
                savedUser.cantidadOperaciones,
                savedUser.reputacion)
            ResponseEntity.status(201)
            response =  ResponseEntity.ok().body(userview)

        } catch (e: Exception) {
            ResponseEntity.status(404)

            val resultado: MutableMap<String, String> = HashMap()
            resultado["error:"] =  e.message.toString() //user.email.toString()
            response = ResponseEntity.ok().body<Map<String, String>>(resultado)
        }

       return response!!
    }

    /**login a user*/
    @PostMapping("/api/login")
    fun login(@RequestBody user: UserLoginMapper): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        try {
            val userview = userService.login(user.email, user.password)
       //     val userDetails: UserDetails = userDetailsService.loadUserByUsername(userview.name)
     //       val token = jwtuUtilSerice.generateToken(userDetails)
   //         jutRequestFilter.filterConfig!!.servletContext.setAttribute("Authorization", token)
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
    fun userById(@PathVariable("id") id: Long): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        try {
            val newUser = userService.findByID(id)
            val userView =   UserViewMapper(newUser.id, newUser.name, newUser.surname, newUser.email, newUser.address, newUser.cvu, newUser.walletAddress,newUser.cantidadOperaciones,newUser.reputacion)

            ResponseEntity.status(200)
            response = ResponseEntity.ok().body(userView)
        } catch (e: Exception) {
            ResponseEntity.status(404)
          val resultado: MutableMap<String, String> = HashMap()
            resultado["user with id not found"] = id.toString()
            response = ResponseEntity.ok().body<Map<String, String>>(resultado)
        }
    return response !!
    }



    /** Update*/
    @PutMapping("/api/users/update/{id}")
    fun update (@PathVariable("id") id: Long,@RequestBody entity: UserUpdateMapper): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        try {
            val userview = userService.update(id,entity)

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
    @DeleteMapping("/api/users/delete/{id}")
    fun deleteUserById(@PathVariable("id") id: Long): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        try {
            userService!!.deleteById(id)
            val resultado: MutableMap<String, Int> = HashMap()
             resultado["succesfully user deleted with iD"] = id.toInt()
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