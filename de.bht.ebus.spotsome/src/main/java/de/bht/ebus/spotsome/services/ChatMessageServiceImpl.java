package de.bht.ebus.spotsome.services;

import java.util.Date;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.bht.ebus.spotsome.exceptions.MediaWrapperNotFoundException;
import de.bht.ebus.spotsome.model.ChatMessage;
import de.bht.ebus.spotsome.model.MediaWrapper;
import de.bht.ebus.spotsome.model.User;
import de.bht.ebus.spotsome.repositories.ChatMessageRepository;

@Service
@Transactional
public class ChatMessageServiceImpl implements ChatMessageService {

	private static final Logger logger = LoggerFactory.getLogger(ChatMessageServiceImpl.class);
	
	@Autowired
	private ChatMessageRepository chatMessageRepository;
	
	@Autowired
	private MediaWrapperService mediaWrapperService;
	
	
	@Override
	public ChatMessage createMessage(String messageText, Long mediaId, Long createdOn, User author) throws MediaWrapperNotFoundException {
		MediaWrapper mediaWrapper = null;
		if (mediaId != null) {
			mediaWrapper = mediaWrapperService.getMediaWrapperById(mediaId);
			if (mediaWrapper == null) {
				logger.warn("No media wrapper with id {} found", mediaId);
				throw new MediaWrapperNotFoundException("No media with id " + mediaId + " found");
			}
		}
		final ChatMessage message = new ChatMessage(messageText, mediaWrapper, new Date(createdOn), author);
		return message;
	}

	@Override
	public ChatMessage saveMessage(ChatMessage message) {
		return chatMessageRepository.save(message);
	}





}
