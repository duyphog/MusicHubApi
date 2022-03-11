package com.aptech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aptech.entity.AppUser;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

	AppUser findByEmail(String email);

	AppUser findByUsername(String username);
}
