package com.tka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShopmateApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopmateApplication.class, args);
		System.err.println("ShopmateApplication started successfully!");
	}

}
