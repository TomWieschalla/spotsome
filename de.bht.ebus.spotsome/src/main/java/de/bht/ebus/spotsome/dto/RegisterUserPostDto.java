package de.bht.ebus.spotsome.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class RegisterUserPostDto {

	@JsonProperty("device_token")
	private final String deviceToken;

	@JsonCreator
	public RegisterUserPostDto(
			@JsonProperty("device_token") String deviceToken) {
		this.deviceToken = Objects.requireNonNull(deviceToken);
	}
	
	public String getDeviceToken() {
		return deviceToken;
	}

}
