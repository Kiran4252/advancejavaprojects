package com.tka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import com.tka.model.Customer;
import com.tka.service.CustomerService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CustomerController {
	
	@Autowired
	CustomerService customerService;
	
	@PostMapping("/verify-login")
	public String verifyLogin(String username, String password, HttpSession session, Model model) {	
		Customer customer = customerService.verifyLogin(username, password);
		if (customer != null) {
			session.setAttribute("userLoginId", customer.getCid());
			session.setAttribute("role", "user");
			
			model.addAttribute("msg", username + " >> Login Successfully !!!!!");
			return "home";
		}
		model.addAttribute("msg", username + " >> Login Failed *****");
		return "login";
	}
	
	@PostMapping("/add-customer")
	public String addCustomer(Customer customer, Model model) {		
		boolean isAdded = customerService.addCustomer(customer);
		if (isAdded) {
            model.addAttribute("msg",  " >> Register Successfully !!!!!");
            return "login";
            }
		model.addAttribute("msg",  " >> Register Failed *****");
		return "reegister";
	}

}
