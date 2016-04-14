package de.bht.ebus.spotsome.services;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import static java.lang.Math.round;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.bht.ebus.spotsome.dto.SpotPostDto;
import de.bht.ebus.spotsome.model.Location;
import de.bht.ebus.spotsome.model.Spot;
import de.bht.ebus.spotsome.model.User;
import de.bht.ebus.spotsome.repositories.SpotRespository;
import de.bht.ebus.spotsome.util.LocationUtil;

@Service
@Transactional
public class SpotServiceImpl implements SpotService {

	@Autowired
	private Validator validator;
	
	@Autowired
	private LocationService locationService;
	
	@Autowired
	private SpotRespository spotRepository;
	
	@Autowired
	private LocationUtil locationUtil;
	
	public Iterable<Spot> getAllSpots() {
		return spotRepository.findAll();
	}

	public Spot getSpotById(Long id) {
		return spotRepository.findOne(id);
	}

	public Spot saveSpot(Spot spot) {
		return spotRepository.save(spot);
	}
	
	public Spot saveSpot(SpotPostDto spotDto, User owner) {
		Location location = locationService.getByLatitudeAndLongitude(
				spotDto.getLatitude(),
				spotDto.getLongitude());
		if (location == null) {
			location = new Location(spotDto.getLatitude(), spotDto.getLongitude());
		} 
		
		final Spot spot = new Spot(
				spotDto.getName(),
				spotDto.getRadius(),
				location, owner);		
		return spotRepository.save(spot);
	}

	public void deleteSpotById(Long id) {
		spotRepository.delete(id);
	}
	
	public Iterable<Spot> getAllSpotsWithinRange(Location location) {
		final Set<Spot> spotsWithinRange = new HashSet<Spot>();
		final Iterable<Spot> allSpots = spotRepository.findAll();
		for (Spot spot : allSpots) {
			final double distance = locationUtil.calculateDistance(spot.getLocation(), location);
			if (round(distance) <= round(spot.getRadius())) {
				spotsWithinRange.add(spot);
			}
		}
		return spotsWithinRange;
	}

	@Override
	public Set<ConstraintViolation<SpotPostDto>> validateSpotPostDto(SpotPostDto dto) {
		return validator.validate(dto);
	}

}
