package ar.huerta.controllers;

import ar.huerta.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${rest.context-path}/token")
public class TokenController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenController.class);

    @PostMapping("/revoke")
    public void revokeToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LOGGER.info("Se intenta revokar el token. User: {}", authentication.getPrincipal());
    }

    @GetMapping("/roles")
    public String[] roles() {
        return SecurityUtils.getUserLoggedRoles();
    }
}
