package ar.huerta.exceptions

/**
 * @author manuel
 */
class ServiceException : RuntimeException {
    override var message: String? = null
    var code: Int? = null

    constructor(code: Int?, message: String?) {
        this.message = message
        this.code = code;
    }

    /**
     * Constructor por defecto
     */
    constructor() {}

    companion object {
        private const val serialVersionUID = 1L
    }
}