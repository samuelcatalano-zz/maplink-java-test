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
	public static double haversine(double lat1, double lon1, double lat2, double lon2){
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);
		double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
		double c = 2 * Math.asin(Math.sqrt(a));
		return R * c;
	}
	
	/**
	 * Just for a test
	 * @param args the arguments
	 */
	public static void main(String[] args){
		System.out.println(haversine(36.12, - 86.67, 33.94, - 118.40));
	}
}