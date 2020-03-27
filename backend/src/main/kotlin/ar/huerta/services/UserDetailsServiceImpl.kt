package ar.huerta.services

import ar.huerta.repositories.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class UserDetailsServiceImpl : UserDetailsService {

    @Autowired
    private lateinit var userRepository: UserRepository;

    private val LOGGER: Logger = LoggerFactory.getLogger(UserDetailsServiceImpl::class.java)


    @Transactional
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user: ar.huerta.entities.User?;
        try {
            user = userRepository.findByUsernameAndActive(username, true)
            val roles = user?.roles?.map { it -> SimpleGrantedAuthority(it.roleName) };
            return User(user?.username, user?.password, roles)
        } catch (e: Exception) {
            LOGGER.error("Ocurrio un error al recuperar el usuario de la base de datos", e);
            throw e;
        }

    }
}