package de.bht.ebus.spotsome.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.tika.mime.MimeType;

import de.bht.ebus.spotsome.exceptions.FileExtensionDetectionException;
import de.bht.ebus.spotsome.exceptions.UnsupportedMimeTypeException;
import de.bht.ebus.spotsome.model.MediaWrapper;
import de.bht.ebus.spotsome.model.User;

public interface MediaWrapperService {
	
	/**
	 * @param inputStream
	 * @param owner
	 * @return
	 * @throws IOException
	 * @throws UnsupportedMimeTypeException
	 * @throws FileExtensionDetectionException
	 */
	MediaWrapper saveMedia(InputStream inputStream, User owner) throws IOException;
	
	List<MimeType> getSupportedMimeTypes();
	
	List<String> getSupportedMimeTypesAsString();

	MediaWrapper getMediaWrapperById(Long mediaId);
	
}
