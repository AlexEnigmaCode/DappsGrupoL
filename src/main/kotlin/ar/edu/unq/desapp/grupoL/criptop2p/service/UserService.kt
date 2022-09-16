package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.UserRegisterMapper
import ar.edu.unq.desapp.grupoL.criptop2p.UsernameExistException
import ar.edu.unq.desapp.grupoL.criptop2p.model.User
import org.springframework.beans.factory.annotation.Autowired
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.UserRepository
import org.omg.CORBA.UserException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService {
    private var userId = 0
    val users = mutableListOf<User>()

    @Autowired
    private val repository: UserRepository? = null
    @Transactional
    fun register(user: UserRegisterMapper): User {
     if (existUser(user)) {
         throw UsernameExistException("User with email:  ${user.email} is used")
        }
        val newUser = User(++userId,user.name, user.surname, user.email,user.adress,user.password,user.cvu,user.walletAdress)
        return repository!!.save(newUser)
    }


    fun login(email: String, password: String): User {
        val users = repository!!.findAll()
        return users.find { (it!!.email == email) && (it!!.password == password) } ?: throw UsernameExistException("Not found user")
    }


    fun findByID(id: Int?): User {
        return repository!!.findById(id!!).get()
    }

    fun deleteById(id: Int?) {
        repository!!.deleteById(id!!)
    }

    @Transactional
    fun findAll(): List<User?> {
        return repository!!.findAll()
    }

    private fun existUser(user: UserRegisterMapper): Boolean {
        val users = repository!!.findAll()
        return users.any { it?.email == user.email }
    }
}