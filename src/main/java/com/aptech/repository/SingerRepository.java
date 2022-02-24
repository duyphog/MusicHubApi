package com.aptech.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aptech.entity.Singer;

@Repository
public interface SingerRepository extends JpaRepository<Singer, Long> {
	
	public List<Singer> findByStageNameContaining(String stageName);
}
