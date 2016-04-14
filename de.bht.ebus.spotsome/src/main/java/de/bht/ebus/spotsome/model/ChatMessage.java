package de.bht.ebus.spotsome.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "chat_message")
public class ChatMessage extends Message {

	private static final long serialVersionUID = 1L;

	protected ChatMessage() {
		super();
	}

	public ChatMessage(String messageText, MediaWrapper mediaWrapper,
			Date createdOn, User author) {
		super(messageText, mediaWrapper, createdOn, author);
	}

}
