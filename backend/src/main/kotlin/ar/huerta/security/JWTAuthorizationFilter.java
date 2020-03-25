package ar.huerta.security;

import ar.huerta.config.SecurityConstants;
import ar.huerta.exceptions.ServiceException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    @Autowired
    private SecurityConstants constants;

    public JWTAuthorizationFilter(AuthenticationManager authManager, SecurityConstants constants) {
        super(authManager);
        this.constants = constants;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {

        String header = req.getHeader(constants.HEADER_STRING);

        if (header == null || !header.startsWith(constants.TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(constants.HEADER_STRING);
        if (token != null) {
            try {
                // parse the token.
                DecodedJWT decodedJWT = JWT.require(HMAC512(constants.SECRET.getBytes()))
                        .build()
                        .verify(token.replace(constants.TOKEN_PREFIX, ""));
                String user = decodedJWT
                        .getSubject();
                final var roles = decodedJWT.getClaims().get("roles").asList(SimpleGrantedAuthority.class);
                if (user != null) {
                    return new UsernamePasswordAuthenticationToken(user, null, roles);
                }
            } catch (TokenExpiredException e) {
                throw new ServiceException(629, "La sesión a expirado. Vuelva a iniciar sesión");
            }

            return null;
        }
        return null;
    }
}
