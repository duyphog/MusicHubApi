package com.aptech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aptech.entity.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
	
	@Query(value = "SELECT * FROM genre a WHERE name COLLATE SQL_Latin1_General_CP1_CI_AI LIKE %:name% COLLATE SQL_Latin1_General_CP1_CI_AI", nativeQuery = true)
	public List<Genre> findByName(@Param("name") String name);
}
