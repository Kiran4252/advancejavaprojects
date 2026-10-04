package com.tka.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tka.entity.Student;

public interface StudentRepository extends JpaRepository <Student, Long> {
	
	boolean existsByEmail(String email);
	
	boolean existsByMobile(String mobile);

}
