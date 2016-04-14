package de.bht.ebus.spotsome.rest;

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

import com.fasterxml.jackson.annotation.JsonView;

import de.bht.ebus.spotsome.dto.ChatPostDto;
import de.bht.ebus.spotsome.exceptions.ChatNotFoundException;
import de.bht.ebus.spotsome.exceptions.SpotNotFoundException;
import de.bht.ebus.spotsome.model.Chat;
import de.bht.ebus.spotsome.model.User;
import de.bht.ebus.spotsome.services.ChatService;
import de.bht.ebus.spotsome.util.JsonViews;

@RestController
@RequestMapping("/chat")
public class ChatController {

	// ~ Instance fields
	// ==============================================================
	
	@Autowired
	private ChatService chatService;
	
	private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
	
	// ~ Methods
	// ==============================================================
	
	/**
	 * GET .../chat?spot_id=2
	 * 
	 * @param spotId
	 * @return
	 * @throws SpotNotFoundException
	 */
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@JsonView(JsonViews.Public.class)
	public Iterable<Chat> getAllChatsBySpotId(@RequestParam(value = "spot_id",required = true) Long spotId) throws SpotNotFoundException {
		return chatService.getChatsBySpotId(spotId);
	}
	
	
	
	/**
	 * GET .../chat/3
	 * 
	 * @param id chatId
	 * @return
	 * @throws ChatNotFoundException
	 */
	@RequestMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@JsonView(JsonViews.Public.class)
	public Chat getChatById(@PathVariable(value = "id") Long chatId) 
			throws ChatNotFoundException {
		final Chat chat = chatService.getChatById(chatId);
		if (chat == null) {
			final String msg = "No chat with id" + chatId + " found";
			logger.warn(msg);
			throw new ChatNotFoundException(msg);
		}
		return chat;
	}
	
	
	/**
	 * POST .../chat
	 * 
	 * @param chatDto
	 * @return
	 * @throws SpotNotFoundException
	 */
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	@JsonView(JsonViews.Public.class)
	public Chat createChat(@RequestBody ChatPostDto chatDto, Authentication authentication) throws SpotNotFoundException {
		final User user = (User) authentication.getPrincipal();
		return chatService.createChat(chatDto.getName(), chatDto.getSpotId(), user);
	}
	
	/**
	 * GET .../chat/test
	 * 
	 * @return
	 */
	@RequestMapping("/test")
	public String testController() {
		return this.getClass().getSimpleName() + " accessible. Yeah!!!";
	}
	
}
