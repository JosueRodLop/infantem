package com.isppG8.infantem.infantem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class InfantemApplication {

	public static void main(String[] args) {
		
		Dotenv dotenv = Dotenv.load();

        System.setProperty("spring.profiles.active", dotenv.get("SPRING_PROFILES_ACTIVE"));
		SpringApplication.run(InfantemApplication.class, args);
	}

}
