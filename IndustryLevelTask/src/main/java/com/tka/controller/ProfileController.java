package com.tka.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tka.entity.Profile;
import com.tka.service.ProfileService;

@RestController
public class ProfileController {
	
	@Autowired
	private ProfileService profileService;
	
	//all
	@GetMapping("/all")
    public List<Profile> getAllProfiles() {
        return profileService.getAllProfiles();
    }
	
	//search by name
	@GetMapping("/search/name/{fname}")
    public Profile searchByName(@PathVariable String fname) {
        return profileService.searchByName(fname);
    }
	
	//create
	@PostMapping("/add")
	public Profile addProfile(@RequestBody Profile profile) {
		return profileService.addProfile(profile);
	}
	
	//update
	@PutMapping("/update/{id}")
    public Profile updateProfile(@PathVariable int id, @RequestBody Profile profile) {
        return profileService.updateProfile(id, profile);
    }
	
	//delete
	@DeleteMapping("/delete/{id}")
	public String deleteProfile(@PathVariable int id) {
		return profileService.deleteProfile(id);
	}
	
	
	@GetMapping("sort/name/asc")
	public List<Profile> sortByNameAsc() {
		return profileService.sortByNameAsc();
	}
	
	@GetMapping("/sort/name/desc")
	public List<Profile> sortByNameDesc() {
	    return profileService.sortByNameDesc();
	}
}
