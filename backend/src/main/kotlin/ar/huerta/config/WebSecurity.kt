package ar.huerta.config

import ar.huerta.security.JWTAuthenticationFilter
import ar.huerta.security.JWTAuthorizationFilter
import ar.huerta.services.UserDetailsServiceImpl
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@EnableWebSecurity
class WebSecurity(
        private val userDetailsService: UserDetailsServiceImpl,
        private val bCryptPasswordEncoder: BCryptPasswordEncoder,
        private val constants: SecurityConstants) : WebSecurityConfigurerAdapter() {

    @Value("\${rest.context-path}")
    private val contextName: String? = null

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, constants.SIGN_UP_URL).permitAll() // Onboarding
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers(HttpMethod.GET, contextName + constants.CONTROLLER_ONBOARDING + constants.CHECK_TOKEN_URL).permitAll()
                .antMatchers(HttpMethod.GET, contextName + constants.CONTROLLER_ONBOARDING + "/{\\d+}").permitAll()
                .antMatchers(HttpMethod.POST, contextName + constants.CONTROLLER_ONBOARDING + "/activate").permitAll()
                .antMatchers("/admin/**")
                .fullyAuthenticated()
                .antMatchers(contextName + "/**").authenticated()
                .and()
                .addFilter(JWTAuthenticationFilter(authenticationManager(), constants))
                .addFilter(JWTAuthorizationFilter(authenticationManager(), constants)) // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    @Throws(Exception::class)
    public override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder)
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", CorsConfiguration().applyPermitDefaultValues())
        return source
    }

}