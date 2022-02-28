package com.aptech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aptech.entity.Artist;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
	
	@Query(value = "SELECT * FROM artist a WHERE is_singer = 1 AND is_active = 1 AND nick_name COLLATE SQL_Latin1_General_CP1_CI_AI LIKE %:name% COLLATE SQL_Latin1_General_CP1_CI_AI ORDER BY nick_name", nativeQuery = true)
	public List<Artist> findSingerByNickNameContaining(@Param("name") String name);
	
	@Query(value = "SELECT * FROM artist a WHERE is_composer = 1 AND is_active = 1 AND nick_name COLLATE SQL_Latin1_General_CP1_CI_AI LIKE %:name% COLLATE SQL_Latin1_General_CP1_CI_AI ORDER BY nick_name", nativeQuery = true)
	public List<Artist> findComposerByNickNameContaining(@Param("name") String name);
	
	@Query("SELECT a FROM Artist a WHERE a.isActive = 1 and a.isSinger = 1 and a.id = :id")
	public Artist findSingerById(@Param("id") Long id);
	
	@Query("SELECT a FROM Artist a WHERE a.isActive = 1 and a.isComposer = 1 and a.id = :id")
	public Artist findComposerById(@Param("id") Long id);
	
	@Query("SELECT a FROM Artist a WHERE a.isActive = 1 and a.isComposer = 1 and a.nickName = :nickName")
	public Artist findComposerByNickName(@Param("nickName") String nickName); 
	
	@Query("SELECT a FROM Artist a WHERE a.isActive = 1 and a.isSinger = 1 and a.nickName = :nickName")
	public Artist findSingerByNickName(@Param("nickName") String nickName); 
}
