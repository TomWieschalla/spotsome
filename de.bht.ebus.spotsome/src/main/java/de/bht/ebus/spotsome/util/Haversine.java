package de.bht.ebus.spotsome.util;

import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;
import static de.bht.ebus.spotsome.util.LocationConstants.EARTH_MEAN_RADIUS;

public class Haversine implements CalculateDistance {

	public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
		final double deltaLat = toRadians(lat2 - lat1);
		final double deltaLon = toRadians(lon2 - lon1);

		lat1 = toRadians(lat1);
		lat2 = toRadians(lat2);

		final double a = sin(deltaLat / 2) * sin(deltaLat / 2) + sin(deltaLon / 2) * sin(deltaLon / 2) * cos(lat1) * cos(lat2);
		final double c = 2 * asin(sqrt(a));

		return EARTH_MEAN_RADIUS * c;
	}

}
