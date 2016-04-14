package de.bht.ebus.spotsome.util;

import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static java.lang.Math.acos;
import static java.lang.Math.toRadians;
import static de.bht.ebus.spotsome.util.LocationConstants.EARTH_MEAN_RADIUS;

public class SphericalLawOfCosines implements CalculateDistance {

	public double calculateDistance(
			double lat1, double lon1,
			double lat2, double lon2) {
		lat1 = toRadians(lat1);
		lat2 = toRadians(lat2);
		final double deltaLon = toRadians(lon2 - lon1);

		final double distance = acos(sin(lat1) * sin(lat2) + cos(lat1) * cos(lat2) * cos(deltaLon)) * EARTH_MEAN_RADIUS;
		return distance;
	}

}
