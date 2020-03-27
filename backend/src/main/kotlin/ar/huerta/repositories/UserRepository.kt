/**
 *
 */
package ar.huerta.repositories

import ar.huerta.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * @author manuel
 */
@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findAllByActive(active: Boolean?): Set<User?>?
    fun findByUsernameAndActive(username: String?, active: Boolean): User?
    fun findByUsername(username: String?): Optional<User?>?
    fun findByUsernameAndEmailVerified(username: String?, verified: Boolean?): User?
    fun findUsersByRoles_RoleName(roleName: String?): List<User?>?
}