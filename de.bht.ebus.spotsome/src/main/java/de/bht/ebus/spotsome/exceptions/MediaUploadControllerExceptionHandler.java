package de.bht.ebus.spotsome.exceptions;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class MediaUploadControllerExceptionHandler {

	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ExceptionHandler(UnsupportedMimeTypeException.class)
	@ResponseBody
	public ErrorInfo handleUnsupportedMimeTypeException(UnsupportedMimeTypeException e) {
		return new ErrorInfo(e.getMessage());
	}
	
	@ResponseStatus(value = HttpStatus.NOT_IMPLEMENTED)
	@ExceptionHandler(FileExtensionDetectionException.class)
	@ResponseBody
	public ErrorInfo handleFileExtensionDetectionException(FileExtensionDetectionException e) {
		return new ErrorInfo(e.getMessage());
	}
	
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(IOException.class)
	public void handleIOException(IOException e) {

	}
	
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ExceptionHandler(MediaWrapperNotFoundException.class)
	@ResponseBody
	public ErrorInfo handleMediaWrapperNotFoundException(MediaWrapperNotFoundException e) {
		return new ErrorInfo(e.getMessage());
	}
	
}
