package ar.huerta.config

import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.format.datetime.DateFormatterRegistrar
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration //@EnableWebMvc
class WebConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**").allowedOrigins("localhost:4200")
    }

    override fun addFormatters(registry: FormatterRegistry) {
        DateTimeFormatterRegistrar().registerFormatters(registry)
        DateFormatterRegistrar().registerFormatters(registry)
    }
}