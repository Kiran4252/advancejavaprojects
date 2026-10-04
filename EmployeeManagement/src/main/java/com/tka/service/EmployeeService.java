package com.tka.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tka.entity.Employee;
import com.tka.repository.EmployeeRepository;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Add Employee
    public String saveEmployee(Employee employee) {

        // Employee ID unique
        if (employeeRepository.existsById(employee.getEmployeeId())) {
            return "Employee ID already exists!";
        }

        // Email unique
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            return "Email already exists!";
        }

        // Name validation
        if (employee.getName() == null ||
                employee.getName().trim().isEmpty()) {
            return "Employee name cannot be empty!";
        }

        // Salary validation
        if (employee.getSalary() <= 10000) {
            return "Salary must be greater than 10000!";
        }

        // Mobile validation
        if (employee.getMobile() == null ||
                !employee.getMobile().matches("\\d{10}")) {
            return "Mobile number must contain 10 digits!";
        }

        employeeRepository.save(employee);

        return "Employee saved successfully!";
    }

    // Get all employees
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Get employee by ID
    public Employee getEmployeeById(int id) {
        return employeeRepository.findById(id).orElse(null);
    }

    // Update employee
    public String updateEmployee(Employee employee) {

        Employee oldEmployee =
                employeeRepository.findById(employee.getEmployeeId())
                .orElse(null);

        if (oldEmployee == null) {
            return "Employee not found!";
        }

        // Check email uniqueness
        if (!oldEmployee.getEmail().equals(employee.getEmail())
                && employeeRepository.existsByEmail(employee.getEmail())) {
            return "Email already exists!";
        }

        // Name validation
        if (employee.getName() == null ||
                employee.getName().trim().isEmpty()) {
            return "Employee name cannot be empty!";
        }

        // Salary validation
        if (employee.getSalary() <= 10000) {
            return "Salary must be greater than 10000!";
        }

        // Mobile validation
        if (employee.getMobile() == null ||
                !employee.getMobile().matches("\\d{10}")) {
            return "Mobile number must contain 10 digits!";
        }

        employeeRepository.save(employee);

        return "Employee updated successfully!";
    }

    // Delete employee
    public String deleteEmployee(int id) {

        if (!employeeRepository.existsById(id)) {
            return "Employee not found!";
        }

        employeeRepository.deleteById(id);

        return "Employee deleted successfully!";
    }

    // Search by name
    public List<Employee> searchByName(String name) {
        return employeeRepository.findByNameContainingIgnoreCase(name);
    }

    // Search by department
    public List<Employee> searchByDepartment(String department) {
        return employeeRepository.findByDepartment(department);
    }

    // Salary > 50000
    public List<Employee> getHighSalaryEmployees() {
        return employeeRepository.findBySalaryGreaterThan(50000);
    }

    // Total employees
    public long getTotalEmployees() {
        return employeeRepository.count();
    }

    // Average salary
    public double getAverageSalary() {

        List<Employee> employees = employeeRepository.findAll();

        if (employees.isEmpty()) {
            return 0;
        }

        double total = 0;

        for (Employee employee : employees) {
            total += employee.getSalary();
        }

        return total / employees.size();
    }
}