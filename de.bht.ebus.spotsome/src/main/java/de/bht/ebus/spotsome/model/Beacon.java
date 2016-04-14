package de.bht.ebus.spotsome.model;

import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.apache.commons.lang3.Range;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Access(AccessType.FIELD)
@Table(name = "beacon")
@NamedQuery(name = "Beacon.findByUuidAndMajorAndMinor", query = "SELECT b from Beacon b where b.uuid = ?1 and b.major = ?2 and b.minor = ?3")
public class Beacon implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final Range<Integer> VALID_RANGE_MINOR_MAJOR = Range.between(0, 65535);
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@JsonProperty(value = "beacon_id")
	@Column(name = "beacon_id", nullable = false)
	private String beaconId;
	
	private String manufacturer;
	
	@Column(nullable = false)
	private String uuid;
	
	@Column(nullable = false)
	private int major;
	
	@Column(nullable = false)
	private int minor;

	/**
	 * Only needed by JPA
	 */
	protected Beacon() { }
	
	public Beacon(String beaconId, String uuid, int major, int minor) {
		setBeaconId(beaconId);
		setUuid(uuid);
		setMajor(major);
		setMinor(minor);
	}
	
	public Long getId() {
		return id;
	}
	
	public String getBeaconId() {
		return beaconId;
	}

	public void setBeaconId(String beaconId) {
		this.beaconId = Objects.requireNonNull(beaconId);
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = Objects.requireNonNull(uuid).toLowerCase(Locale.ENGLISH);
	}

	public int getMajor() {
		return major;
	}

	public void setMajor(int major) {
		if (!VALID_RANGE_MINOR_MAJOR.contains(major)) {
			throw new IllegalArgumentException("Major value " + major + " not in valid range " + VALID_RANGE_MINOR_MAJOR);
		}
		this.major = major;
	}

	public int getMinor() {
		return minor;
	}

	public void setMinor(int minor) {
		if (!VALID_RANGE_MINOR_MAJOR.contains(minor)) {
			throw new IllegalArgumentException("Minor value " + minor + " not in valid range " + VALID_RANGE_MINOR_MAJOR);
		}
		this.minor = minor;
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
		Beacon other = (Beacon) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Beacon [id=");
		builder.append(id);
		builder.append(", beaconId=");
		builder.append(beaconId);
		builder.append(", manufacturer=");
		builder.append(manufacturer);
		builder.append(", uuid=");
		builder.append(uuid);
		builder.append(", major=");
		builder.append(major);
		builder.append(", minor=");
		builder.append(minor);
		builder.append("]");
		return builder.toString();
	}
	
}
