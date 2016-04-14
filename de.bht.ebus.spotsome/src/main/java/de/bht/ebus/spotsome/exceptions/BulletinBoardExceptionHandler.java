package de.bht.ebus.spotsome.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class BulletinBoardExceptionHandler {

	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ExceptionHandler(BulletinBoardNotFound.class)
	@ResponseBody
	public ErrorInfo handleBulletinBoardNotFoundException(BulletinBoardNotFound e) {
		return new ErrorInfo(e.getMessage());
	}
	
}
