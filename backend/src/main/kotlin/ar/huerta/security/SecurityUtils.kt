package ar.huerta.security

import ar.huerta.exceptions.ForbiddenException
import ar.huerta.exceptions.ServiceException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder

object SecurityUtils {
    val username: String
        get() {
            val authentication = SecurityContextHolder.getContext().authentication
            val principal = authentication!!.principal as String
            if (authentication == null || principal == null) {
                throw ServiceException(200, "No se pudo determinar el usuario")
            }
            return principal
        }

    val userLoggedRoles: Array<String>
        get() {
            val authentication = SecurityContextHolder.getContext().authentication
            val authorities = authentication.authorities
            if (authorities == null || authorities.isEmpty()) {
                throw ForbiddenException()
            }
            return authorities.stream().map { r: GrantedAuthority -> r.authority }.toArray {  arrayOf<String>()}
        }
}