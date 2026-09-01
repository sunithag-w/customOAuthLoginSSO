package com.fragma.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.fragma.entity.OauthProviderEntity;

import java.util.List;

public interface OauthConfigRepository extends JpaRepository<OauthProviderEntity, Long> {
    List<OauthProviderEntity> findByIsEnabledTrue();
}