package ar.huerta.security

import ar.huerta.config.SecurityConstants
import ar.huerta.exceptions.ServiceException
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.TokenExpiredException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthorizationFilter(authManager: AuthenticationManager?, @field:Autowired private val constants: SecurityConstants) : BasicAuthenticationFilter(authManager) {
    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(req: HttpServletRequest,
                                  res: HttpServletResponse,
                                  chain: FilterChain) {

        val header = req.getHeader(constants.HEADER_STRING)
        if (header == null || !header.startsWith(constants.TOKEN_PREFIX)) {
            chain.doFilter(req, res)
            return
        }
        val authentication = getAuthentication(req)
        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(req, res)
    }

    private fun getAuthentication(request: HttpServletRequest): UsernamePasswordAuthenticationToken? {
        val token = request.getHeader(constants.HEADER_STRING)
        if (token != null) {
            try {
                // parse the token.
                val decodedJWT = JWT.require(Algorithm.HMAC512(constants.SECRET.toByteArray()))
                        .build()
                        .verify(token.replace(constants.TOKEN_PREFIX, ""))
                val user = decodedJWT
                        .subject
                val roles = decodedJWT.claims["roles"]!!.asList(SimpleGrantedAuthority::class.java)
                if (user != null) {
                    return UsernamePasswordAuthenticationToken(user, null, roles)
                }
            } catch (e: TokenExpiredException) {
                throw ServiceException(629, "La sesión a expirado. Vuelva a iniciar sesión")
            }
            return null
        }
        return null
    }

}