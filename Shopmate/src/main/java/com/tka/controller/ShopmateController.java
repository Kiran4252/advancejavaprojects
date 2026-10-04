package com.tka.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class ShopmateController {
	
	@GetMapping("/")
	public String getIndexPage() {
		return "index";
	}
	
	@GetMapping("/login")
	public String getLoginPage() {
		return "login";
	}
	
	@GetMapping("/register")
	public String getRegisterPage() {
		return "register";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "index";
	}
	
}
