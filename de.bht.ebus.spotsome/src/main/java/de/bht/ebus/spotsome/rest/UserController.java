package de.bht.ebus.spotsome.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.bht.ebus.spotsome.dto.DeviceTokenPostDto;
import de.bht.ebus.spotsome.model.User;
import de.bht.ebus.spotsome.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@RequestMapping(value = "/device_token", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public void updateDeviceToken(@RequestBody DeviceTokenPostDto dto, Authentication authentication) {
		final User user = (User) authentication.getPrincipal();
		user.setDeviceToken(dto.getDeviceToken());
		userService.saveUser(user);
	}
	
}
