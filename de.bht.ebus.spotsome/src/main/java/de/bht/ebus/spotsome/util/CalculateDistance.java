package de.bht.ebus.spotsome.util;


public interface CalculateDistance {

	/**
	 * @param lat1 in signed decimal degrees
	 * @param lon1 in signed decimal degrees
	 * @param lat2 in signed decimal degrees
	 * @param lon2 in signed decimal degrees
	 * @return The distance between two points in meters
	 */
	double calculateDistance(double lat1, double lon1, double lat2, double lon2);

}
