package ar.huerta.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*
import java.io.Serializable


/**
 * Controller generico que expone las operaciones mas comunes de una entidad
 *
 * @author Manuel Ortiz <ortizman@gmail.com>
 */
open class GenericController<Entity>{

    @Autowired
    lateinit var repository: JpaRepository<Entity, Long>

    /**
     * Devuelve todos los elementos de la entidad
     */
    @GetMapping
    fun listPaginate(
            @PageableDefault(size = 10, page = 0) pageable: Pageable
    ): Page<Entity> {
        return repository.findAll(pageable)
    }

    /**
     * Crea una nueva entidad y devuelve dicha entidad
     */
    @PostMapping
    fun create(@RequestBody entity: Entity) = repository.save(entity)

    /**
     * actualiza una entidad dado su ID
     */
    @PutMapping(path = ["/{id}"])
    fun update(@PathVariable("id") id: Long, @RequestBody entity: Entity): Entity? {
        return repository.save(entity)
    }

    /**
     * Elimina una entidad dado su ID
     */
    @DeleteMapping(path = ["/{id}"])
    fun delete(@PathVariable("id") id: Long) {
        repository.deleteById(id);
    }

    /**
     * Devuelve la entidad con el ID dado
     */
    @GetMapping(path = ["/{id}"])
    fun get(@PathVariable("id") id: Long) = repository.getOne(id)

}