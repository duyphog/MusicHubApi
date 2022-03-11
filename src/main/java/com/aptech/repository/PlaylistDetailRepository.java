package com.aptech.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aptech.entity.Playlist;
import com.aptech.entity.PlaylistDetail;
import com.aptech.entity.Track;

@Repository
public interface PlaylistDetailRepository extends JpaRepository<PlaylistDetail, Long> {
	@Transactional
	void deleteByPlaylistAndTrack(Playlist playlist, Track track);
	
	@Query("select case when count(c)> 0 then true else false end from PlaylistDetail c where c.playlist = :playlist and c.track = :track")
	boolean existByPlaylistAndTrack(Playlist playlist, Track track);
}
