package de.bht.ebus.spotsome.push;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.CreatePlatformApplicationRequest;
import com.amazonaws.services.sns.model.CreatePlatformApplicationResult;
import com.amazonaws.services.sns.model.CreatePlatformEndpointRequest;
import com.amazonaws.services.sns.model.CreatePlatformEndpointResult;
import com.amazonaws.services.sns.model.DeletePlatformApplicationRequest;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

public class AmazonSNSClientWrapper {

	private final Logger logger = LoggerFactory.getLogger(AmazonSNSClientWrapper.class);
	
	private final AmazonSNS snsClient;

	public AmazonSNSClientWrapper(AmazonSNS client) {
		this.snsClient = client;
	}

	private CreatePlatformApplicationResult createPlatformApplication(
			String applicationName, Platform platform, String principal,
			String credential) {
		CreatePlatformApplicationRequest platformApplicationRequest = new CreatePlatformApplicationRequest();
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("PlatformPrincipal", principal);
		attributes.put("PlatformCredential", credential);
		platformApplicationRequest.setAttributes(attributes);
		platformApplicationRequest.setName(applicationName);
		platformApplicationRequest.setPlatform(platform.name());
		return snsClient.createPlatformApplication(platformApplicationRequest);
	}

	private CreatePlatformEndpointResult createPlatformEndpoint(
			Platform platform, String customData, String platformToken,
			String applicationArn) {
		CreatePlatformEndpointRequest platformEndpointRequest = new CreatePlatformEndpointRequest();
		platformEndpointRequest.setCustomUserData(customData);
		String token = platformToken;
		String userId = null;
		if (platform == Platform.BAIDU) {
			String[] tokenBits = platformToken.split("\\|");
			token = tokenBits[0];
			userId = tokenBits[1];
			Map<String, String> endpointAttributes = new HashMap<String, String>();
			endpointAttributes.put("UserId", userId);
			endpointAttributes.put("ChannelId", token);
			platformEndpointRequest.setAttributes(endpointAttributes);
		}
		platformEndpointRequest.setToken(token);
		platformEndpointRequest.setPlatformApplicationArn(applicationArn);
		return snsClient.createPlatformEndpoint(platformEndpointRequest);
	}

	private void deletePlatformApplication(String applicationArn) {
		DeletePlatformApplicationRequest request = new DeletePlatformApplicationRequest();
		request.setPlatformApplicationArn(applicationArn);
		snsClient.deletePlatformApplication(request);
	}

	private PublishResult publish(String endpointArn, PushMessage pushMessage,
			Map<Platform, Map<String, MessageAttributeValue>> attributesMap) {
		PublishRequest publishRequest = new PublishRequest();
		Map<String, MessageAttributeValue> notificationAttributes = getValidNotificationAttributes(attributesMap
				.get(pushMessage.getPlatform()));
		if (notificationAttributes != null && !notificationAttributes.isEmpty()) {
			publishRequest.setMessageAttributes(notificationAttributes);
		}
		publishRequest.setMessageStructure("json");
		// If the message attributes are not set in the requisite method,
		// notification is sent with default attributes
		Map<String, String> messageMap = new HashMap<String, String>();
		messageMap.put(pushMessage.getPlatform().name(), pushMessage.getMessageAsJsonString());
		final String message = PushMessageGenerator.jsonify(messageMap);
		// For direct publish to mobile end points, topicArn is not relevant.
		publishRequest.setTargetArn(endpointArn);

		// Display the message that will be sent to the endpoint/
		logger.info("{Message Body: " + message + "}");
		StringBuilder builder = new StringBuilder();
		builder.append("{Message Attributes: ");
		for (Map.Entry<String, MessageAttributeValue> entry : notificationAttributes
				.entrySet()) {
			builder.append("(\"" + entry.getKey() + "\": \""
					+ entry.getValue().getStringValue() + "\"),");
		}
		builder.deleteCharAt(builder.length() - 1);
		builder.append("}");
		logger.info(builder.toString());

		publishRequest.setMessage(message);
		return snsClient.publish(publishRequest);
	}

	public void sendNotification(PushMessage pushMessage, String principal,
			String credential, String platformToken, String applicationName,
			Map<Platform, Map<String, MessageAttributeValue>> attrsMap) {
		// Create Platform Application. This corresponds to an app on a
		// platform.
		CreatePlatformApplicationResult platformApplicationResult = createPlatformApplication(
				applicationName, pushMessage.getPlatform(), principal, credential);
		logger.info(platformApplicationResult.toString());

		// The Platform Application Arn can be used to uniquely identify the
		// Platform Application.
		String platformApplicationArn = platformApplicationResult
				.getPlatformApplicationArn();

		// Create an Endpoint. This corresponds to an app on a device.
		CreatePlatformEndpointResult platformEndpointResult = createPlatformEndpoint(
				pushMessage.getPlatform(),
				"CustomData - Useful to store endpoint specific data",
				platformToken, platformApplicationArn);
		logger.info(platformEndpointResult.toString());

		// Publish a push notification to an Endpoint.
		PublishResult publishResult = publish(
				platformEndpointResult.getEndpointArn(), pushMessage, attrsMap);
		logger.info("Published! \n{MessageId="
				+ publishResult.getMessageId() + "}");
		// Delete the Platform Application since we will no longer be using it.
		deletePlatformApplication(platformApplicationArn);
	}

	public static Map<String, MessageAttributeValue> getValidNotificationAttributes(
			Map<String, MessageAttributeValue> notificationAttributes) {
		Map<String, MessageAttributeValue> validAttributes = new HashMap<String, MessageAttributeValue>();

		if (notificationAttributes == null) return validAttributes;

		for (Map.Entry<String, MessageAttributeValue> entry : notificationAttributes
				.entrySet()) {
			if (!StringUtils.isBlank(entry.getValue().getStringValue())) {
				validAttributes.put(entry.getKey(), entry.getValue());
			}
		}
		return validAttributes;
	}
}
