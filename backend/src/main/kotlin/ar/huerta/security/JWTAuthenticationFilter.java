package ar.huerta.security;

import ar.huerta.config.SecurityConstants;
import ar.huerta.entities.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private SecurityConstants constants;
    private ObjectMapper mapper = new ObjectMapper();


    public JWTAuthenticationFilter(
            AuthenticationManager authenticationManager,
            SecurityConstants constants
    ) {
        this.authenticationManager = authenticationManager;
        this.constants = constants;
        this.setRequiresAuthenticationRequestMatcher(
                new AntPathRequestMatcher(this.constants.LOGIN_CONTEXT_PATH, "POST")
        );
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            User creds = new ObjectMapper()
                    .readValue(req.getInputStream(), User.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, String> message = new HashMap<>();
        message.put("status", "Invalid credentials");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println(mapper.writeValueAsString(message));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        UserDetails principal = (UserDetails) auth.getPrincipal();
        String[] roles = principal.getAuthorities().stream().map(r -> r.getAuthority())
                .toArray(String[]::new);

        String token = JWT.create()
                .withSubject(principal.getUsername())
                .withArrayClaim("roles", roles)
                .withExpiresAt(new Date(System.currentTimeMillis() + constants.EXPIRATION_TIME_MS))
                .sign(Algorithm.HMAC512(constants.SECRET.getBytes()));

        res.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String jsonToken = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                Map.of("token", constants.TOKEN_PREFIX + token)
        );

        res.getWriter().println(jsonToken);
        res.addHeader(constants.HEADER_STRING, constants.TOKEN_PREFIX + token);
    }
}
