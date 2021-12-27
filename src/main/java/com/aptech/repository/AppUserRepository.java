package com.aptech.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aptech.entity.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {

	AppUser findByEmail(String email);

	AppUser findByUsername(String username);
}
