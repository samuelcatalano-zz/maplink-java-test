package com.example.maplink.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Samuel Catalano
 */
public class VillainDTO implements Serializable {
	
	@Getter
	@Setter
	private String name;
	
	@Getter
	@Setter
	private LocationDTO location;
}