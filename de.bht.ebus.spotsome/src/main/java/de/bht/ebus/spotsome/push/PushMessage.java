package de.bht.ebus.spotsome.push;

import java.util.Objects;

public class PushMessage {

	private final String messageAsJsonString;
	
	private final Platform platform;
	
	public PushMessage(String messageAsJsonString, Platform platform) {
		this.messageAsJsonString = Objects.requireNonNull(messageAsJsonString);
		this.platform = Objects.requireNonNull(platform);
	}

	public String getMessageAsJsonString() {
		return messageAsJsonString;
	}

	public Platform getPlatform() {
		return platform;
	}
	
	
}
