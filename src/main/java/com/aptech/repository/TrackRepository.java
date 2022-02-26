package com.aptech.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aptech.entity.Track;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {

	@Transactional
	@Modifying
	@Query(value = "UPDATE Track t SET t.listened = t.listened + 1 WHERE t.id = :id")
	public int AddListenedToId(@Param("id") Long id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE Track t SET t.liked = t.liked + 1 WHERE t.id = :id")
	public int AddLikedToId(@Param("id") Long id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE Track t SET t.liked = t.liked - 1 WHERE t.id = :id")
	public int RemoveLikedToId(@Param("id") Long id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE Track t SET t.isActive = false WHERE t.id = :id")
	public int DeactiveForId(@Param("id") Long id);

	public List<Track> findAllByAppStatusId(Long statusId);
}
