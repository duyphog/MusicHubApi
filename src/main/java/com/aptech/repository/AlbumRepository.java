package com.aptech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aptech.entity.Album;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
	
	@Query("SELECT count(a) FROM Album a WHERE name = :albumName")
	public Long findContainsName(@Param("albumName") String albumName);
}
