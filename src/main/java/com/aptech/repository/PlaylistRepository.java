package com.aptech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aptech.entity.Playlist;
import com.aptech.entity.PlaylistType;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
	Playlist findByName(String playlistName);
	
	List<Playlist> findAllByPlaylistType(PlaylistType playlistType);
}
