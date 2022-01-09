package com.aptech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aptech.entity.Album;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
}
