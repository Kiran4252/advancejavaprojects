package com.tka.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tka.model.Product;
import com.tka.service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProductController {
	
	@Autowired
	ProductService productService;
	
	@GetMapping("/products")
	public String getAllProducts(Model model) {
		List<Product> products = productService.getAllProducts();
		model.addAttribute("products", products);
		return "products";
	}
	
	@GetMapping("/shop")  
	public String getShopProducts(HttpSession session, Model model) {
		Integer userLoginId = (Integer) session.getAttribute("userLoginId");
		if(userLoginId != null) {
			List<Product> products = productService.getAllProducts();
			model.addAttribute("products", products);
			return "shop";
		}
		else {
			model.addAttribute("msg", "Do Login First...");
			return "login";
		}
	}
	
	@GetMapping("/add-to-cart/{pid}")
	public String addToCart(@PathVariable int pid, Model model) {
		Product product = productService.addToCart(pid);
		model.addAttribute("msg", product.getPname() + " > added to cart....");
		return "shop";
	}
	
	@GetMapping("/cart")
	public String viewCart(Model model) {
		List<Product> cartProducts = productService.getCart();
		model.addAttribute("cartProducts", cartProducts);
		return "cart";
	}
	
	@GetMapping("/bill")
	public String viewBill(Model model) {
		List<Product> billProducts = productService.getCart();
		double totalBill = productService.getTotalBill();
		
		model.addAttribute("billProducts", billProducts);
		model.addAttribute("totalBill", totalBill);
		return "bill";
	}

}

