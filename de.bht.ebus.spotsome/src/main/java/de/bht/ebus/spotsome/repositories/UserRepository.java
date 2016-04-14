package de.bht.ebus.spotsome.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import de.bht.ebus.spotsome.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	User findByAccessTokenStr(String accessToken);
	
	User findBySocialIdentity(Long id);
	
}
