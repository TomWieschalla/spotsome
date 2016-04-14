package de.bht.ebus.spotsome.services;

import de.bht.ebus.spotsome.model.Location;

public interface LocationService {

	Location saveLocation(Location location);
	
	Location getByLatitudeAndLongitude(double latitude, double longitude);

}
