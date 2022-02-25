package com.aptech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aptech.entity.Artist;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
	
	@Query("SELECT a FROM Artist a WHERE a.isActive = 1 and a.isSinger = 1 and a.nickName LIKE %:name%")
	public List<Artist> findSingerfindNickNameContaining(@Param("name") String name);
	
	@Query("SELECT a FROM Artist a WHERE a.isActive = 1 and a.isComposer = 1 and a.nickName LIKE %:name%")
	public List<Artist> findComposerByNickNameContaining(@Param("name") String name);
	
	@Query("SELECT a FROM Artist a WHERE a.isActive = 1 and a.isSinger = 1 and a.id = :id")
	public Artist findSingerById(@Param("id") Long id);
	
	@Query("SELECT a FROM Artist a WHERE a.isActive = 1 and a.isComposer = 1 and a.id = :id")
	public Artist findComposerById(@Param("id") Long id);
}
