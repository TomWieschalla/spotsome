package de.bht.ebus.spotsome.exceptions;

public class SpotNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private static final String defaultMessage = "Spot not found";

	public SpotNotFoundException() {
		super(defaultMessage);
	}
	
	public SpotNotFoundException(String message) {
		super(message);
	}
	
	
}
