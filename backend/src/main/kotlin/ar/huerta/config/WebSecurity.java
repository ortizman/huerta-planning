package ar.huerta.config;

import ar.huerta.security.JWTAuthenticationFilter;
import ar.huerta.security.JWTAuthorizationFilter;
import ar.huerta.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private SecurityConstants constants;
    private UserDetailsServiceImpl userDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${rest.context-path}")
    private String contextName;

    public WebSecurity(UserDetailsServiceImpl userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder, SecurityConstants constants) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.constants = constants;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors().and().csrf().disable().authorizeRequests()
            .antMatchers(HttpMethod.POST, constants.SIGN_UP_URL).permitAll()

            // Onboarding
            .antMatchers(HttpMethod.GET, this.contextName + constants.CONTROLLER_ONBOARDING + constants.CHECK_TOKEN_URL).permitAll()
            .antMatchers(HttpMethod.GET, this.contextName + constants.CONTROLLER_ONBOARDING + "/{\\d+}").permitAll()
            .antMatchers(HttpMethod.POST, this.contextName + constants.CONTROLLER_ONBOARDING + "/activate").permitAll()

            .antMatchers("/admin/**")
            .fullyAuthenticated()
            .antMatchers(this.contextName + "/**").authenticated()
            .and()
            .addFilter(new JWTAuthenticationFilter(authenticationManager(), constants))
            .addFilter(new JWTAuthorizationFilter(authenticationManager(), constants))
            // this disables session creation on Spring Security
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

}
