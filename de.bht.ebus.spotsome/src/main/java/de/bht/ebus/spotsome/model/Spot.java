package de.bht.ebus.spotsome.model;

import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import de.bht.ebus.spotsome.util.JsonViews;

@Entity
@Table(name = "spot")
@Access(AccessType.FIELD)
@JsonAutoDetect(fieldVisibility=Visibility.ANY,
	getterVisibility=Visibility.NONE,
	isGetterVisibility=Visibility.NONE)
public class Spot implements Persistable<Long> {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "spot_id")
	@JsonView({JsonViews.OnlyId.class, JsonViews.Public.class})
	@JsonProperty("spot_id")
	private Long spotId;
	
	@Column(nullable = false)
	@JsonView(JsonViews.Public.class)
	private String name;
	
	/**
	 * The radius around the location in meters
	 */
	@Column(nullable = false)
	@JsonView(JsonViews.Public.class)
	private double radius;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name = "location_id", nullable = false)
	@JsonView(JsonViews.Public.class)
	private Location location;
	
	@OneToOne
	@JoinColumn(name = "user_fk", nullable = false, updatable = false)
	@JsonIgnore
	private User owner;

	public Spot(String name, double radius, Location location, User owner) {
		setName(name);
		setRadius(radius);
		setLocation(location);
		this.owner = Objects.requireNonNull(owner);
	}
	
	protected Spot() { }

	public Long getId() {
		return spotId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = Objects.requireNonNull(name);
	}
	
	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = Objects.requireNonNull(location);
	}
	
	public User getOwner() {
		return owner;
	}

	@Override
	public boolean isNew() {
		return spotId == null && location.getLocationId() == null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((spotId == null) ? 0 : spotId.hashCode());
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
		Spot other = (Spot) obj;
		if (spotId == null) {
			if (other.spotId != null)
				return false;
		} else if (!spotId.equals(other.spotId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Spot [spotId=");
		builder.append(spotId);
		builder.append(", name=");
		builder.append(name);
		builder.append(", radius=");
		builder.append(radius);
		builder.append(", location=");
		builder.append(location);
		builder.append(", owner=");
		builder.append(owner);
		builder.append("]");
		return builder.toString();
	}

}
