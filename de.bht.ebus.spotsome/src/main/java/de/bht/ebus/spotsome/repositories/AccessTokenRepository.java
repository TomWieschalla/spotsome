package de.bht.ebus.spotsome.repositories;

import org.springframework.data.repository.CrudRepository;

import de.bht.ebus.spotsome.model.AccessToken;

public interface AccessTokenRepository extends CrudRepository<AccessToken, Long> {

	AccessToken findByToken(String token);
	
}
