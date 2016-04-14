package de.bht.ebus.spotsome.rest;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.bht.ebus.spotsome.model.Beacon;
import de.bht.ebus.spotsome.repositories.BeaconRepository;

@RestController
@RequestMapping(value = "/beacon")
public class BeaconController {

	@Autowired
	private BeaconRepository beaconRepository;
	
	@RequestMapping(value = "/{id}", 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Beacon getBeaconById(@PathVariable Long id) {
		return this.beaconRepository.findOne(id);
	}
	
	@RequestMapping(value = "/{uuid}/{major}/{minor}", 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Beacon getBeaconByUuidAndMajorAndMinor(@PathVariable String uuid, @PathVariable int major, @PathVariable int minor) {
		return this.beaconRepository.findByUuidAndMajorAndMinor(uuid.toLowerCase(Locale.ENGLISH), major, minor);
	}
	
	@RequestMapping(method = RequestMethod.POST, 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	public Beacon saveBeacon(@RequestBody Beacon beacon) {
		return this.beaconRepository.save(beacon);
	}
	
	@RequestMapping(value = "/{id}", 
			method = RequestMethod.PUT, 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Beacon> updateBeacon(@PathVariable Long id, @RequestBody Beacon beacon) {
		return new ResponseEntity<Beacon>(HttpStatus.NOT_IMPLEMENTED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteBeacon(@PathVariable Long id) {
		if (this.beaconRepository.findOne(id) == null) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		} else {
			this.beaconRepository.delete(id);
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}

	}
	
	//TODO: Exception Handler -> Status codes which follow rest conventions
	
}
