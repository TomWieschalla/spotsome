package de.bht.ebus.spotsome.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public final class TwitterVerifyCredentialsDto {

	private final Long id;
	
	private final String screenName;
	
	@JsonCreator
	public TwitterVerifyCredentialsDto(
			@JsonProperty("id") Long id,
			@JsonProperty("screen_name") String screenName) {
		this.id = Objects.requireNonNull(id);
		this.screenName = Objects.requireNonNull(screenName);
	}

	public Long getId() {
		return id;
	}
	
	public String getScreenName() {
		return screenName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TwitterVerifyCredentialsDto other = (TwitterVerifyCredentialsDto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TwitterVerifyCredentialsDto [id=" + id + ", screenName="
				+ screenName + "]";
	}

}
