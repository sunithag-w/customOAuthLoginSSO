package com.fragma.config;


import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fragma.entity.UserData;
import com.fragma.service.AuthService;


@Slf4j
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private AuthService authService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        log.info("OAuth authentication successful");

        OAuth2AuthenticationToken oauthToken =(OAuth2AuthenticationToken) authentication;
                

        OAuth2User user = oauthToken.getPrincipal();

        String provider =oauthToken.getAuthorizedClientRegistrationId();
                

        log.info("OAuth provider authentication completed: {}", provider);

        String email = user.getAttribute("email");

        if (email == null) {
            email = user.getAttribute("preferred_username");
        }

        String name = user.getAttribute("name");
        String picture = user.getAttribute("picture");


        if (email == null) {

            log.warn(
                "OAuth authentication completed but email was not received from provider: {}",
                provider
            );

            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Email not received from OAuth provider");
            

            return;
        }


        log.info("Checking whether OAuth user is already registered or not");


        UserData existingUser = authService.findUserIfExists(email);
               


        if (existingUser != null) {

            log.info("Existing user detected. Redirecting to profile page");

            response.sendRedirect( "http://localhost:5501/profile.html");

            return;
        }


        log.info("New OAuth user detected. Redirecting to registration page");

        response.sendRedirect("http://localhost:5501/register.html");
                
     
    }

    }
