package com.tka.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tka.model.Product;
import com.tka.repo.ProductRepo;

@Service
public class ProductService {
	
	@Autowired
	ProductRepo productRepo;
	
	List<Product> cartProduct = new ArrayList<>();

	public List<Product> getAllProducts() {
		return productRepo.findAll();
	}

	public Product addToCart(int pid) {
		Product product = productRepo.getById(pid);
		if (product != null) 
		cartProduct.add(product);
		return product;
	}		

	public List<Product> getCart() {
		return cartProduct;
	}

	public double getTotalBill() {
        return cartProduct.stream()
        					.mapToDouble(Product::getPrice)
        					.sum();
	}

}
