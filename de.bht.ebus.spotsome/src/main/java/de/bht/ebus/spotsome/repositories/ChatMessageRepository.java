package de.bht.ebus.spotsome.repositories;

import org.springframework.data.repository.CrudRepository;

import de.bht.ebus.spotsome.model.ChatMessage;

public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long> { 
	
}
