package ar.huerta.controllers

import ar.huerta.entities.User
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("\${rest.context-path}/users")
class UserController: GenericController<User>() {

}