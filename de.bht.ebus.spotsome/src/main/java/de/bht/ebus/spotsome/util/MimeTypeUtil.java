package de.bht.ebus.spotsome.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

@Component
public class MimeTypeUtil {

	private static final Logger logger = LoggerFactory.getLogger(MimeTypeUtil.class);
	
	@Value("classpath:${media.supported_media_types.relative_path}")
	private Resource supportedMimeTypesResource;
	
	@Autowired
	@Qualifier("standardYaml")
	private Yaml yaml;
	
	@Autowired
	private Tika tika;
	
	@Autowired
	@Qualifier("mimeTypes")
	private MimeTypes mimeTypes;
	
	private List<MimeType> supportedMimeTypes;
	
	/**
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public MimeType detectMimeType(File file) throws IOException {
		Objects.requireNonNull(file, "Couldn't detect mimetype of null");
		return detectMimeType(new FileInputStream(file));
	}
	
	/**
	 * @param inputStream
	 * @return
	 * @throws IOException 
	 */
	public MimeType detectMimeType(InputStream inputStream) throws IOException {
		Objects.requireNonNull(inputStream, "Couldn't detect mimetype of null");
		try {
			MediaType mediaType = tika.getDetector().detect(inputStream, new Metadata());
			return this.getMimeTypeForMediaType(mediaType);
		} catch (MimeTypeException e) {
			/* 
			 * This could not happen, because tika.getDetector().detect(...) returns always a valid media type
			*/
			throw new RuntimeException("Invalid mime type returnd from tika and passed as argument to tika.", e);
		}
	}
	
	public MimeType getMimeTypeForName(String mimeType) throws MimeTypeException {
		return mimeTypes.forName(Objects.requireNonNull(mimeType, "Cannot get mimetype for null"));
	}
	
	public MimeType getMimeTypeForMediaType(MediaType mediaType) throws MimeTypeException {
		return this.getMimeTypeForName(Objects.requireNonNull(mediaType, "Cannot get mimetype for null").toString());
	}
	
	public boolean isSupportedMimeType(MimeType mimeType) {
		return supportedMimeTypes.contains(mimeType);
	}
	
	public List<MimeType> getSupportedMimeTypes() {
		return supportedMimeTypes;
	}
	
	/**
	 * Load supported mime types from resource {@link MimeTypeUtil#supportedMimeTypesResource} and assign them as List to field {@link MimeTypeUtil#supportedMimeTypes}
	 * 
	 * @throws IOException
	 * @throws MimeTypeException 
	 */
	@PostConstruct
	private void init() throws IOException, MimeTypeException {
		@SuppressWarnings("unchecked")
		final List<String> supportedMimeTypesTmp = (List<String>) yaml.load(supportedMimeTypesResource.getInputStream());
		Objects.requireNonNull(supportedMimeTypesTmp, "No supported mime types found. Please check file: " + supportedMimeTypesResource.getFilename());
		final List<MimeType> supportedMimeTypes = new ArrayList<MimeType>(supportedMimeTypesTmp.size());
		for (String mimeType : supportedMimeTypesTmp) {
			try {
				supportedMimeTypes.add(this.getMimeTypeForName(mimeType));
				this.supportedMimeTypes = supportedMimeTypes;
			} catch (MimeTypeException e) {
				logger.error("Invalid mime type {} in supportedMimeTypeResource {}", mimeType, supportedMimeTypesResource.getFilename());
				throw e;
			}
		}
	}

}
