package com.tka.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "employee")
public class Employee {

    @Id
    
    private Integer employeeId;

    private String name;
    private String email;
    private String mobile;
    private String department;
    private String designation;
    private double salary;
    private LocalDate joiningDate;

   
}