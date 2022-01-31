package com.juli0mendes.capitalgainsms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CapitalGainsMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CapitalGainsMsApplication.class, args);
	}

}
