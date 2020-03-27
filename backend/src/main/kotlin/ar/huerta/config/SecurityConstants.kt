package ar.huerta.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "huerta.security")
class SecurityConstants {
    var SECRET = "5t6r7e8w¡?=)(/&%$#"
    var EXPIRATION_TIME_MS = 5356800000L // 86_400_000l; // 1 days
    var TOKEN_PREFIX = "Bearer "
    var HEADER_STRING = "Authorization"
    var SIGN_UP_URL = "/administrator/"
    var LOGIN_CONTEXT_PATH = "/api/login"
    var CONTROLLER_ONBOARDING = "/onboarding"
    var CHECK_TOKEN_URL = "/checkToken"
}