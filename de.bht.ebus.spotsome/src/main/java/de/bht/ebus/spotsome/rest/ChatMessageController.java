package de.bht.ebus.spotsome.rest;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.fasterxml.jackson.annotation.JsonView;

import de.bht.ebus.spotsome.dto.MessagePostDto;
import de.bht.ebus.spotsome.exceptions.ChatNotFoundException;
import de.bht.ebus.spotsome.exceptions.MediaWrapperNotFoundException;
import de.bht.ebus.spotsome.model.ChatMessage;
import de.bht.ebus.spotsome.model.Message;
import de.bht.ebus.spotsome.model.User;
import de.bht.ebus.spotsome.services.ChatMessageService;
import de.bht.ebus.spotsome.services.ChatService;
import de.bht.ebus.spotsome.util.JsonViews;

@RestController
@RequestMapping("/chat")
public class ChatMessageController {

	// ~ Instance fields
	// ==============================================================
    
    @SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ChatMessageController.class);
    
    @Autowired
    private ChatMessageService messageService;
    
    @Autowired
    private ChatService chatService;

    
	// ~ Methods
	// ==============================================================
    
    /**
     * GET .../2/message/poll?message_index=7
     * 
     * @param chatId
     * @param messageIndex
     * @return
     * @throws ChatNotFoundException 
     */
    @RequestMapping(value = "/{id}/message/poll", produces = MediaType.APPLICATION_JSON_VALUE)
	@JsonView(JsonViews.Public.class)
    public DeferredResult<List<ChatMessage>> getMessagesBehindMessageIndex(
    		@PathVariable("id") Long chatId,
    		@RequestParam(value = "message_index", required = true) Long messageIndex) throws ChatNotFoundException {
    	return chatService.getMessagesByChatIdAfterMessageIndexAsync(chatId, messageIndex);
    }
    
    /**
     * GET .../2/message?since=1238923854
     * 
     * @param chatId
     * @param timestamp in millieseconds
     * @return
     * @throws ChatNotFoundException 
     */
    @RequestMapping(value = "/{id}/message", produces = MediaType.APPLICATION_JSON_VALUE)
	@JsonView(JsonViews.Public.class)
    public Iterable<ChatMessage> getMessages(@PathVariable("id") Long chatId,
    		@RequestParam(value = "since", required = false) Long timestamp) throws ChatNotFoundException {
    	
    	Date timestampAsDate = (timestamp != null) ? new Date(timestamp) : new Date(0);
    	return chatService.getAllMessagesByChatIdAfterCreatedOnOrderByCreatedOn(chatId, timestampAsDate);
    
    }
    
    /**
     * POST .../{id}/message
     * 
     * @param chatId
     * @param messageDto
     * @return
     * @throws MediaWrapperNotFoundException 
     * @throws ChatNotFoundException 
     */
    @RequestMapping(value = "/{id}/message", method = RequestMethod.POST ,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    @JsonView(JsonViews.OnlyId.class)
    public Message createMessage(
    		@PathVariable("id") Long chatId,
    		@RequestBody MessagePostDto messageDto,
    		Authentication authentication) throws MediaWrapperNotFoundException, ChatNotFoundException {
    	
    	final ChatMessage message = messageService.createMessage(
    			messageDto.getMessageText(),
    			messageDto.getMediaId(),
    			messageDto.getCreatedOn().getTime(),
    			(User) authentication.getPrincipal());
    	final ChatMessage savedMessage = messageService.saveMessage(message);
    	chatService.addMessageToChat(chatId, message);
    	return savedMessage;
    }
	
}
