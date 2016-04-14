package de.bht.ebus.spotsome.exceptions;

public class UnsupportedMimeTypeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private static final String defaultMessage = "MIME-Type of uploaded media not supported by api";

	public UnsupportedMimeTypeException() {
		super(defaultMessage);
	}
	
}
