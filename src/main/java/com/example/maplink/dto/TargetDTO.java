package com.example.maplink.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Samuel Catalano
 */
public class TargetDTO implements Serializable {
	
	@Getter
	@Setter
	private String place;
	
	@Getter
	@Setter
	private LocationDTO location;
	
	private double probability;
	
	/**
	 * Get probability
	 * @return probability
	 */
	public double getProbability(){
		return this.probability;
	}
	
	/**
	 * Set probability
	 * @param probability the probability
	 */
	public void setProbability(final double probability){
		this.probability = probability;
		final double fractionalPart = this.probability % 1;
		this.probability = this.probability - fractionalPart;
	}
}