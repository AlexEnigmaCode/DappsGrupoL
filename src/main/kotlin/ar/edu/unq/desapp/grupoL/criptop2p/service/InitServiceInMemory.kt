package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.UserRegisterMapper
import ar.edu.unq.desapp.grupoL.criptop2p.model.User
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct

@Service
@Transactional
class InitServiceInMemory {
    protected val logger = LogFactory.getLog(javaClass)

    @Value("\${spring.datasource.driverClassName:NONE}")
    private val className: String? = null

    @Autowired
    private val userService: UserService? = null
    @PostConstruct
    fun initialize() {
        if (className == "org.h2.Driver") {
            logger.info("Init Data Using H2 DB")
            fireInitialData()
        }
    }

    private fun fireInitialData() {
        val user1 = UserRegisterMapper( "Ale", "Fariña", "ale@gmail.com", "address1","1", 123, 7  )
        userService!!.register(user1)
        val user2 = UserRegisterMapper( "Ulises", "Lopez","ulisese@gmail.com", "address2","2", 234, 8 )
        userService!!.register(user2)
    }
}