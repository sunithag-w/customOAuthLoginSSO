package com.fragma.config;


import java.io.IOException;

import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fragma.entity.UserData;
import com.fragma.service.AuthService;


@Component
public class OAuth2SuccessHandler
        implements AuthenticationSuccessHandler {
    @Autowired
    private  AuthService authService;

   

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException {

        OAuth2AuthenticationToken oauthToken =
                (OAuth2AuthenticationToken) authentication;

        OAuth2User user =
                oauthToken.getPrincipal();

        String provider =
                oauthToken
                    .getAuthorizedClientRegistrationId();
        
    
        String email = null;
        String name = null;
        String picture = null;


        if (provider.equals("google")) {

            email = user.getAttribute("email");
            name = user.getAttribute("name");
            picture = user.getAttribute("picture");

        }


        else if (provider.equals("azure")) {
        	

            email = user.getAttribute("email");

            if (email == null) {
                email = user.getAttribute("preferred_username");
            }

            name = user.getAttribute("name");

        }


        else if (provider.equals("cognito")) {

            email = user.getAttribute("email");
            name = user.getAttribute("name");
            picture = user.getAttribute("picture");

        }


        if (email == null) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Email not received from OAuth provider"
            );

            return;
        }


        UserData existingUser =
                authService.findUser(email);


        if (existingUser != null) {

        	response.sendRedirect("http://localhost:5501/profile.html");

            return;
        }


        request.getSession().setAttribute(
                "OAUTH_EMAIL",
                email
        );

        request.getSession().setAttribute(
                "OAUTH_NAME",
                name
        );

        request.getSession().setAttribute(
                "OAUTH_PICTURE",
                picture
        );


        response.sendRedirect("http://localhost:5501/register.html");
    }
}