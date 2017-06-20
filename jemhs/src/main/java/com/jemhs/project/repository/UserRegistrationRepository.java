package com.jemhs.project.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jemhs.project.model.UserRegistration;

@Transactional
public interface UserRegistrationRepository extends JpaRepository<UserRegistration, Long>{

}
