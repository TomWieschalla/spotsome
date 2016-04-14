package de.bht.ebus.spotsome.exceptions;

public class MediaWrapperNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private static final String defaultMessage = "MediaWrapper not found";

	public MediaWrapperNotFoundException() {
		super(defaultMessage);
	}
	
	public MediaWrapperNotFoundException(String message) {
		super(message);
	}
	
}
