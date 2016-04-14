package de.bht.ebus.spotsome.exceptions;

public class BulletinBoardNotFound extends Exception {

	private static final long serialVersionUID = 1L;
	
	private static final String defaultMessage = "BulletinBoard not found";

	public BulletinBoardNotFound() {
		super(defaultMessage);
	}
	
	public BulletinBoardNotFound(String message) {
		super(message);
	}

}
