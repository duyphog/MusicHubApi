package com.aptech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aptech.entity.AppUser;
import com.aptech.entity.Track;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

	AppUser findByEmail(String email);

	AppUser findByUsername(String username);
	
	
	@Query(value = "SELECT t.* FROM track t INNER JOIN user_track ut ON t.id = ut.track_id  INNER JOIN app_user au ON ut.user_id  = au.id " + 
			"WHERE au.id = 3 ORDER BY ut.date_new DESC offset :offset rows fetch next :pageSize rows only", nativeQuery = true)
	public List<Track> findWhiteList(@Param("offset") Long offset, @Param("pageSize") Long pageSize);
}
