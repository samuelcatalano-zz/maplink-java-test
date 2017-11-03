package com.example.maplink.utils;

public class HaversineUtils{
	
	private static final double R = 6371; // in kilometers
	
	/**
	 * Responsible for calculate the inear distance
	 * @param lat1 latitude 1
	 * @param lon1 longitute 1
	 * @param lat2 latitude 2
	 * @param lon2 longitude 2
	 * @return the result in kilometers
	 */
	public static double haversine(double lat1, final double lon1, double lat2, final double lon2){
		final double dLat = Math.toRadians(lat2 - lat1);
		final double dLon = Math.toRadians(lon2 - lon1);
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);
		final double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
		final double c = 2 * Math.asin(Math.sqrt(a));
		return R * c;
	}
}