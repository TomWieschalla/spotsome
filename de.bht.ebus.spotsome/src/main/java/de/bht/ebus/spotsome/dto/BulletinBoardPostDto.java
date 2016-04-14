package de.bht.ebus.spotsome.dto;

import java.util.Objects;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

//TODO: use the validation constraints in service
public final class BulletinBoardPostDto {

	@NotNull
	@Min(1)
	private final Long spotId;
	
	@NotNull
	private final String name;

	@JsonCreator
	public BulletinBoardPostDto(
			@JsonProperty("spot_id") Long spotId,
			@JsonProperty("name") String name) {
		this.spotId = Objects.requireNonNull(spotId);
		this.name = Objects.requireNonNull(name);
	}

	public Long getSpotId() {
		return spotId;
	}
	
	public  String getName() {
		return name;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BulletinBoardPostDto [spotId=");
		builder.append(spotId);
		builder.append(", name=");
		builder.append(name);
		builder.append("]");
		return builder.toString();
	}
	
}
