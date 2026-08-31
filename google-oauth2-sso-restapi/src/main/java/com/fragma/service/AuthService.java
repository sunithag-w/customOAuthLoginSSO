

package com.fragma.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.fragma.entity.UserData;
import com.fragma.exception.UserAlreadyExistsException;
import com.fragma.exception.UserNotFoundException;
import com.fragma.repository.UserDataRepository;

@Service
public class AuthService {

	@Autowired
	private UserDataRepository repository;
	
	  public UserData findUser(String email) {

	        return repository
	                .findByEmail(email)
	                .orElseThrow(() ->
                    new UserNotFoundException(
                            "User not found with email: " + email
                    )
            );
	    }

	  public UserData createUser(
	            String email,
	            String name,
	            String picture,
	            String phoneNumber,
	            String department,
	            String designation) {
		  
		   if (repository.findByEmail(email).isPresent()) {

		        throw new UserAlreadyExistsException(
		                "User already registered with email: " + email
		        );
		    }

	        UserData user = new UserData();

	        user.setEmail(email);
	        user.setName(name);
	        user.setPicture(picture);
	        user.setPhoneNumber(phoneNumber);
	        user.setDepartment(department);
	        user.setDesignation(designation);

	        return repository.save(user);
	    }
}
