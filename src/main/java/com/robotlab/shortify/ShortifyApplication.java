package com.robotlab.shortify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.robotlab.shortify.config.RateLimitProperties;

@SpringBootApplication
@EnableConfigurationProperties(RateLimitProperties.class)
public class ShortifyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShortifyApplication.class, args);
	}

}
