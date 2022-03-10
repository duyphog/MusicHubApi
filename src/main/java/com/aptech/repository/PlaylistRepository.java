package com.aptech.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aptech.entity.AppUser;
import com.aptech.entity.Playlist;
import com.aptech.entity.PlaylistType;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
	Playlist findByName(String playlistName);

	List<Playlist> findAllByPlaylistTypeAndIsPublicTrue(PlaylistType playlistType);

	public Page<Playlist> findAllByAppUser(AppUser appUser, Pageable pageable);
}
