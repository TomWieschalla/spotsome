package de.bht.ebus.spotsome.rest;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import de.bht.ebus.spotsome.model.MediaWrapper;
import de.bht.ebus.spotsome.model.User;
import de.bht.ebus.spotsome.services.MediaWrapperService;

@RestController
@RequestMapping("/media/upload")
public class MediaUploadController {
	
	@Autowired
	private MediaWrapperService mediaWrapperService;
	
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public MediaWrapper handleMediaUpload(@RequestParam(value= "media") MultipartFile file, Authentication authentication) throws IOException {
			return mediaWrapperService.saveMedia(file.getInputStream(), (User) authentication.getPrincipal());
	}
	
	@RequestMapping(value = "/supported_mime_type", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> getSupportedMimeTypes() {
		return mediaWrapperService.getSupportedMimeTypesAsString();
	}
	
	@RequestMapping("/test")
	public String testUpdloadController() {
		return this.getClass().getSimpleName() + " accessible. Yeah!!!";
	}
	
}
