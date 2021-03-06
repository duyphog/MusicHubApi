package com.aptech.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aptech.entity.Album;
import com.aptech.entity.Artist;
import com.aptech.entity.Category;
import com.aptech.entity.Genre;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

	public boolean existsByName(String albumName);
	
	public List<Album> findAllByAppStatusId(Long appStatusId);
	
	public Page<Album> findAllByIsActiveTrueAndCategory(Category category, Pageable pageable);
	
	public Page<Album> findAllByIsActiveTrueAndCategoryAndGenres(Category category, Genre genre, Pageable pageable);
	
	public Page<Album> findAllByIsActiveTrueAndSingersContaining(Artist singer, Pageable pageable);
}
