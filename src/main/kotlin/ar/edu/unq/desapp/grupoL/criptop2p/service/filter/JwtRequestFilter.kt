package ar.edu.unq.desapp.grupoL.criptop2p.service.filter

import ar.edu.unq.desapp.grupoL.criptop2p.service.JwtUtilService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class JwtRequestFilter : OncePerRequestFilter() {
    @Autowired
    private val userDetailsService: UserDetailsService? = null

    @Autowired
    private val jwtUtilService: JwtUtilService? = null
    @Throws(ServletException::class, IOException::class)
    protected override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        val authorizationHeader: String = request.getHeader("Authorization")
        var username: String? = null
        var jwtToken: String? = null
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7)
            username = jwtUtilService?.extractUsername(jwtToken)
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            val userDetails: UserDetails = userDetailsService!!.loadUserByUsername(username)
            if (jwtUtilService!!.validateToken(jwtToken, userDetails)) {
                val authenticationToken = UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
                )
                authenticationToken.setDetails(WebAuthenticationDetailsSource().buildDetails(request))
                SecurityContextHolder.getContext().setAuthentication(authenticationToken)
            }
        }
        chain.doFilter(request, response)
    }
}