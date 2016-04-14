package de.bht.ebus.spotsome.model;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.tika.mime.MimeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import de.bht.ebus.spotsome.converter.FileToStringConverter;
import de.bht.ebus.spotsome.converter.MimeTypeToStringConverter;
import de.bht.ebus.spotsome.util.JsonViews;

@Entity
@Access(AccessType.FIELD)
@Table(name = "media_wrapper")
public class MediaWrapper implements Serializable {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(MediaWrapper.class);
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "media_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
	@JsonProperty(value = "media_id")
	@JsonView(JsonViews.OnlyId.class)
	private Long mediaId;
	
	@JsonProperty(value = "media_file")
	@JsonSerialize(converter = FileToStringConverter.class)
	@Column(name = "media_file", updatable = false, nullable = false)
	private File mediaFile;
	
	/**
	 * String form of the value of a Multipurpose Internet Mail Extension (MIME) content type 
	 * as defined by RFC 2045 
	 */
	@JsonProperty(value = "mime_type")
	@JsonSerialize(converter = MimeTypeToStringConverter.class)
	@Column(name = "mime_type", updatable = false, nullable = false)
	private MimeType mimeType;
	
	@OneToOne
	@JoinColumn(name = "user_fk", nullable = false, updatable = false)
	@JsonIgnore
	private User owner;
	
	/**
	 * Only needed by JPA
	 */
	protected MediaWrapper() { }
	
	/**
	 * Creates a new <tt>MediaWrapper</tt> instance
	 * 
	 * @param mediaFile
	 * @throws NullPointerException
	 * 			If the <code>mediaFile</code> argument is <code>null</code>
	 * @throws IllegalArgumentException
	 * 			If the <code>mediaFile</code> argument doesn't exists or can't read
	 */
	public MediaWrapper(final File mediaFile, final MimeType mimeType, final User owner) {
		this.mediaFile = Objects.requireNonNull(mediaFile);
		if (!this.mediaFile.canRead()) {
			throw new IllegalArgumentException("MediaFile " + mediaFile + " doesn't exists or can't read");
		}
		this.mimeType = Objects.requireNonNull(mimeType);
		this.owner = Objects.requireNonNull(owner);
	}
	
	public Long getMediaId() {
		return mediaId;
	}
	
	public File getMediaFile() {
		return mediaFile;
	}
	
	public MimeType getMimeType() {
		return mimeType;
	}
	
	public User getOwner() {
		return owner;
	}
	
}
