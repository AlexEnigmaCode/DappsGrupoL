package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.*
import ar.edu.unq.desapp.grupoL.criptop2p.model.Usuario
import ar.edu.unq.desapp.grupoL.criptop2p.model.UsuarioMapper
import org.springframework.beans.factory.annotation.Autowired
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.UserRepository
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service
class UserService{

    @Autowired
    private val repository: UserRepository ? = null

   // @Autowired
   // private  var encoder : BCryptPasswordEncoder = BCryptPasswordEncoder()

 // @Autowired
 // private lateinit var usuarioMapper: UsuarioMapper
    private var usuarioMapper = UsuarioMapper.instancia()


    @Transactional
    fun register(user: UserRegisterMapper): Usuario {

        if ( existEmail(user.email!!) )  {
         throw UsernameExistException("User with email:  ${user.email} is used")
        }
       val  password = user.password!!
       // val password =encoder.encode(user.password)
       // val newuser = usuarioMapper.aModelo(user)

      val newUser = Usuario(0,user.name, user.surname, user.email,user.address,password,user.cvu,user.walletAddress,0, 0.0)
      val savedUser = repository!!.save(newUser)
       // savedUser.validar()
        return  savedUser
      }




    @Transactional
    fun login(email: String, password: String): UserViewMapper {

       val users = repository!!.findAll()
        val newUser = users.find { (it.email == email)  && (it.password == password) } ?: throw ItemNotFoundException("Not found user")
        val userview = UserViewMapper(newUser.id,newUser.name,newUser.surname,newUser.email,newUser.address,newUser.cvu,newUser.walletAddress,newUser.cantidadOperaciones,newUser.reputacion)
        return userview
    }

    @Transactional
    fun findByID(id: Long): Usuario {
       val user =  repository!!.findById(id)
       if ( ! (user.isPresent ))
       {throw ItemNotFoundException("User with Id:  $id not found") }
       val newUser=  user.get()
       return newUser

       }




    @Transactional
    fun deleteById(id: Long) {
        val user =  repository!!.findById(id)
        if ( ! (user.isPresent ))
        {throw ItemNotFoundException("User with Id:  $id not found") }
       repository.deleteById(id)

    }



    @Transactional
    fun update(id: Long, entity: UserUpdateMapper) : UserViewMapper {
      if ( existEmail(entity.email!!) )  {
                throw UsernameExistException("User with email:  ${entity.email} is used")
            }
        val  newUser:Usuario = findByID(id)
        newUser.password = entity.password
        newUser.email = entity.email
        newUser.address = entity.address
        newUser.cvu =  entity.cvu
        newUser.walletAddress  = entity.walletAddress
            val savedUser = repository!!.save(newUser)
            val userView =   UserViewMapper(savedUser.id, savedUser.name, savedUser.surname, savedUser.email, savedUser.address, savedUser.cvu, savedUser.walletAddress, savedUser.cantidadOperaciones, savedUser.reputacion)
             return userView

}

    @Transactional
    fun findAll(): List<UserViewMapper> {
     val list =  repository!!.findAll()
     val users =  list.map { UserViewMapper(it.id, it.name, it.surname, it.email, it.address, it.cvu, it.walletAddress, it.cantidadOperaciones, it.reputacion) }
     return users
    }

    @Transactional
    fun listadoInformeUsuarios(): List<InformeUsuarioMapper> {
        val list =  repository!!.findAll()
        val users =  list.map { InformeUsuarioMapper (it.name!!, it.surname!!,it.cantidadOperaciones, it.reputacion) }
        return users
    }




    private fun existEmail(email: String): Boolean {
       val users = repository!!.findAll().toMutableList()
        if ( users.isNotEmpty() ) {
            return  users.any { it.email == email }
        }
        return false
    }


}