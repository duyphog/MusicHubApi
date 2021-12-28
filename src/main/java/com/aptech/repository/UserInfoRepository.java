package com.aptech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aptech.entity.UserInfo;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

//	@Query("select u from UserInfo u where u.appuser_id = :appUserID")
//	UserInfo findByAppUserId(@Param("appUserID") UUID appUserID);
}