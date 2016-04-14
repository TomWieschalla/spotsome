package de.bht.ebus.spotsome.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class ChatPostDto {

	private String name;
	
	@JsonProperty("spot_id")
	private Long spotId;
	
	@JsonCreator
	public ChatPostDto(
			@JsonProperty("name") String name,
			@JsonProperty("spot_id") Long spotId) {
		this.name = name;
		this.spotId = spotId;
	}
	
	public String getName() {
		return name;
	}

	public Long getSpotId() {
		return spotId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ChatPostDto [name=");
		builder.append(name);
		builder.append(", spotId=");
		builder.append(spotId);
		builder.append("]");
		return builder.toString();
	}

}
