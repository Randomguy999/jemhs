package com.jemhs.project.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jemhs.project.model.PasswordResetToken;

@Transactional
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long>{

	PasswordResetToken findByUserName (String userName);
	
	Long deleteByUserName(String userName);
	
	/*@Query(nativeQuery = true,value="")
	void deleteByExpiryDate();*/
	
}
