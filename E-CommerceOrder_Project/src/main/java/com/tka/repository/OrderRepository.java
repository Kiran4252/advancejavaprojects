package com.tka.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tka.entity.CustomerOrder;

public interface OrderRepository extends JpaRepository<CustomerOrder, Integer> {

}
