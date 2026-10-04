package com.tka.attendance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmployeeAttendenceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeAttendenceApplication.class, args);
		
		System.err.println("Employee Attendance Application started successfully.");
	}

}
