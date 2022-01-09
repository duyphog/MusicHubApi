package com.aptech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aptech.entity.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
}
