package com.aptech.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aptech.entity.Album;
import com.aptech.entity.AppUser;
import com.aptech.entity.Artist;
import com.aptech.entity.Category;
import com.aptech.entity.Genre;
import com.aptech.entity.Track;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {

	@Transactional
	@Modifying
	@Query(value = "UPDATE Track t SET t.listened = t.listened + 1 WHERE t.id = :id")
	public int addListenedToId(@Param("id") Long id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE Track t SET t.liked = t.liked + 1 WHERE t.id = :id")
	public int addLikedToId(@Param("id") Long id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE Track t SET t.liked = t.liked - 1 WHERE t.id = :id")
	public int removeLikedToId(@Param("id") Long id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE Track t SET t.isActive = false WHERE t.id = :id")
	public int deactiveForId(@Param("id") Long id);

	public List<Track> findAllByAppStatusId(Long statusId);
	
	public List<Track> findAllByIsActiveTrueAndAlbum(Album album);
	
	public Page<Track> findAllByIsActiveTrueAndCategory(Category category, Pageable pageable);
	
	public Page<Track> findAllByIsActiveTrueAndCategoryAndGenres(Category category, Genre genre, Pageable pageable);
	
	public List<Track> findTop20ByIsActiveTrueAndCategoryOrderByListenedDesc(Category category);
	
	@Query("SELECT w.track FROM WhiteList w WHERE w.appUser =:appUser ORDER BY w.dateNew DESC")
	public Page<Track> findWhiteListByUser(@Param("appUser") AppUser appUser, Pageable pageable);
	
	@Query("SELECT w.track.id FROM WhiteList w WHERE w.appUser =:appUser")
	public List<Long> findAllTrackIdInWhiteListByUser(@Param("appUser") AppUser appUser);
	
	public Page<Track> findAllByIsActiveTrueAndSingersContaining(Artist singer, Pageable pageable);
}
