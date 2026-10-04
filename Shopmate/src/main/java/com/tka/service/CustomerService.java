package com.tka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tka.model.Customer;
import com.tka.repo.CustomerRepo;

@Service
public class CustomerService {
	
	@Autowired
	CustomerRepo customerRepo;

	public boolean addCustomer(Customer customer) {
		Customer addedCustomer = customerRepo.save(customer);
		if(addedCustomer != null) {
			return true;
		}
		return false;
	}

	public Customer verifyLogin(String username, String password) {
		Customer customer = customerRepo.findByUsernameAndPassword(username, password);	
		if (customer != null) {
			return customer;
		}
		return null;
	}

}
