package de.bht.ebus.spotsome.services;

import de.bht.ebus.spotsome.exceptions.MediaWrapperNotFoundException;
import de.bht.ebus.spotsome.model.BulletinBoardMessage;
import de.bht.ebus.spotsome.model.User;

public interface BulletinBoardMessageService {

	BulletinBoardMessage createMessage(String messageText, Long mediaId, Long createdOn, User author) throws MediaWrapperNotFoundException;

	BulletinBoardMessage saveMessage(BulletinBoardMessage message);

	
}
