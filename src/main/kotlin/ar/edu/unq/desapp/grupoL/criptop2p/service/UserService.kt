package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.*
import ar.edu.unq.desapp.grupoL.criptop2p.model.Usuario
import ar.edu.unq.desapp.grupoL.criptop2p.model.UsuarioMapper
import org.springframework.beans.factory.annotation.Autowired
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service
class UserService: UserDetailsService {
   // private var userId :Int= 0

    @Autowired
    private  lateinit var repository: UserRepository

    @Autowired
    private lateinit var encoder: BCryptPasswordEncoder

   @Autowired
   private lateinit var usuarioMapper: UsuarioMapper
   // private var usuarioMapper = UsuarioMapper.instancia()


    @Transactional
    fun register(user: UserRegisterMapper): UserViewMapper {

        lateinit var  userview : UserViewMapper
       if ( existUser(user) )  {
         throw UsernameExistException("User with email:  ${user.email} is used")
        }
        val password =encoder.encode(user.password)
       // val newuser = usuarioMapper.aModelo(user)
       // val newUser = Usuario(++userId,user.name, user.surname, user.email,user.address,user.password,user.cvu,user.walletAddress)
      val newUser = Usuario(0,user.name, user.surname, user.email,user.address,password,user.cvu,user.walletAddress)

      // try {
            val savedUser = repository.save(newUser)
           // savedUser.validar()

                userview = UserViewMapper(
                savedUser.id,
                savedUser.name,
                savedUser.surname,
                savedUser.email,
                savedUser.address,
                savedUser.cvu,
                savedUser.walletAddress
            )
       // }
/*
        catch (e: Exception) {

            val resultado: MutableMap<String, String> = HashMap()
            resultado["Object invalid"] = e.message.toString()

        }
  */
        return  userview
      }




    @Transactional
    fun login(email: String, password: String): UserViewMapper {

       val users = repository.findAll()
       // return users.find { (it.email == email) && (it.password == password) } ?: throw UserNotFoundException("Not found user")
        val newUser = users.find { (it.email == email)  } ?: throw ItemNotFoundException("Not found user")
        val userview = UserViewMapper(newUser.id,newUser.name,newUser.surname,newUser.email,newUser.address,newUser.cvu,newUser.walletAddress)
        return userview
    }

    @Transactional
    fun findByID(id: Long): Usuario {
       val user =  repository.findById(id)
       if ( ! (user.isPresent ))
       {throw ItemNotFoundException("User with Id:  $id not found") }
       val newUser=  user.get()
       return newUser

       }




    @Transactional
    fun deleteById(id: Long) {
        val user =  repository.findById(id)
        if ( ! (user.isPresent ))
        {throw ItemNotFoundException("User with Id:  $id not found") }
       repository.deleteById(id)

    }



    @Transactional
    fun update(id: Long, entity: UserUpdateMapper) : UserViewMapper {
       lateinit var   entityOptional:Optional<Usuario?>
        try {
              entityOptional = repository.findById(id)
             val  newUser:Usuario = entityOptional.get()
            val password =encoder.encode(entity.password)
            val user = Usuario(newUser.id,newUser.name, newUser.surname, entity.email,entity.address,password,entity.cvu,entity.walletAddress)
           // repository.deleteById(id)
            val savedUser = repository.save(user)
            val userView =   UserViewMapper(savedUser.id, savedUser.name, savedUser.surname, savedUser.email, savedUser.address, savedUser.cvu, savedUser.walletAddress)
             return userView
            }
        catch (e:Exception){
             throw ItemNotFoundException("User with Id:  $id not found")


    }
}

    @Transactional
    fun findAll(): List<UserViewMapper> {
     val list =  repository.findAll()
     val users =  list.map { UserViewMapper(it.id, it.name, it.surname, it.email, it.address, it.cvu, it.walletAddress) }
     return users
    }



    private fun existUser(user: UserRegisterMapper): Boolean {
        var bool = false
        val users = repository.findAll().toMutableList()
        if ( users.isNotEmpty() ) {
        bool =  users.any { it.email == user.email }
        }
        return bool
    }

    override fun loadUserByUsername(username: String?): UserDetails {
       val  users : List<Usuario> = repository.findAll()
      val user : Usuario = users.find { (it.name == username)  } ?: throw ItemNotFoundException("Not found user")
       var  roles : List<GrantedAuthority> = listOf()
        roles.toMutableList().add (SimpleGrantedAuthority("ADMIN") )

       val userDet :UserDetails = User(user.name, user.password, roles)
       return userDet
            
    }

}