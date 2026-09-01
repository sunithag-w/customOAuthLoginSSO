package com.fragma.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fragma.entity.UserData;

public interface UserDataRepository extends JpaRepository<UserData, Integer>{

	 Optional<UserData> findByEmail(String email);
}
