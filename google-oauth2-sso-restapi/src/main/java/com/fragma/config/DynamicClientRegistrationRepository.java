package com.fragma.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.stereotype.Component;

import com.fragma.entity.OauthProviderEntity;
import com.fragma.repository.OauthConfigRepository;

@Component
public class DynamicClientRegistrationRepository implements ClientRegistrationRepository {
        
    @Autowired
    private  OauthConfigRepository dbRepository;

   
    @Override
    @Cacheable(value = "oauthConfigCache", key = "#registrationId")
    public ClientRegistration findByRegistrationId(String registrationId) {

        return dbRepository.findByIsEnabledTrue()
                .stream()
                .filter(provider ->
                        provider.getProviderName()
                                .equalsIgnoreCase(registrationId))
                .findFirst()
                .map(this::toClientRegistration)
                .orElse(null);
    }

    private ClientRegistration toClientRegistration(OauthProviderEntity entity) {
            

        return ClientRegistration
                .withRegistrationId(entity.getProviderName().toLowerCase())
                .clientId(entity.getClientId())
                .clientSecret(entity.getClientSecret())
                .authorizationGrantType( AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri(entity.getRedirectUri())
                .scope(entity.getScopes().split(","))
                .authorizationUri(entity.getAuthorizationUri())
                .tokenUri(entity.getTokenUri())
                .jwkSetUri(entity.getJwkSetUri())
                .build();
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

