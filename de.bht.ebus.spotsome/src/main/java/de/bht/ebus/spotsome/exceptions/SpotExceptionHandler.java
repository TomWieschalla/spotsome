package de.bht.ebus.spotsome.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class SpotExceptionHandler {

	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ExceptionHandler(SpotNotFoundException.class)
	@ResponseBody
	public ErrorInfo handleSpotNotFoundException(SpotNotFoundException e) {
		return new ErrorInfo(e.getMessage());
	}
	
}
