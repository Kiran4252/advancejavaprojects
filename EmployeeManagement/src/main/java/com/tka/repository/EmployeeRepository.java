package com.tka.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tka.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
	
	boolean existsByEmail(String email);

    List<Employee> findByNameContainingIgnoreCase(String name);

    List<Employee> findByDepartment(String department);

    List<Employee> findBySalaryGreaterThan(double salary);

    List<Employee> findByEmployeeId(int employeeId);

}
