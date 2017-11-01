package com.example.maplink.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Samuel Catalano
 */
public class PlaceDTO implements Serializable{
	
	@Getter
	@Setter
	private String place;
	
	@Getter
	@Setter
	private LocationDTO location;
	
}