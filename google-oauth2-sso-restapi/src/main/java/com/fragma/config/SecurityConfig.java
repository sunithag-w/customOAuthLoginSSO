package com.fragma.config;



import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,OAuth2SuccessHandler successhandler)
            throws Exception {

    	 http
         .cors(Customizer.withDefaults())
         .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth

            		.requestMatchers(
                            "/",
                            "/index.html",
                            "/register.html",
                            "/css/**",
                            "/js/**",
                            "/images/**",
                            "/oauth2/**",
                            "/login/**",
                            "/api/auth/login/**"
                        ).permitAll()

                // Everything else requires login
                .anyRequest().authenticated()
            )

            .oauth2Login(oauth -> oauth
                .successHandler(successhandler)
                
                
            )
            .logout(logout -> logout
                    .logoutSuccessUrl("http://localhost:5501/index.html")
                    .permitAll()
                );

        return http.build();
    }

 
}

