package de.bht.ebus.spotsome.repositories;

import org.springframework.data.repository.CrudRepository;

import de.bht.ebus.spotsome.model.BulletinBoard;

public interface BulletinBoardRepository extends CrudRepository<BulletinBoard, Long> { 
	
	Iterable<BulletinBoard> findAllBySpotId(Long id);
	
	Iterable<BulletinBoard> findAllBySpotIds(Iterable<Long> spotIds);
	
}