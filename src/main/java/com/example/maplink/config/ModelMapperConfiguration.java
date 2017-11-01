package com.example.maplink.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Samuel Catalano
 */
@Configuration
public class ModelMapperConfiguration{
	
	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}
}