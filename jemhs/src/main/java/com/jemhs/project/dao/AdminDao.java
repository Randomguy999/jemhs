package com.jemhs.project.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.jemhs.project.model.Admin;

@Transactional
public interface AdminDao extends JpaRepository<Admin, Long> {

	Admin findByuserName(String userName);
	
	@Modifying
	@Query("update Admin set password = ?1")
	int changepassword(String password);
	
}
