/**
 *
 */
package ar.huerta.entities

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

/**
 * @author manuel
 */
@Entity
data class Role (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val roleName: String? = null,
    val description: String? = null
)