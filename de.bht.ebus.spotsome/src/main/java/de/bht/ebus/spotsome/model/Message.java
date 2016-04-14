package de.bht.ebus.spotsome.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import de.bht.ebus.spotsome.util.JsonViews;

@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class Message implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "message_id")
	@JsonView({JsonViews.OnlyId.class, JsonViews.Public.class})
	@JsonProperty(value = "message_id")
	protected Long id;

	@Column(name = "message_text", updatable = false)
	@JsonProperty("message_text")
	@JsonView(JsonViews.Public.class)
	protected String messageText;

	@OneToOne
	@JoinColumn(name = "media_wrapper", nullable = true, updatable = false)
	@JsonProperty("media_wrapper")
	@JsonView(JsonViews.Public.class)
	protected MediaWrapper mediaWrapper;

	@Column(name = "created_on", nullable = false, updatable = false)
	@JsonProperty("created_on")
	@JsonView(JsonViews.Public.class)
	protected Date createdOn;

	@OneToOne
	@JoinColumn(name = "user_fk", nullable = false, updatable = false)
	@JsonView(JsonViews.Public.class)
	protected User author;
	
	protected Message(String messageText, MediaWrapper mediaWrapper, Date createdOn, User author) {
		this.messageText = messageText;
		this.mediaWrapper = mediaWrapper;
		this.createdOn = new Date(Objects.requireNonNull(createdOn).getTime());
		this.author = author;
	}

	protected Message() { }
	
	public Long getId() {
		return id;
	}

	public String getMessageText() {
		return messageText;
	}

	public MediaWrapper getMediaWrapper() {
		return mediaWrapper;
	}
	
	public Date getCreatedOn() {
		return new Date(createdOn.getTime());
	}
	
	public User getAuthor() {
		return author;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((id == null) ? 0 : id.hashCode());
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
		Message other = (Message) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.getId()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Message [id=");
		builder.append(id);
		builder.append(", messageText=");
		builder.append(messageText);
		builder.append(", mediaWrapper=");
		builder.append(mediaWrapper);
		builder.append(", createdOn=");
		builder.append(createdOn);
		builder.append(", author=");
		builder.append(author);
		builder.append("]");
		return builder.toString();
	}

}
