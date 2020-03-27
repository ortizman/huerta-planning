package ar.huerta.controllers

import ar.huerta.security.SecurityUtils
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("\${rest.context-path}/token")
class TokenController {

    @PostMapping("/revoke")
    fun revokeToken() {
        val authentication = SecurityContextHolder.getContext().authentication
        LOGGER.info("Se intenta revokar el token. User: {}", authentication.principal)
    }

    @GetMapping("/roles")
    fun roles(): Array<String> {
        return SecurityUtils.userLoggedRoles
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(TokenController::class.java)
    }
}