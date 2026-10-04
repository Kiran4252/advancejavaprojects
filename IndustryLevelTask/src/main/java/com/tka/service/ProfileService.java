package com.tka.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tka.entity.Profile;
import com.tka.repository.ProfileRepository;

@Service
public class ProfileService {
	
	@Autowired
	private ProfileRepository profileRepository;
	
	//all 
	public List<Profile> getAllProfiles() {
		return profileRepository.findAll();
	}
	
	//search by name
	public Profile searchByName(String fname) {
		return profileRepository.findByFname(fname);
	}

	//create 
	public Profile addProfile(Profile profile) {
		return profileRepository.save(profile);
	}

	//update profile
	public Profile updateProfile(int id, Profile profile) {
		
		Profile existingProfile = profileRepository.findById(id).orElse(null);
		
		if (existingProfile != null) {
			
			existingProfile.setFname(profile.getFname());
            existingProfile.setLname(profile.getLname());
            existingProfile.setPhone(profile.getPhone());
            existingProfile.setCity(profile.getCity());
            existingProfile.setAddress(profile.getAddress());
		}
		
		return profileRepository.save(existingProfile);
	}

	//delete
	public String deleteProfile(int id) {
		
		if (profileRepository.existsById(id)) {
			profileRepository.deleteById(id);
		
		return "Profile deleted successfully";
	}
	
	return null;

 }

	public List<Profile> sortByNameAsc() {
		return profileRepository.findAllByOrderByFnameAscLnameAsc();
	}

	public List<Profile> sortByNameDesc() {
	    return profileRepository.findAllByOrderByFnameDescLnameDesc();
	}

	
}
