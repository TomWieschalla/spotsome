package de.bht.ebus.spotsome.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.base.Joiner;

import de.bht.ebus.spotsome.dto.SpotPostDto;
import de.bht.ebus.spotsome.exceptions.BadRequestException;
import de.bht.ebus.spotsome.exceptions.SpotNotFoundException;
import de.bht.ebus.spotsome.exceptions.UniversalRestException;
import de.bht.ebus.spotsome.model.Location;
import de.bht.ebus.spotsome.model.Spot;
import de.bht.ebus.spotsome.model.User;
import de.bht.ebus.spotsome.services.BulletinBoardService;
import de.bht.ebus.spotsome.services.ChatService;
import de.bht.ebus.spotsome.services.SpotService;
import de.bht.ebus.spotsome.util.JsonViews;

@RestController
@RequestMapping("/spot")
public class SpotController {

	private static final Logger logger = LoggerFactory.getLogger(SpotController.class);
	
	@Autowired
	private SpotService spotService;
	
	@Autowired
	private BulletinBoardService bulletinBoardService;
	
	@Autowired
	private ChatService chatService;
	
	@RequestMapping(method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Iterable<Spot> getAllSpotsWithinRange(
			@RequestParam(required = false) Double latitude, 
			@RequestParam(required = false) Double longitude) {
		if (latitude == null || longitude == null) {
			return spotService.getAllSpots();
		} else {
			final Location location = new Location(latitude, longitude);
			return spotService.getAllSpotsWithinRange(location);
		}
	}
	
	@RequestMapping(value = "/{id}", 
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Spot getSpotById(@PathVariable Long id) throws SpotNotFoundException {
		final Spot spot = spotService.getSpotById(id);
		if (spot == null) {
			logger.warn("No spot with id {} found", id);
			throw new SpotNotFoundException("No spot with id " + id + " found");
		}
		return spot;
	}

	@RequestMapping(method = RequestMethod.POST, 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	@JsonView(JsonViews.OnlyId.class)
	public Spot saveSpot(@RequestBody SpotPostDto spotDto, Authentication authentication) throws BadRequestException {
		final Set<ConstraintViolation<SpotPostDto>> dtoConstraintViolations = spotService.validateSpotPostDto(spotDto);
		if (dtoConstraintViolations.isEmpty()) {
			final User user = (User) authentication.getPrincipal();
			final Spot spot = spotService.saveSpot(spotDto, user);
			try {
				bulletinBoardService.createBulletinBoard("Default bulletin board", spot.getId(), user);
				chatService.createChat("Default chat", spot.getId(), user);
			} catch (SpotNotFoundException e) {
				logger.error("New created spot doesn't exists. What?!");
				throw new UniversalRestException(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return spot;
		} else {
			// TODO: maybe move this code external
			final List<String> constraintViolationMessages = new ArrayList<String>(dtoConstraintViolations.size());
			for (ConstraintViolation<SpotPostDto> constraintViolation : dtoConstraintViolations) {
				constraintViolationMessages.add(constraintViolation.getPropertyPath() + " " + constraintViolation.getMessage());
			}
			throw new BadRequestException(Joiner.on("; ").join(constraintViolationMessages));
		}
		
	}
	
	@RequestMapping(value = "/{id}", 
			method = RequestMethod.PUT, 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Spot> updateSpot(@PathVariable Long id, @RequestBody Spot spot) {
		return new ResponseEntity<Spot>(HttpStatus.NOT_IMPLEMENTED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteSpot(@PathVariable Long id, Authentication authentication) throws SpotNotFoundException {
		// TODO: Only delete spots which the owned
		if (spotService.getSpotById(id) == null) {
			logger.warn("No spot with id {} found", id);
			throw new SpotNotFoundException("No spot with id " + id + " found");
		} else {
			spotService.deleteSpotById(id);
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
	}
	
	@RequestMapping("/test")
	public String testSpotController() {
		return this.getClass().getSimpleName() + " accessible. Yeah!!!";
	}
	
}
