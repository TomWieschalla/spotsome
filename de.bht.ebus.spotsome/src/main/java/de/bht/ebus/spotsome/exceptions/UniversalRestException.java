package de.bht.ebus.spotsome.exceptions;

import java.util.Objects;

import org.springframework.http.HttpStatus;

public final class UniversalRestException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private final HttpStatus status;
	
	public UniversalRestException(HttpStatus status) {
		this(null, status);
	}
	
	public UniversalRestException(String message, HttpStatus status) {
		super(message == null ? "no message" : message);
		this.status = Objects.requireNonNull(status);
	}
	
	public HttpStatus getStatus() {
		return status;
	}

}
