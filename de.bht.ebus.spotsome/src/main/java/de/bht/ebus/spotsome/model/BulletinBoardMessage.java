package de.bht.ebus.spotsome.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "bulletin_board_message")
public class BulletinBoardMessage extends Message {

	private static final long serialVersionUID = 1L;

	protected BulletinBoardMessage() {
		super();
	}
	
	public BulletinBoardMessage(String messageText, MediaWrapper mediaWrapper,
			Date createdOn, User author) {
		super(messageText, mediaWrapper, createdOn, author);
	}

}
