package com.fragma.controller;
import java.io.IOException;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import com.fragma.dto.ProviderDataDTO;
import com.fragma.dto.RegistrationRequest;
import com.fragma.entity.UserData;
import com.fragma.exception.OAuthSessionException;
import com.fragma.repository.OauthConfigRepository;
import com.fragma.service.AuthService;


@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private AuthService authService;

    @GetMapping("/providers")
    public List<Map<String, String>> getEnabledProviders() {
        return authService.getEnabledProviders();
    }

    @Value("${oauth2.authorization-url}")
    private String oauth2AuthorizationUrl;

    @GetMapping("/login/{provider}")
    public void login(@PathVariable String provider,HttpServletResponse response) throws IOException {

        log.info("OAuth login request received for provider: {}", provider);

        ClientRegistration registration =clientRegistrationRepository.findByRegistrationId(provider);

        if (registration == null) {

            log.warn("OAuth provider not configured: {}", provider);

            response.sendError(HttpServletResponse.SC_NOT_FOUND, "OAuth provider not configured");

            return;
        }

        log.info("OAuth provider validated: {}", provider);

        log.info("Redirecting user to OAuth authorization endpoint");

        response.sendRedirect(oauth2AuthorizationUrl + provider);
    }


    @GetMapping("/profile")
    public UserData profile(@AuthenticationPrincipal OAuth2User oauthUser) {
            

        log.info("Profile request received");

        String email = oauthUser.getAttribute("email");
        
        if (email == null) {
            email = oauthUser.getAttribute("preferred_username");
        }

        if (email == null) {
            email = oauthUser.getAttribute("preferred_username");
        }

        log.debug("Fetching profile for authenticated user");

        return authService.findUser(email);
    }


    @PostMapping("/register")
    public UserData register(@RequestBody RegistrationRequest request,Authentication auth) {

        log.info("User registration request received");

        OAuth2User oauth2User =(OAuth2User) auth.getPrincipal();
                

        String email = oauth2User.getAttribute("email");
        
        if (email == null) {
            email = oauth2User.getAttribute("preferred_username");
        }

        if (email == null) {

            log.warn("OAuth email not available during registration");

            throw new OAuthSessionException( "OAuth email not available");
                  
        }

        String name = oauth2User.getAttribute("name");
        String picture = oauth2User.getAttribute("picture");

        log.info("Creating user account from OAuth profile");

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
    public ProviderDataDTO registrationData(Authentication auth) {
            

        log.info("Fetching OAuth registration data");

        OAuth2User oauth2User =(OAuth2User) auth.getPrincipal();
                

        String email = oauth2User.getAttribute("email");
        if (email == null) {
            email = oauth2User.getAttribute("preferred_username");
        }
        String name = oauth2User.getAttribute("name");
        String picture = oauth2User.getAttribute("picture");

        if (email == null) {

            log.warn("OAuth session missing email");

            throw new OAuthSessionException("OAuth login required");

        }

        return new ProviderDataDTO(
                name,
                email,
                picture
        );
    }
}






























//clientregistration will contain information roughly like:

//ClientRegistration
//├── registrationId = "google"
//├── clientId = "xxxxxxxx"
//├── clientSecret = "xxxxxxxx"
//├── clientName = "Google"
//├── authorizationGrantType = authorization_code
//├── redirectUri = "{baseUrl}/login/oauth2/code/{registrationId}"
//├── scopes
//│   ├── openid
//│   ├── profile
//│   └── email
//├── authorizationUri = "https://accounts.google.com/o/oauth2/v2/auth"
//├── tokenUri = "https://oauth2.googleapis.com/token"
//└── userInfoUri = "https://openidconnect.googleapis.com/v1/userinfo"
//
