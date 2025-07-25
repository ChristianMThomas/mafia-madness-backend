package com.Mind_Forge.MafiaMadness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:.env.properties")
public class MafiaMadnessApplication {

	public static void main(String[] args) {
		SpringApplication.run(MafiaMadnessApplication.class, args);
	}

}
