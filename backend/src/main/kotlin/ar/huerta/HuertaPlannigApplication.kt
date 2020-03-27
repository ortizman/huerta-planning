package ar.huerta

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.ComponentScans
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled=true)
@EnableJpaRepositories(basePackages = ["ar.huerta.repositories"])
@EntityScan("ar.huerta.entities")
@EnableTransactionManagement
class HuertaPlannigApplication

fun main(args: Array<String>) {
	runApplication<HuertaPlannigApplication>(*args)
}
