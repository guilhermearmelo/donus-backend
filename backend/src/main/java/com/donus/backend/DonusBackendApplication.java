package com.donus.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableCaching
public class DonusBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DonusBackendApplication.class, args);
	}

}
