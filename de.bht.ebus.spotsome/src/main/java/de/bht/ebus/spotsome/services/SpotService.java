package de.bht.ebus.spotsome.services;

import java.util.Set;

import javax.validation.ConstraintViolation;

import de.bht.ebus.spotsome.dto.SpotPostDto;
import de.bht.ebus.spotsome.model.Location;
import de.bht.ebus.spotsome.model.Spot;
import de.bht.ebus.spotsome.model.User;

public interface SpotService {

	Iterable<Spot> getAllSpots();
	
	Spot getSpotById(Long id);
	
	Spot saveSpot(Spot spot);
	
	Spot saveSpot(SpotPostDto spotDto, User owner);
	
	void deleteSpotById(Long id);
	
	Iterable<Spot> getAllSpotsWithinRange(Location location);
	
	Set<ConstraintViolation<SpotPostDto>> validateSpotPostDto(SpotPostDto dto);

}
