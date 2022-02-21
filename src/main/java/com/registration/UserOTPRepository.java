package com.registration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.registration.dto.User;
import com.registration.dto.UserOTP;

public interface UserOTPRepository extends JpaRepository<UserOTP, Long> {
	
	@Query("SELECT u FROM UserOTP u WHERE u.email =?1")
	UserOTP findByEmail(String email);

}
