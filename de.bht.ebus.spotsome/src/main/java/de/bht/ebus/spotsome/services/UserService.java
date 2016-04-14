package de.bht.ebus.spotsome.services;

import java.util.List;

import de.bht.ebus.spotsome.model.AccessToken;
import de.bht.ebus.spotsome.model.SocialIdentity;
import de.bht.ebus.spotsome.model.User;

public interface UserService {

	User getUserByAccessToken(AccessToken accessToken);
	
	User getUserByAccessTokenStr(String accessToken);
	
	User getUserBySocialIdentity(SocialIdentity socialIdentity);
	
	User saveUser(User user);
	
	User updateAccessToken(User user, AccessToken accessToken);
	
	List<User> getAllUsers();
	
}
