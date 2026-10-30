package com.plantrip.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PlantripApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlantripApiApplication.class, args);
	}

}
