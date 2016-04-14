package de.bht.ebus.spotsome.dto;

import java.util.Date;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

// TODO: use the validation constraints in service
public final class MessagePostDto {

	private final String messageText;

	private final Long mediaId;

	@NotNull
	@Past
	private final Date createdOn;
	
	@JsonCreator
	public MessagePostDto(
			@JsonProperty("message_text") String messageText,
			@JsonProperty("media_id") Long mediaId,
			@JsonProperty("created_on") Date createdOn) {
		this.messageText = messageText;
		this.mediaId = mediaId;
		this.createdOn = createdOn;
	}

	public String getMessageText() {
		return messageText;
	}

	public Long getMediaId() {
		return mediaId;
	}

	public Date getCreatedOn() {
		return new Date(createdOn.getTime());
	}
	
	
	/**
	 * Quick and dirty hack, maybe write own validation annotation
	 * @return
	 */
	@AssertTrue(message = "Either messageText or mediaId must be non null")
	private boolean isValid() {
		return messageText != null || mediaId != null;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MessagePostDto [messageText=");
		builder.append(messageText);
		builder.append(", mediaId=");
		builder.append(mediaId);
		builder.append(", createdOn=");
		builder.append(createdOn);
		builder.append("]");
		return builder.toString();
	}

}
