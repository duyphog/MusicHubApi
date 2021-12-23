package com.aptech.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aptech.entity.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, UUID> {

	VerificationToken findByToken(UUID token);
}
