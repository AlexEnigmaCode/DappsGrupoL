package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.UserNotFoundException
import ar.edu.unq.desapp.grupoL.criptop2p.UserRegisterMapper
import ar.edu.unq.desapp.grupoL.criptop2p.UserUpdateMapper
import ar.edu.unq.desapp.grupoL.criptop2p.UsernameExistException
import ar.edu.unq.desapp.grupoL.criptop2p.model.Usuario
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
    private var userId = 0

    @Autowired
    private  lateinit var repository: UserRepository

    @Autowired
    private lateinit var encoder: BCryptPasswordEncoder

    @Transactional
    fun register(user: UserRegisterMapper): Usuario {

       if ( existUser(user) )  {
         throw UsernameExistException("User with email:  ${user.email} is used")
        }
        val password =encoder.encode(user.password)
       //val newUser = Usuario(++userId,user.name, user.surname, user.email,user.address,user.password,user.cvu,user.walletAddress)
        val newUser = Usuario(++userId,user.name, user.surname, user.email,user.address,password,user.cvu,user.walletAddress)
        return  repository.save(newUser)
      }

    fun login(email: String, password: String): Usuario {

        val users = repository.findAll()
       // return users.find { (it.email == email) && (it.password == password) } ?: throw UserNotFoundException("Not found user")
        return users.find { (it.email == email)  } ?: throw UserNotFoundException("Not found user")
    }

    @Transactional
    fun findByID(id: Int): Usuario {
       val user =  repository.findById(id)
       if ( ! (user.isPresent ))
       {throw UserNotFoundException("User with Id:  $id not found") }
       return user.get()
    }

    @Transactional
    fun deleteById(id: Int) {
        val user =  repository.findById(id)
        if ( ! (user.isPresent ))
        {throw UserNotFoundException("User with Id:  $id not found") }
       repository.deleteById(id)

    }
/*
    @Transactional
    fun update(id: Int , entity: UserUpdateMapper) : User{
        val user =  repository.findById(id)
        if ( ! (user.isPresent ))
        {throw UserNotFoundException("User with Id:  $id not found") }
        var newUser :User  =  user.get()
          newUser = User(newUser.id,newUser.name, newUser.surname, entity.email,entity.adress,entity.password,entity.cvu,entity.walletAdress)
          repository.save(newUser)
        return  repository.findById(id).get()
    }
*/


    @Transactional
    fun update(id: Int , entity: UserUpdateMapper) : Usuario {
       lateinit var   entityOptional:Optional<Usuario?>
        try {
              entityOptional = repository.findById(id)
             val  newUser:Usuario = entityOptional.get()
            val password =encoder.encode(entity.password)
            val user = Usuario(newUser.id,newUser.name, newUser.surname, entity.email,entity.address,password,entity.cvu,entity.walletAddress)
            repository.deleteById(id)
             return  repository.save(user)

            }
        catch (e:Exception){
             throw UserNotFoundException("User with Id:  $id not found")
          //throw  Exception (e.message)

    }
}

    @Transactional
    fun findAll(): List<Usuario> {

        return repository.findAll()
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
      val user : Usuario = users.find { (it.name == username)  } ?: throw UserNotFoundException("Not found user")
       var  roles : List<GrantedAuthority> = listOf()
        roles.toMutableList().add (SimpleGrantedAuthority("ADMIN") )

       val userDet :UserDetails = User(user.name, user.password, roles)
       return userDet
            
    }

}