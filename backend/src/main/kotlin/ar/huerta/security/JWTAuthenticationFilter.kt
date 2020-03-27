package ar.huerta.security

import ar.huerta.config.SecurityConstants
import ar.huerta.entities.User
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import java.io.IOException
import java.util.*
import java.util.Map.of
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthenticationFilter(
        val authentication: AuthenticationManager,
        val constants: SecurityConstants
) : UsernamePasswordAuthenticationFilter() {

    private val mapper = ObjectMapper()

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(req: HttpServletRequest,
                                       res: HttpServletResponse): Authentication {
        return try {
            val user = ObjectMapper().readValue(req.inputStream, User::class.java)
            authentication.authenticate(
                    UsernamePasswordAuthenticationToken(
                            user.username,
                            user.password,
                            ArrayList())
            )
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    @Throws(IOException::class, ServletException::class)
    override fun unsuccessfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, failed: AuthenticationException) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        val message: MutableMap<String, String> = HashMap()
        message["status"] = "Invalid credentials"
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.writer.println(mapper.writeValueAsString(message))
    }

    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(req: HttpServletRequest,
                                          res: HttpServletResponse,
                                          chain: FilterChain,
                                          auth: Authentication) {

        val principal = auth.principal as UserDetails
        val roles = principal.authorities.map { grant -> grant.authority }.toTypedArray()
        val token = JWT.create()
                .withSubject(principal.username)
                .withArrayClaim("roles", roles)
                .withExpiresAt(Date(System.currentTimeMillis() + constants.EXPIRATION_TIME_MS))
                .sign(Algorithm.HMAC512(constants.SECRET.toByteArray()))
        res.contentType = MediaType.APPLICATION_JSON_VALUE
        val jsonToken = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                mapOf("token" to constants.TOKEN_PREFIX + token)
        )
        res.writer.println(jsonToken)
        res.addHeader(constants.HEADER_STRING, constants.TOKEN_PREFIX + token)
    }

    init {
        setRequiresAuthenticationRequestMatcher(
                AntPathRequestMatcher(constants.LOGIN_CONTEXT_PATH, "POST")
        )
    }
}