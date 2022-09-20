package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.UserNotFoundException
import ar.edu.unq.desapp.grupoL.criptop2p.UserRegisterMapper
import ar.edu.unq.desapp.grupoL.criptop2p.UserUpdateMapper
import ar.edu.unq.desapp.grupoL.criptop2p.UsernameExistException
import ar.edu.unq.desapp.grupoL.criptop2p.model.User
import org.springframework.beans.factory.annotation.Autowired
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class UserService {
    private var userId = 0

    @Autowired
    private  lateinit var repository: UserRepository

    @Transactional
    fun register(user: UserRegisterMapper): User {

       if ( existUser(user) )  {
         throw UsernameExistException("User with email:  ${user.email} is used")
        }
       val newUser = User(++userId,user.name, user.surname, user.email,user.adress,user.password,user.cvu,user.walletAdress)
       return  repository.save(newUser)
      }

    fun login(email: String, password: String): User {

        val users = repository.findAll()
        return users.find { (it.email == email) && (it.password == password) } ?: throw UserNotFoundException("Not found user")
    }

    @Transactional
    fun findByID(id: Int): User {
       val user =  repository.findById(id)
       if ( ! (user.isPresent ))
       {throw UserNotFoundException("User with Id:  $id not found") }
       return user.get()
    }

    @Transactional
    fun deleteById(id: Int) {
       repository.deleteById(id)

    }

    @Transactional
    fun update(id: Int , entity: UserUpdateMapper) : User{
        val user =  repository.findById(id)
        if ( ! (user.isPresent ))
        {throw UserNotFoundException("User with Id:  $id not found") }
        var newUser :User  =  user.get()
          newUser = User(newUser.id,newUser.name, newUser.surname, entity.email,entity.adress,entity.password,entity.cvu,entity.walletAdress)
         return  repository.save(newUser)
    }


    @Transactional
    fun findAll(): List<User> {
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

}