package com.fragma.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "oauth_providers")
@Data
public class OauthProviderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String providerName; 
    
    @Column(nullable = false)
    private String displayName; 
    
    @Column(name = "is_enabled", nullable = false)
    private boolean isEnabled;
    
    private String clientId;
    private String clientSecret;
    private String authorizationUri;
    private String tokenUri;
    private String jwkSetUri;
    private String scopes;      
    private String redirectUri;
    
}
