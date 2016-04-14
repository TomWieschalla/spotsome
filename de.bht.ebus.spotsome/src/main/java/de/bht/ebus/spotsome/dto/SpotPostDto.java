package de.bht.ebus.spotsome.dto;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class SpotPostDto {

	@NotNull
	private final String name;
	
	@DecimalMin("1.")
	private final double radius;
	
	@DecimalMin("-90.")
	@DecimalMax("90.")
	private final double latitude;
	
	@DecimalMin("-180.")
	@DecimalMax("180.")
	private final double longitude;

	@JsonCreator
	public SpotPostDto(
			@JsonProperty("name") String name,
			@JsonProperty("radius") double radius, 
			@JsonProperty("latitude") double latitude,
			@JsonProperty("longitude") double longitude) {
		this.name = name;
		this.radius = radius;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public double getRadius() {
		return radius;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SpotPostRequestWrapper [name=");
		builder.append(name);
		builder.append(", radius=");
		builder.append(radius);
		builder.append(", latitude=");
		builder.append(latitude);
		builder.append(", longitude=");
		builder.append(longitude);
		builder.append("]");
		return builder.toString();
	}
	
}
