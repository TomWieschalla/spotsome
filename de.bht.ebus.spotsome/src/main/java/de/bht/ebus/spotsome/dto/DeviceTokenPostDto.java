package de.bht.ebus.spotsome.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeviceTokenPostDto {

	@JsonProperty("device_token")
	private final String deviceToken;
	
	@JsonCreator
	public DeviceTokenPostDto(@JsonProperty("device_token") String deviceToken) {
		this.deviceToken = Objects.requireNonNull(deviceToken);
	}

	public String getDeviceToken() {
		return deviceToken;
	}
	
}
