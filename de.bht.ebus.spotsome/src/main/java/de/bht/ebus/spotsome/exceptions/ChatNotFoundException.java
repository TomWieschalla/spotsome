package de.bht.ebus.spotsome.exceptions;

public class ChatNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private static final String defaultMessage = "Chat not found";

	public ChatNotFoundException() {
		super(defaultMessage);
	}
	
	public ChatNotFoundException(String message) {
		super(message);
	}
	
}
