package de.bht.ebus.spotsome.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;
import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import de.bht.ebus.spotsome.util.JsonViews;

@Entity
@Table(name = "bulletin_board")
@AccessType(Type.FIELD)
@NamedQueries({
		@NamedQuery(name = "BulletinBoard.findAllBySpotId", 
			query ="SELECT b from BulletinBoard b JOIN b.spot s where s.spotId = ?1"),
		@NamedQuery(name = "BulletinBoard.findAllBySpotIds", 
			query ="SELECT b from BulletinBoard b JOIN b.spot s where s.spotId IN ?1")})
@JsonAutoDetect(fieldVisibility=Visibility.ANY,
getterVisibility=Visibility.NONE,
isGetterVisibility=Visibility.NONE)
public class BulletinBoard implements Serializable, Persistable<Long> {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "bulletin_board_id")
	@JsonProperty("bulletin_board_id")
	@JsonView(JsonViews.OnlyId.class)
	private Long bulletinBoardId;

	@Column(nullable = false)
	private String name;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(nullable = false, updatable = false)
	private Spot spot;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<BulletinBoardMessage> messages = new HashSet<BulletinBoardMessage>();
	
	@OneToOne
	@JoinColumn(name = "user_fk", nullable = false, updatable = false)
	@JsonIgnore
	private User creator;
	
	public BulletinBoard(String name, Spot spot, User creator) {
		this.name = Objects.requireNonNull(name);
		this.spot = Objects.requireNonNull(spot);
		this.creator = Objects.requireNonNull(creator);
	}
	
	@Override
	public boolean isNew() {
		return bulletinBoardId == null && spot.getId() == null;
	}
	
	protected BulletinBoard() { }
	
	public Long getId() {
		return bulletinBoardId;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Spot getSpot() {
		return spot;
	}
	
	public Set<BulletinBoardMessage> getMessages() {
		return messages;
	}
	
	public void addMessage(BulletinBoardMessage... message) {
		for (BulletinBoardMessage msg : message) {
			// Avoid adding null to messages
			if (msg != null) {
				messages.add(msg);
			}
		}
	}
	
	public User getCreator() {
		return creator;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((bulletinBoardId == null) ? 0 : bulletinBoardId.hashCode());
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
		BulletinBoard other = (BulletinBoard) obj;
		if (bulletinBoardId == null) {
			if (other.bulletinBoardId != null)
				return false;
		} else if (!bulletinBoardId.equals(other.bulletinBoardId))
			return false;
		return true;
	}

}
