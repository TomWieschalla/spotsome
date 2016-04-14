package de.bht.ebus.spotsome.model;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.Range;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import de.bht.ebus.spotsome.util.JsonViews;

@Entity
@Table(name = "location")
@Access(AccessType.FIELD)
public class Location implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final Range<Double> VALID_RANGE_LATITUDE = Range.between(-90., 90.);
	
	public static final Range<Double> VALID_RANGE_LONGITUDE = Range.between(-180., 180.);

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "location_id")
	@JsonProperty("location_id")
	@JsonView(JsonViews.OnlyId.class)
	private Long locationId;
	
	/**
	 * Latitude in signed decimal degrees (DD)
	 */
	@Column(nullable = false, updatable = false)
	private double latitude;
	
	/**
	 * Longitude in signed decimal degrees (DD)
	 */
	@Column(nullable = false, updatable = false)
	private double longitude;
	
	public Location(double latitude, double longitude) {
		if (!VALID_RANGE_LATITUDE.contains(latitude)) {
			throw new IllegalArgumentException("Latitude (" + latitude + ") not in valid range " + VALID_RANGE_LATITUDE);
		}
		if (!VALID_RANGE_LONGITUDE.contains(longitude)) {
			throw new IllegalArgumentException("Longitude (" + longitude + ") not in valid range " + VALID_RANGE_LONGITUDE);
		}
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	protected Location() { }
	
	public Long getLocationId() {
		return locationId;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((locationId == null) ? 0 : locationId.hashCode());
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
		Location other = (Location) obj;
		if (locationId == null) {
			if (other.locationId != null)
				return false;
		} else if (!locationId.equals(other.locationId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Location [locationId=");
		builder.append(locationId);
		builder.append(", latitude=");
		builder.append(latitude);
		builder.append(", longitude=");
		builder.append(longitude);
		builder.append("]");
		return builder.toString();
	}
	
}
