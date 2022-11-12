package ar.edu.unq.desapp.grupoL.criptop2p
/*
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.beans.factory.annotation.Autowiredimport ar.edu.unq.desapp.grupoL.criptop2p.service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import kotlin.Throws
import java.lang.Exception
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
*/
/*
@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {
   // @Autowired
    private val userDetailsService: UserService = UserService()

    @Autowired
    private val bCrypt: BCryptPasswordEncoder? = null
    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        // auth.userDetailsService(auth.getDefaultUserDetailsService())
      //  auth.userDetailsService(userDetailsService).passwordEncoder(bCrypt)
    }
*//*
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .authorizeHttpRequests()
            .anyRequest()
            .authenticated()
            .and()
            .httpBasic()
    }

}

*/