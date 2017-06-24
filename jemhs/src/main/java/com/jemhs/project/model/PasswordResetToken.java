package com.jemhs.project.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PasswordResetToken {
  
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
  
    private String token;
  
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private String userName;
    public String getUser_name() {
		return userName;
	}

	public void setUser_name(String userName) {
		this.userName = userName;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	private Date expiryDate;
}
