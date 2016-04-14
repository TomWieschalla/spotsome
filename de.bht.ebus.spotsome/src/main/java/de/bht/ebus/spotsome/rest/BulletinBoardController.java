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

import de.bht.ebus.spotsome.dto.BulletinBoardPostDto;
import de.bht.ebus.spotsome.dto.MessagePostDto;
import de.bht.ebus.spotsome.exceptions.BulletinBoardNotFound;
import de.bht.ebus.spotsome.exceptions.MediaWrapperNotFoundException;
import de.bht.ebus.spotsome.exceptions.SpotNotFoundException;
import de.bht.ebus.spotsome.model.BulletinBoard;
import de.bht.ebus.spotsome.model.BulletinBoardMessage;
import de.bht.ebus.spotsome.model.Message;
import de.bht.ebus.spotsome.model.User;
import de.bht.ebus.spotsome.services.BulletinBoardMessageService;
import de.bht.ebus.spotsome.services.BulletinBoardService;
import de.bht.ebus.spotsome.util.JsonViews;

@RestController
@RequestMapping("/bulletin_board")
public class BulletinBoardController {

	// ~ Instance fields
	// ==============================================================
	
	private static final Logger logger = LoggerFactory.getLogger(BulletinBoardController.class);
	
	@Autowired
	private BulletinBoardMessageService messageService;
	
	@Autowired
	private BulletinBoardService bulletinBoardService;
	
	// ~ Methods
	// ==============================================================
	
	/**
	 * GET .../bulletin_board?spot_id=2
	 * 
	 * @param spot_id
	 * @return
	 * @throws SpotNotFoundException
	 */
	@RequestMapping(method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Iterable<BulletinBoard> getAllBulletinBoardsForSpotId(
			@RequestParam(required = true) Long spot_id) throws SpotNotFoundException {
		return bulletinBoardService.getBulletinBoardsBySpotId(spot_id);
	}
	
	/**
	 * GET .../bulletin_board/3
	 * 
	 * @param id Bulletin board id
	 * @return
	 * @throws BulletinBoardNotFound
	 */
	@RequestMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public BulletinBoard getBulletinBoardById(@PathVariable Long id) throws BulletinBoardNotFound {
		final BulletinBoard bulletinBoard = bulletinBoardService.getBulletinBoardById(id);
		if (bulletinBoard == null) {
			final String msg = "Bulletin board with id " + id + " not found";
			logger.warn(msg);
			throw new BulletinBoardNotFound(msg);
		}
		return bulletinBoard;
	}
	
	// TODO: Maybe move message endpoints to seperate controller
	@RequestMapping(value = "/{id}/message", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Iterable<BulletinBoardMessage> getMessagesFromBulletinBoard(@PathVariable Long id) throws BulletinBoardNotFound {
		return bulletinBoardService.getMessagesFromBulletinBoard(id);
	}
	
	@RequestMapping(value = "/{id}/message", method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	public Message addMessageToBulletinBoard(@PathVariable Long id,
			@RequestBody MessagePostDto messageDto,
			Authentication authentication) throws BulletinBoardNotFound, MediaWrapperNotFoundException {
		final BulletinBoardMessage message = messageService.createMessage(
				messageDto.getMessageText(),
				messageDto.getMediaId(),
				messageDto.getCreatedOn().getTime(),
				(User) authentication.getPrincipal());
		
		final BulletinBoardMessage savedMessage = messageService.saveMessage(message);
		bulletinBoardService.addMessageToBulletinBoard(id, savedMessage);
		return savedMessage;
	}
	
	@RequestMapping(method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	@JsonView(JsonViews.OnlyId.class)
	public BulletinBoard createBulletinBoard(@RequestBody BulletinBoardPostDto bulletinBoardDto, Authentication authentication) throws SpotNotFoundException {
		return bulletinBoardService.createBulletinBoard(bulletinBoardDto.getName(),
				bulletinBoardDto.getSpotId(),
				(User) authentication.getPrincipal());
	}
	
	@RequestMapping("/test")
	public String testController() {
		return this.getClass().getSimpleName() + " accessible. Yeah!!!";
	}
	
}
