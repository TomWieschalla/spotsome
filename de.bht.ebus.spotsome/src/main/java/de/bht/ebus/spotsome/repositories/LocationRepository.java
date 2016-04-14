package de.bht.ebus.spotsome.repositories;

import org.springframework.data.repository.CrudRepository;

import de.bht.ebus.spotsome.model.Location;

public interface LocationRepository extends CrudRepository<Location, Long> { 
	
	Location findByLatitudeAndLongitude(double latitude, double longitude);
	
}