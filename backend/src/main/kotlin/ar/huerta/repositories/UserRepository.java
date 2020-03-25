/**
 *
 */
package ar.huerta.repositories;

import ar.huerta.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author manuel
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Set<User> findAllByActive(Boolean active);

	User findByUsernameAndActive(String username, boolean active);

    Optional<User> findByUsername(String username);

    User findByUsernameAndEmailVerified(String username, Boolean verified);

    List<User> findUsersByRoles_RoleName(String roleName);
}
