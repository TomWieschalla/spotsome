package de.bht.ebus.spotsome.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BadRequestException.class)
	@ResponseBody
	public ErrorInfo handleBadRequestException(BadRequestException e) {
		return new ErrorInfo(e.getMessage());
	}
	
	@ExceptionHandler(UniversalRestException.class)
	@ResponseBody
	public ResponseEntity<ErrorInfo> handleUniversalRestException(UniversalRestException e) {
		return new ResponseEntity<ErrorInfo>(new ErrorInfo(e.getMessage()), e.getStatus()); 
	}
}
