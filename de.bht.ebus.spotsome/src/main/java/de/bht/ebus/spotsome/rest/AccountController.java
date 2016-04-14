package de.bht.ebus.spotsome.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.bht.ebus.spotsome.dto.RegisterUserPostDto;
import de.bht.ebus.spotsome.dto.TwitterVerifyCredentialsDto;
import de.bht.ebus.spotsome.exceptions.UniversalRestException;
import de.bht.ebus.spotsome.model.Company;
import de.bht.ebus.spotsome.model.SocialIdentity;
import de.bht.ebus.spotsome.model.User;
import de.bht.ebus.spotsome.services.UserService;
import de.bht.ebus.spotsome.util.JsonViews;

@RestController
@RequestMapping("/account")
public class AccountController {

	// ~ Instance fields
	// ==============================================================

	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private UserService userService;

	private static final String verifyTwitterCredentialsUrl =
			"https://api.twitter.com/1.1/account/verify_credentials.json";

	// ~ Methods
	// ==============================================================

	@RequestMapping(value = "/access_token", produces = MediaType.APPLICATION_JSON_VALUE)
	@JsonView(JsonViews.Sensible.class)
	public User getAccessToken(HttpServletRequest request) {

		final TwitterVerifyCredentialsDto dto = verifyTwitterCredentials(request);
		final SocialIdentity socialIdentity = new SocialIdentity(dto.getId(), Company.TWITTER);

		final User user = userService.getUserBySocialIdentity(socialIdentity);

		if (user == null) {
			throw new UniversalRestException("User does not exists", HttpStatus.NOT_FOUND);
		}
		return user;
	}

	@RequestMapping(value = "/register",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	@JsonView(JsonViews.Sensible.class)
	public User registerAccount(HttpServletRequest request) {

		final TwitterVerifyCredentialsDto dto = verifyTwitterCredentials(request);
		final SocialIdentity socialIdentity = new SocialIdentity(dto.getId(), Company.TWITTER);
		User user = userService.getUserBySocialIdentity(socialIdentity);

		final RegisterUserPostDto registerUserPostDto;
		try {
			registerUserPostDto = extractRegisterUserPostDto(request);
		} catch (JsonParseException | JsonMappingException e) {
			logger.error("Unable to parse request body.", e);
			throw new UniversalRestException(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (IOException | IllegalStateException e) {
			logger.error("Unable to parse request body.", e);
			throw new UniversalRestException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		// Did we know the user?
		if (user == null) {
			user = new User(dto.getScreenName(), socialIdentity, registerUserPostDto.getDeviceToken());
			user = userService.saveUser(user);
			return user;
		} else {
			logger.warn("Try to register already known user {}", user);
			throw new UniversalRestException("User already exists", HttpStatus.CONFLICT);
		}
	}

	@RequestMapping("/test")
	public String testController(Authentication auth) {
		logger.info(((User) auth.getPrincipal()).getUsername());
		return this.getClass().getSimpleName() + " accessible. Yeah!!!";
	}




	private TwitterVerifyCredentialsDto verifyTwitterCredentials(HttpServletRequest request) {

		/*
		 * Check if the HTTP header contains the fields "X-Auth-Service-Provider"
		 * and "X-Verify-Credentials-Authorization", which are needed by "Twitter OAuth Echo".
		 *
		 * More information: https://dev.twitter.com/oauth/echo
		 */
		final String xVerfiyCredentialsAuthorization =
				request.getHeader("X-Verify-Credentials-Authorization");

		if (xVerfiyCredentialsAuthorization == null) {
			throw new UniversalRestException(
					"\"X-Verify-Credentials-Authorization\" header field missing",
					HttpStatus.BAD_REQUEST);
		}

		/*
		 * "X-Verify-Credentials-Authorization" leads to HTTP-Status 400 by Twitter
		 * Use "Authorization" header field instead
		 */
		final Header[] headers = {
				new BasicHeader("Authorization", xVerfiyCredentialsAuthorization)
		};

		final CloseableHttpClient httpclient = HttpClients.createDefault();
		final HttpGet httpGet = new HttpGet(verifyTwitterCredentialsUrl);
		httpGet.setHeaders(headers);

		try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
			if (response.getStatusLine().getStatusCode() == 200) {
				final ObjectMapper mapper = new ObjectMapper();
				final TwitterVerifyCredentialsDto twitterResponseDto =
						mapper.readValue(response.getEntity().getContent(),
								TwitterVerifyCredentialsDto.class);
				return twitterResponseDto;
			} else {
				logger.warn("Credentials are not valid. Got status code: {}. Got content: {}",
						response.getStatusLine().getStatusCode(),
						IOUtils.toString(response.getEntity().getContent(), "UTF-8"));
				throw new UniversalRestException("Credentials are not valid", HttpStatus.UNAUTHORIZED);
			}
		} catch (ClientProtocolException e) {
			logger.error("Unable to create connection to uri: " + verifyTwitterCredentialsUrl, e);
			throw new UniversalRestException(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (IOException e) {
			logger.error("Unable to map json to dto object", e);
			throw new UniversalRestException(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	private RegisterUserPostDto extractRegisterUserPostDto(HttpServletRequest request)
			throws JsonParseException, JsonMappingException, IOException {
		final ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(request.getInputStream(), RegisterUserPostDto.class);
	}
	
}
