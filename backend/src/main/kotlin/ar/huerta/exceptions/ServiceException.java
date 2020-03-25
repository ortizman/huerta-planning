package ar.huerta.exceptions;

/**
 * @author manuel
 *
 */
public class ServiceException extends RuntimeException {
	
	private String message;
	private Integer code;

	private static final long serialVersionUID = 1L;
	
	public ServiceException(Integer code, String message) {
		this.message = message;
		this.code = code;
	}
	
	/**
	 * Constructor por defecto
	 */
	public ServiceException() {
		
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}	

}
