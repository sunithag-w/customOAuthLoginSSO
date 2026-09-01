
package com.fragma.service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.fragma.entity.UserData;
import com.fragma.exception.UserAlreadyExistsException;
import com.fragma.exception.UserNotFoundException;
import com.fragma.repository.OauthConfigRepository;
import com.fragma.repository.UserDataRepository;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class AuthService {

    @Autowired
    private UserDataRepository repository;
    @Autowired
    private OauthConfigRepository oauthrepository;
    
    
    @Cacheable("oauthProviders")
    public List<Map<String, String>> getEnabledProviders() {

        log.info("Fetching enabled OAuth providers from database");

        return oauthrepository.findByIsEnabledTrue()
                .stream()
                .map(provider -> Map.of(
                        "id", provider.getProviderName(),
                        "name", provider.getDisplayName()
                ))
                .collect(Collectors.toList());
    }


    public UserData findUser(String email) {

        log.info("Searching for user profile");

        return repository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("User profile not found");
                    return new UserNotFoundException(
                            "User not found with email: " + email
                    );
                });
    }


    public UserData findUserIfExists(String email) {

        log.debug("Checking whether user already exists");

        UserData user = repository.findByEmail(email)
                .orElse(null);

        if (user != null) {
            log.info("Existing user found");
        } else {
            log.info("User does not exist");
        }

        return user;
    }


    public UserData createUser(
            String email,
            String name,
            String picture,
            String phoneNumber,
            String department,
            String designation) {

        log.info("Starting user registration");

        if (repository.findByEmail(email).isPresent()) {

            log.warn("Registration failed because user already exists");

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

        UserData savedUser = repository.save(user);

        log.info("User registration completed successfully");

        return savedUser;
    }
}
