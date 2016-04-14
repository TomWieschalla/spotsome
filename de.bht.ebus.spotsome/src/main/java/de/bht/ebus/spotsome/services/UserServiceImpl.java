package de.bht.ebus.spotsome.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.bht.ebus.spotsome.model.AccessToken;
import de.bht.ebus.spotsome.model.SocialIdentity;
import de.bht.ebus.spotsome.model.User;
import de.bht.ebus.spotsome.repositories.SocialIdentityRepository;
import de.bht.ebus.spotsome.repositories.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserRepository userRepository;

	@Autowired SocialIdentityRepository socialIdentityRepository;
	
	@Override
	public User getUserByAccessTokenStr(String accessToken) {
		return userRepository.findByAccessTokenStr(accessToken);
	}
	
	@Override
	public User getUserByAccessToken(AccessToken accessToken) {
		return getUserByAccessTokenStr(accessToken.getToken());
	}

	@Override
	public User getUserBySocialIdentity(SocialIdentity socialIdentity) {
		if (socialIdentity.getId() == null) {
			socialIdentity = socialIdentityRepository.findBySocialIdAndCompany(socialIdentity.getSocialId(), socialIdentity.getCompany());
			if (socialIdentity == null) {
				return null;
			}
		}
		return userRepository.findBySocialIdentity(socialIdentity.getId());
	}
	
	@Override
	public User saveUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public User updateAccessToken(User user, AccessToken accessToken) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}



}
