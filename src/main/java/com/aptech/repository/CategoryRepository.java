package com.aptech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aptech.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
