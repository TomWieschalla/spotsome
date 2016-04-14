package de.bht.ebus.spotsome.repositories;

import org.springframework.data.repository.CrudRepository;

import de.bht.ebus.spotsome.model.Beacon;

public interface BeaconRepository extends CrudRepository<Beacon, Long> {

	Beacon findByUuidAndMajorAndMinor(String uuid, int major, int minor);
	
}
