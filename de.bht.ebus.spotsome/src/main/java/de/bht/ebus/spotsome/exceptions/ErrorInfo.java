package de.bht.ebus.spotsome.exceptions;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class ErrorInfo {

	@JsonProperty("message")
	private final String errorMessage;
	
	@JsonCreator
	public ErrorInfo(@JsonProperty("message") String errorMessage) {
		this.errorMessage = Objects.requireNonNull(errorMessage);
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ErrorInfo [errorMessage=");
		builder.append(errorMessage);
		builder.append("]");
		return builder.toString();
	}
	
}
