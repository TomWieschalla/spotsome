package de.bht.ebus.spotsome.repositories;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import de.bht.ebus.spotsome.model.Chat;
import de.bht.ebus.spotsome.model.ChatMessage;

public interface ChatRepository extends CrudRepository<Chat, Long> {

	Iterable<Chat> findAllBySpotId(Long id);
	
	@Query("SELECT m FROM Chat c JOIN c.messages m WHERE c.id = ?1 ORDER BY m.createdOn")
	Iterable<ChatMessage> findAllChatMessagesByChatIdOrderByCreatedOn(Long chatId);
	
	@Query("SELECT m FROM Chat c JOIN c.messages m WHERE c.id = ?1 and m.createdOn > ?2 ORDER BY m.createdOn")
	Iterable<ChatMessage> findAllChatMessagesByChatIdAndAfterCreatedOnOrderByCreatedOn(Long chatId, Date after);
	
	@Query("SELECT m FROM Chat c JOIN c.messages m WHERE c.id = ?1 and m.id > ?2 ORDER BY m.id")
	Iterable<ChatMessage> findAllChatMessagesByChatIdAndGreaterThanChatMessageIdOrderByChatMessageId(Long chatId, Long messageId);

}


