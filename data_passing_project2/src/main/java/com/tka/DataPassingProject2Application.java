package com.tka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataPassingProject2Application {

	public static void main(String[] args) {
		SpringApplication.run(DataPassingProject2Application.class, args);
		System.err.println("Server is running on http://localhost:6060");
	}

}
