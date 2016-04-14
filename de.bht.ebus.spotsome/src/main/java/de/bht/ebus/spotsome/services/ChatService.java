package de.bht.ebus.spotsome.services;

import java.util.Date;
import java.util.List;

import org.springframework.web.context.request.async.DeferredResult;

import de.bht.ebus.spotsome.exceptions.ChatNotFoundException;
import de.bht.ebus.spotsome.exceptions.SpotNotFoundException;
import de.bht.ebus.spotsome.model.Chat;
import de.bht.ebus.spotsome.model.ChatMessage;
import de.bht.ebus.spotsome.model.User;

public interface ChatService {

	Chat getChatById(Long id) throws ChatNotFoundException;
	
	Iterable<Chat> getChatsBySpotId(Long spotId) throws SpotNotFoundException;
	
	Chat createChat(String name, Long spotId, User creator) throws SpotNotFoundException;
	
	void addMessageToChat(Long chatId, ChatMessage message) throws ChatNotFoundException;
	
	DeferredResult<List<ChatMessage>> getMessagesByChatIdAfterMessageIndexAsync(Long chatId, Long messageIndex) throws ChatNotFoundException;

	Iterable<ChatMessage> getAllMessagesByChatId(Long chatId) throws ChatNotFoundException;
	
	Iterable<ChatMessage> getAllMessagesByChatIdAfterCreatedOnOrderByCreatedOn(Long chatId, Date after) throws ChatNotFoundException;
		
	Iterable<ChatMessage> getAllMessagesByChatIdAndGreaterThanMessageIdOrderByMessageId(Long chatId, Long messageId) throws ChatNotFoundException;
}