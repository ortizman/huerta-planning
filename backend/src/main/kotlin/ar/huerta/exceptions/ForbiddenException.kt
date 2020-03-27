package ar.huerta.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(
        value = HttpStatus.FORBIDDEN,
        reason = "You do not have access to the resource. This event will be reported"
)
class ForbiddenException : RuntimeException()