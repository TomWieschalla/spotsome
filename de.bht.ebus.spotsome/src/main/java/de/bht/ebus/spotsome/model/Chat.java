package de.bht.ebus.spotsome.model;

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import de.bht.ebus.spotsome.util.JsonViews;

@Entity
@Table(name="chat")
@AccessType(Type.FIELD)
@NamedQueries({
	@NamedQuery(name = "Chat.findAllBySpotId", 
		query ="SELECT c from Chat c where c.spot.spotId = ?1")
})
public class Chat implements Persistable<Long>{

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "chat_id")
	@JsonProperty("chat_id")
	@JsonView({JsonViews.OnlyId.class, JsonViews.Public.class})
	private Long id;
	
	@JsonView(JsonViews.Public.class)
	private String name;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(nullable = false, updatable = false)
	@JsonView(JsonViews.OnlyId.class)
	private Spot spot;
	
	@OneToOne
	@JoinColumn(name = "user_fk", nullable = false, updatable = false)
	@JsonView(JsonViews.Public.class)
	private User creator;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonView(JsonViews.Public.class)
	private Set<ChatMessage> messages = new HashSet<ChatMessage>();
	
	public Chat(String name, Spot spot, User creator) {
		this.name = Objects.requireNonNull(name);
		this.spot = Objects.requireNonNull(spot);
		this.creator = Objects.requireNonNull(creator);
	}
	
	protected Chat() { }
	
	@Override
	@JsonIgnore
	public boolean isNew() {
		return id == null && spot.getId() == null;
	}
	
	@Override
	public Long getId() {
		return id;
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
	
	public User getCreator() {
		return creator;
	}
	
	public Set<ChatMessage> getMessages() {
		return messages;
	}
	
	public void addMessage(ChatMessage... message) {
		for (ChatMessage msg : message) {
			// Avoid adding null to messages
			if (msg != null) {
				messages.add(msg);
			}
		}
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
		Chat other = (Chat) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
