/**
 *
 */
package ar.huerta.entities

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

/**
 * @author manuel
 */
@Entity
@Table(
        uniqueConstraints = [
            UniqueConstraint(
                    columnNames = ["username"],
                    name = "unique_username")
        ],
        indexes = [Index(name = "usernameIndex", columnList = "username")]
)
data class User (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    val active: Boolean = false,

    val username: String? = null,

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    val password: String? = null,

    val emailVerified: Boolean = false,

    @ManyToMany
    val roles: Set<Role>? = null
)