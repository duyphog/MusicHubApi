package com.aptech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aptech.entity.Playlist;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
	
}
