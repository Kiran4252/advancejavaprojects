package com.tka.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Product {
		
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private int pid; 
		private String pname; 
		private String category; 
		private double price;
		private String imagePath;
}
