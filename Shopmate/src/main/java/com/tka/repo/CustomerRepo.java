package com.tka.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tka.model.Customer;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Integer> {

	Customer findByUsernameAndPassword(String username, String password);

}
