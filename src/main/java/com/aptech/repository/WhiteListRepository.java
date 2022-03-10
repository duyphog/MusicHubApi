package com.aptech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aptech.entity.AppUser;
import com.aptech.entity.Track;
import com.aptech.entity.WhiteList;

@Repository
public interface WhiteListRepository extends JpaRepository<WhiteList, Long> {

	public boolean existsByAppUserAndTrack(AppUser appUser, Track track);
	
	public Integer deleteByAppUserAndTrack(AppUser appUser, Track track);
}
