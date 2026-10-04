package com.tka.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tka.entity.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {
	
	Profile findByFname(String fname);

	List<Profile> findAllByOrderByFnameAscLnameAsc();

	List<Profile> findAllByOrderByFnameDescLnameDesc();

}
