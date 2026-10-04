package com.tka.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
public class IPLController {
	
	@GetMapping("/")
	public String HomeView() {
		return "index";
	}
	
	@GetMapping("/get-one-player/{id}")
	public String getPlayerView(@PathVariable int id, Model model) {
		System.err.println(id);
		
		model.addAttribute("pk", id);
		model.addAttribute("user", "Virat Kohli");
		model.addAttribute("pwd", "RCB1234");
		
		return "display";
	}
	
	@GetMapping("/insert-player")
	public String insertOne() {
		System.err.println("using get method");
		return "insert";
	}
	
	@PostMapping("/insert-player")
	public String insertOne2(@ModelAttribute Player obj, Model model) {
		
		System.out.println(obj.getJn());
		System.out.println(obj.getPname());
		System.out.println(obj.getRuns());
		
		model.addAttribute("player", obj);
		
		System.err.println("using post method");
		return "display2";
	}
}
