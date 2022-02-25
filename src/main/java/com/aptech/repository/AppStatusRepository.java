package com.aptech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.aptech.entity.AppStatus;

@Repository
public interface AppStatusRepository extends JpaRepository<AppStatus, Long> {

	@Query("SELECT a FROM AppStatus a WHERE a.isDefault = 1")
	public AppStatus getDefaultAppStatus();
}
