package com.jemhs.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jemhs.project.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{
	Role findByRole(String role);

}