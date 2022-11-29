package ar.edu.unq.desapp.grupoL.criptop2p.service
/*
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import java.util.function.Function
import java.util.stream.Collectors

@Service
class JwtUtilService {

    private val JWT_SECRET_KEY = "TExBVkVfTVVZX1NFQ1JFVEE="

    val JWT_TOKEN_VALIDITY = 1000 * 60 * 60 * 8L // 8 Horas

    fun extractUsername(token: String?): String? {
        return extractClaim(token) { obj: Claims? -> obj!!.subject }
    }
    fun extractExpiration(token: String?): Date? {
        return extractClaim(token) { obj: Claims? -> obj!!.expiration }
    }

    fun <T> extractClaim(token: String?, claimsResolver: Function<Claims?, T>): T {
        return claimsResolver.apply(extractAllClaims(token))
    }

    private fun extractAllClaims(token: String?): Claims {
        return Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(token).getBody()
    }

    private fun isTokenExpired(token: String?): Boolean {
        return extractExpiration(token)!!.before(Date())
    }

    fun generateToken(userDetails: UserDetails): String {
        val claims: MutableMap<String, Any> = HashMap()
        // Agregando informacion adicional como "claim"
        val rol: GrantedAuthority = userDetails.getAuthorities().stream().collect(Collectors.toList()).get(0)
        claims["rol"] = rol
        return createToken(claims, userDetails.getUsername())
    }

    private fun createToken(claims: Map<String, Any>, subject: String): String {
        return Jwts
            .builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
            .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
            .compact()
    }

    fun validateToken(token: String?, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.getUsername() && !isTokenExpired(token)
    }
}

*/
