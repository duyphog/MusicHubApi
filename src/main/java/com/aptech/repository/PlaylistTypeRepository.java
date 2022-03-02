package com.aptech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aptech.entity.PlaylistType;

@Repository
public interface PlaylistTypeRepository extends JpaRepository<PlaylistType, Long> {
}
