package de.bht.ebus.spotsome.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.tika.mime.MimeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

import de.bht.ebus.spotsome.exceptions.FileExtensionDetectionException;
import de.bht.ebus.spotsome.exceptions.UnsupportedMimeTypeException;
import de.bht.ebus.spotsome.model.MediaWrapper;
import de.bht.ebus.spotsome.model.User;
import de.bht.ebus.spotsome.repositories.MediaWrapperRepository;
import de.bht.ebus.spotsome.util.MimeTypeUtil;

@Service
public class MediaWrapperServiceImpl implements MediaWrapperService {

	private static final Logger logger = LoggerFactory.getLogger(MediaWrapperServiceImpl.class);
	
	@Value("file:${media.upload.savedir.absolute_path}")
	private Resource mediaSaveDirectoryResource;

	@Autowired
	private MimeTypeUtil mimeTypeUtil;

	@Autowired
	private MediaWrapperRepository mediaWrapperRepository;

	public MediaWrapper saveMedia(InputStream inputStream, User owner) throws IOException {

		final MimeType mimeType = mimeTypeUtil.detectMimeType(inputStream);
		if (!mimeTypeUtil.isSupportedMimeType(mimeType)) {
			logger.warn("Try to save media file with unsupported mime type {}", mimeType);
			throw new UnsupportedMimeTypeException();
		}
		
		final String extension = mimeType.getExtension();
		if (Strings.isNullOrEmpty(extension)) {
			/*
			 * We don't save files without determined file extension.
			 */
			logger.warn("No file extension found for mime-type {}", mimeType);
			throw new FileExtensionDetectionException();
		}
		
		final String randomMediaFileName = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase(Locale.ENGLISH);
		final String fullMediaFileName = randomMediaFileName + extension;
		final File mediaFile = new File(mediaSaveDirectoryResource.getFile().getAbsolutePath(), fullMediaFileName);
		FileUtils.copyInputStreamToFile(inputStream, mediaFile);
		final MediaWrapper mediaFileWrapper = new MediaWrapper(mediaFile, mimeType, owner);
		return mediaWrapperRepository.save(mediaFileWrapper);
	} 

	@PostConstruct
	private void checkMediaSaveDirectoryResource() {
		try {
			if (!mediaSaveDirectoryResource.getFile().isDirectory()) {
				throw new RuntimeException("mediaSaveDirectory " + mediaSaveDirectoryResource + " doesn't exists or is no directory");
			}
			if (!mediaSaveDirectoryResource.getFile().canWrite()) {
				throw new RuntimeException("mediaSaveDirectory " + mediaSaveDirectoryResource + " not writeable");
			}
		} catch (IOException e) {
			throw new RuntimeException("MediaSaveDirectoryResource checks are failed", e);
		}

	}

	public List<MimeType> getSupportedMimeTypes() {
		return mimeTypeUtil.getSupportedMimeTypes();
	}

	public List<String> getSupportedMimeTypesAsString() {
		final List<MimeType> supportedMimeTypes = this.getSupportedMimeTypes();
		final List<String> supportedMimeTypesAsString = new ArrayList<String>(supportedMimeTypes.size());
		for (MimeType mimeType : supportedMimeTypes) {
			supportedMimeTypesAsString.add(mimeType.toString());
		}
		return supportedMimeTypesAsString;
	}

	@Override
	public MediaWrapper getMediaWrapperById(Long mediaId) {
		return mediaWrapperRepository.findOne(mediaId);
	}

}
