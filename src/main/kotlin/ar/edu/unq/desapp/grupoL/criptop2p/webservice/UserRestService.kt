package ar.edu.unq.desapp.grupoL.criptop2p.webservice


import ar.edu.unq.desapp.grupoL.criptop2p.UserLoginMapper
import ar.edu.unq.desapp.grupoL.criptop2p.UserRegisterMapper
import ar.edu.unq.desapp.grupoL.criptop2p.UserUpdateMapper
import ar.edu.unq.desapp.grupoL.criptop2p.UserViewMapper
import ar.edu.unq.desapp.grupoL.criptop2p.model.AuthenticationReq
import ar.edu.unq.desapp.grupoL.criptop2p.model.TokenInfo
import ar.edu.unq.desapp.grupoL.criptop2p.service.JwtUtilService
import ar.edu.unq.desapp.grupoL.criptop2p.service.UserService
import ar.edu.unq.desapp.grupoL.criptop2p.service.filter.JwtRequestFilter
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.*



@RestController
@EnableAutoConfiguration
//@CrossOrigin("*")
@RequestMapping("")



    class UserRestService {

    @Autowired
    lateinit var userService: UserService

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private lateinit var jwtuUtilSerice: JwtUtilService

    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    @Autowired
    private lateinit var jwtRequestFilter: JwtRequestFilter

    private val builder: ResponseEntity.BodyBuilder? = null


    companion object {
        var logger = LoggerFactory.getLogger(UserRestService::class.java)
    }


    @PostMapping("/publico/api/post")
    fun autenticate(@RequestBody user: UserLoginMapper): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        try {
            val userview = userService.login(user.email, user.password)
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                   userview.name,
                   user.password
                )
            )

            val userDetails: UserDetails = userDetailsService.loadUserByUsername(userview.name)
            val jwtToken = jwtuUtilSerice.generateToken(userDetails)
            //         jutRequestFilter.filterConfig!!.servletContext.setAttribute("Authorization", token)
            val tokenInfo = TokenInfo(jwtToken)
            ResponseEntity.status(200)
            // response = ResponseEntity.ok().body(userview)
            response = ResponseEntity.ok().body(tokenInfo)
        }
        catch (e:Exception) {
            ResponseEntity.status(404)
            val resultado: MutableMap<String, String> = HashMap()
            resultado["error"] =  e.message.toString()
            response = ResponseEntity.ok().body<Map<String, String>>(resultado)
        }
        return response!!
    }

    @GetMapping("/publico/api/users")
    fun allUsers(): ResponseEntity<*>{
       val users = userService.findAll()
       return ResponseEntity.ok().body(users)

    }


    /**register a user*/
    @PostMapping("/publico/api/register")
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
    @PostMapping("/publico/api/login")
    fun login(@RequestBody user: UserLoginMapper): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        try {
            val userview = userService.login(user.email, user.password)

           ResponseEntity.status(200)
           response = ResponseEntity.ok().body(userview)

        }
        catch (e:Exception) {
            ResponseEntity.status(404)
           val resultado: MutableMap<String, String> = HashMap()
            resultado["error"] =  e.message.toString()
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
           // resultado["usuario con id no encontrado"] = id.toString()
            resultado["error"] = e.message.toString()
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