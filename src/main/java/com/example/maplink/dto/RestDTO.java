package com.example.maplink.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Samuel Catalano
 */
public class RestDTO implements Serializable {
	
	@Getter
	@Setter
	private String message;
	
	public RestDTO(String message) {
		this.message = message;
	}
}