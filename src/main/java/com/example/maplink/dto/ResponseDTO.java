package com.example.maplink.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author Samuel Catalano
 */
@JsonPropertyOrder({ "villain", "targets" })
public class ResponseDTO implements Serializable {
	
	@Getter
	@Setter
	private List<TargetDTO> targets;
	
	@Getter
	@Setter
	private VillainDTO villain;
}