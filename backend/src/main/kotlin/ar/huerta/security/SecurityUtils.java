package ar.huerta.security;

import ar.huerta.exceptions.ForbiddenException;
import ar.huerta.exceptions.ServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

public class SecurityUtils {

    public static String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String principal = (String) authentication.getPrincipal();
        if (authentication == null || principal == null) {
            throw new ServiceException(200, "No se pudo determinar el usuario");
        }

        return principal;
    }

    public static String[] getUserLoggedRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        if (authorities == null || authorities.isEmpty()) {
            throw new ForbiddenException();
        }

        return authorities.stream().map(r -> r.getAuthority()).toArray(String[]::new);
    }

}
