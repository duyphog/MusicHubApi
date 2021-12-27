package com.aptech.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aptech.entity.AppUser;
import com.aptech.entity.UserInfo;

public interface UserInfoRepository extends JpaRepository<UserInfo, UUID> {

//	@Query("select u from UserInfo u where u.appuser_id = :appUserID")
//	UserInfo findByAppUserId(@Param("appUserID") UUID appUserID);
}
