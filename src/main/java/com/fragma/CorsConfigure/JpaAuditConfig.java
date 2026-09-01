package com.fragma.CorsConfigure;

import org.springframework.context.annotation.Configuration;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;



import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Configuration
@EnableJpaAuditing
public class JpaAuditConfig {

    @Bean
    public AuditorAware<String> auditorAware() {

        return () -> {

            Authentication authentication =
                    SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null ||
                    !authentication.isAuthenticated()) {

                return Optional.empty();
            }

            Object principal = authentication.getPrincipal();

            if (principal instanceof OAuth2User oauth2User) {

                String name = oauth2User.getAttribute("name");

                if (name == null) {
                    name = oauth2User.getAttribute("displayName");
                }

                if (name == null) {
                    name = oauth2User.getAttribute("preferred_username");
                }

                return Optional.ofNullable(name);
            }

            return Optional.of(authentication.getName());
        };
    }
}