package com.registration;

import javax.persistence.Column;

import org.springframework.stereotype.Component;

@Component
public class UserBean {
	
	
	
	private String username;
	
	private String id;
	
	private String email;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
}
