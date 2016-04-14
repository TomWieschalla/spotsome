package de.bht.ebus.spotsome.exceptions;

public class FileExtensionDetectionException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private static final String defaultMessage = "";
	
	public FileExtensionDetectionException() {
		super(defaultMessage);
	}

}
