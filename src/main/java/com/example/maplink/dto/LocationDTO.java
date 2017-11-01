package com.example.maplink.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Samuel Catalano
 */
public class LocationDTO implements Serializable{
	
	@Getter
	@Setter
	private double lat;
	
	@Getter
	@Setter
	private double lng;
	
}
