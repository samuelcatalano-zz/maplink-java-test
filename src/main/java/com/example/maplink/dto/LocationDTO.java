package com.example.maplink.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Samuel Catalano
 */
public class LocationDTO implements Serializable {
	
	@Getter
	@Setter
	private double lat;
	
	@Getter
	@Setter
	private double lng;
	
	/**
	 * Default.
	 */
	public LocationDTO(){
	}
	
	/**
	 * Parameters.
	 */
	public LocationDTO(double lat, double lng){
		this.lat = lat;
		this.lng = lng;
	}
}