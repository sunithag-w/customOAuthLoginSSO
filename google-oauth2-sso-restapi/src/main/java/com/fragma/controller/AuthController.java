
package com.fragma.controller;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.fragma.dto.RegistrationRequest;
import com.fragma.entity.UserData;
import com.fragma.exception.OAuthSessionException;
import com.fragma.service.AuthService;



@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;
    @Autowired
    private  AuthService authService;        


    @GetMapping("/login/{provider}")
    public void login(@PathVariable String provider,HttpServletResponse response) throws IOException {
            
              ClientRegistration registration = clientRegistrationRepository .findByRegistrationId(provider);
               
                  if (registration == null) {

                       response.sendError( HttpServletResponse.SC_NOT_FOUND,"OAuth provider not configured");
                             return;
                    }
                   response.sendRedirect("/oauth2/authorization/" + provider);
    }


    @GetMapping("/profile")
    public UserData profile(
            @AuthenticationPrincipal OAuth2User oauthUser) {

        String email =
                oauthUser.getAttribute("email");

        return authService.findUser(email);
    }
    @PostMapping("/register")
    public UserData register(
            @RequestBody RegistrationRequest request,
            HttpSession session) {

        String email =
                (String) session.getAttribute("OAUTH_EMAIL");

        if (email == null) {
            throw new OAuthSessionException(
                    "OAuth login required"
            );
        }
        
        String name =
                (String) session.getAttribute("OAUTH_NAME");

        String picture =
                (String) session.getAttribute("OAUTH_PICTURE");

        return authService.createUser(
                email,
                name,
                picture,
                request.getPhoneNumber(),
                request.getDepartment(),
                request.getDesignation()
        );
    }
    
    @GetMapping("/registration-data")
    public Map<String, String> registrationData(
            HttpSession session) {

        String email =
                (String) session.getAttribute("OAUTH_EMAIL");

        if (email == null) {
            throw new OAuthSessionException(
                    "OAuth login required"
            );
        }

        Map<String, String> data = new HashMap<>();

        data.put(
                "name",
                (String) session.getAttribute("OAUTH_NAME")
        );

        data.put(
                "email",
                email
        );

        data.put(
                "picture",
                (String) session.getAttribute("OAUTH_PICTURE")
        );

        return data;
    }
}


