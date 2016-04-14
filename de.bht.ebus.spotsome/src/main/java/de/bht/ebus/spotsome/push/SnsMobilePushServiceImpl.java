package de.bht.ebus.spotsome.push;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;

import de.bht.ebus.spotsome.model.User;
import de.bht.ebus.spotsome.services.UserService;

@Service
public class SnsMobilePushServiceImpl implements SnsMobilePushService {
	
	@Value("file:${aws.credentials.properties}")
	private Resource awsProperties;
	
	private static final Logger logger = LoggerFactory.getLogger(SnsMobilePushServiceImpl.class);
	
	public static final Map<Platform, Map<String, MessageAttributeValue>> attributesMap =
			new HashMap<Platform, Map<String, MessageAttributeValue>>();
	
	static {
		attributesMap.put(Platform.GCM, null);
		attributesMap.put(Platform.APNS, null);
	}

	private AmazonSNSClientWrapper snsClientWrapper;
	
	@Autowired
	private UserService userService;
	
	public void sendAppNotification(PushMessage message) {
		final Platform platform = message.getPlatform();
		final List<User> users = userService.getAllUsers();
		switch (platform) {
			case APNS:
				sendAppleAppNotificationToUsers(message, users);
				break;
			case GCM:
				sendAndroidAppNotification(message, users);
				break;
			default:
				logger.warn("Cannot send to platform " + platform.name() + "; Service not implemented yet");
				break;
		}
	}
	
	
	private void sendAppleAppNotificationToUsers(PushMessage message, Iterable<User> users) {
//		String certificate = ""; // This should be in pem format with \n at the end of each line.
//		String privateKey = ""; // This should be in pem format with \n at the end of each line.
//		String applicationName = "";
//		for (User user : users) {
//			snsClientWrapper.sendNotification(message, certificate,
//					privateKey, user.getDeviceToken(), applicationName, attributesMap);
//		}
	}
	
	private void sendAndroidAppNotification(PushMessage message, Iterable<User> users) {
		String serverAPIKey = "AIzaSyBFoNdhjBC5peOCd01cwe_mykdEWBQ5oJU";
		String applicationName = "de.bht.spotsome.pushtest";
		for (User user : users) {
			snsClientWrapper.sendNotification(message, "", serverAPIKey,
					user.getDeviceToken(), applicationName, attributesMap);
		}
	}	
	
	@PostConstruct
	private void init() throws IOException {		
		final AmazonSNS sns = new AmazonSNSClient(new PropertiesCredentials(awsProperties.getInputStream()));
		sns.setEndpoint("https://sns.us-west-2.amazonaws.com");
		
		try {
			this.snsClientWrapper = new AmazonSNSClientWrapper(sns);
		} catch (AmazonServiceException ase) {
			logger.error("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon SNS, but was rejected with an error response for some reason.");
			logger.error("Error Message:    " + ase.getMessage());
			logger.error("HTTP Status Code: " + ase.getStatusCode());
			logger.error("AWS Error Code:   " + ase.getErrorCode());
			logger.error("Error Type:       " + ase.getErrorType());
			logger.error("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			logger.error("Caught an AmazonClientException, which means the client encountered "
							+ "a serious internal problem while trying to communicate with SNS, such as not "
							+ "being able to access the network.");
			logger.error("Error Message: " + ace.getMessage());
		}
	}
	
}
