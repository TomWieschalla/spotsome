package de.bht.ebus.spotsome.repositories;

import org.springframework.data.repository.CrudRepository;

import de.bht.ebus.spotsome.model.Company;
import de.bht.ebus.spotsome.model.SocialIdentity;

public interface SocialIdentityRepository extends CrudRepository<SocialIdentity, Long> { 
	
	SocialIdentity findBySocialIdAndCompany(Long socialId, Company company);
	
}
