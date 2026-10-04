package com.tka.controller;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.tka.entity.Employee;
import com.tka.service.EmployeeService;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Home
    @GetMapping("")
    public String home(Model model) {

        model.addAttribute("totalEmployees",
                employeeService.getTotalEmployees());

        model.addAttribute("averageSalary",
                employeeService.getAverageSalary());

        return "index";
    }

    // Employee List
    @GetMapping("/list")
    public String employeeList(Model model) {

        model.addAttribute("employees",
                employeeService.getAllEmployees());

        return "employee-list";
    }

    // Add page
    @GetMapping("/add")
    public String addEmployeePage(Model model) {

        model.addAttribute("employee", new Employee());

        return "add-employee";
    }

    // Save employee
    @PostMapping("/save")
    public String saveEmployee(
            @ModelAttribute Employee employee,
            Model model) {

        String message =
                employeeService.saveEmployee(employee);

        if (message.equals("Employee saved successfully!")) {

            return "redirect:/employees/list";
        }

        model.addAttribute("employee", employee);
        model.addAttribute("error", message);

        return "add-employee";
    }

    // Search page
    @GetMapping("/search")
    public String searchPage() {

        return "search-employee";
    }

    // Search employee
    @GetMapping("/search/result")
    public String searchEmployee(
            @RequestParam String name,
            Model model) {

        model.addAttribute("employees",
                employeeService.searchByName(name));

        model.addAttribute("searchName", name);

        return "search-employee";
    }

    // Search by department
    @GetMapping("/search/department")
    public String searchDepartment(
            @RequestParam String department,
            Model model) {

        model.addAttribute("employees",
                employeeService.searchByDepartment(department));

        model.addAttribute("selectedDepartment",
                department);

        return "search-employee";
    }

    // Edit page
    @GetMapping("/edit/{id}")
    public String editEmployee(
            @PathVariable int id,
            Model model) {

        Employee employee =
                employeeService.getEmployeeById(id);

        if (employee == null) {
            return "redirect:/employees/list";
        }

        model.addAttribute("employee", employee);

        return "edit-employee";
    }

    // Update
    @PostMapping("/update")
    public String updateEmployee(
            @ModelAttribute Employee employee,
            Model model) {

        String message =
                employeeService.updateEmployee(employee);

        if (message.equals("Employee updated successfully!")) {

            return "redirect:/employees/list";
        }

        model.addAttribute("employee", employee);
        model.addAttribute("error", message);

        return "edit-employee";
    }

    // Delete
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable int id) {

        employeeService.deleteEmployee(id);

        return "redirect:/employees/list";
    }

    // High salary employees
    @GetMapping("/salary")
    public String highSalaryEmployees(Model model) {

        model.addAttribute("employees",
                employeeService.getHighSalaryEmployees());

        return "employee-list";
    }
}
