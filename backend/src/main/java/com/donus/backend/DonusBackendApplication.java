package com.donus.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DonusBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DonusBackendApplication.class, args);
	}

}
