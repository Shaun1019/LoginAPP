package com.registration;

import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {

	public static void amin(String[] args) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String rawPassword ="nam2020";
		String encodedpassword = encoder.encode(rawPassword);
		
		System.out.print(encodedpassword);
		
	}
	
}
