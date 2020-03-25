package ar.huerta.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "consortium.security")
public class SecurityConstants {
    public String SECRET = "5t6r7e8w¡?=)(/&%$#";
    public Long EXPIRATION_TIME_MS = 5_356_800_000l; // 86_400_000l; // 1 days
    public String TOKEN_PREFIX = "Bearer ";
    public String HEADER_STRING = "Authorization";
    public String SIGN_UP_URL = "/administrator/";
    public String LOGIN_CONTEXT_PATH = "/api/login";
    public String CONTROLLER_ONBOARDING = "/onboarding";
    public String CHECK_TOKEN_URL = "/checkToken";
}
