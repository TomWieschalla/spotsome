package de.bht.ebus.spotsome.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.bht.ebus.spotsome.model.Location;
import de.bht.ebus.spotsome.repositories.LocationRepository;

@Service
@Transactional
public class LocationServiceImpl implements LocationService {

	@Autowired
	private LocationRepository locationRepository;
	
	public Location getByLatitudeAndLongitude(double latitude, double longitude) {
		return locationRepository.findByLatitudeAndLongitude(latitude, longitude);
	}

	public Location saveLocation(Location location) {
		return locationRepository.save(location);
	}

}
