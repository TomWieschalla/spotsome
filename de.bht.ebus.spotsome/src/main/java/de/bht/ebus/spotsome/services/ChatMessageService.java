package de.bht.ebus.spotsome.services;

import de.bht.ebus.spotsome.exceptions.MediaWrapperNotFoundException;
import de.bht.ebus.spotsome.model.ChatMessage;
import de.bht.ebus.spotsome.model.User;

public interface ChatMessageService {
		
	ChatMessage createMessage(String messageText, Long mediaId, Long createdOn, User author) throws MediaWrapperNotFoundException;
	
	ChatMessage saveMessage(ChatMessage message);
	
}
