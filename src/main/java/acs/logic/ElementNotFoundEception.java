package acs.logic;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ElementNotFoundEception extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -899124295355665519L;

	public ElementNotFoundEception() {
		super();
	}

	public ElementNotFoundEception(String message) {
		super(message);
		
	}
	
	

}
