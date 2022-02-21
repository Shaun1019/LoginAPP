package com.registration.controller;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.registration.TempOTP;
import com.registration.UserOTPRepository;
import com.registration.UserRepository;
import com.registration.dto.User;
import com.registration.dto.UserOTP;
import com.registration.service.SmsService;


@Controller
public class AppController implements ErrorController{
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private UserOTPRepository repoOTP;
  
  
	
	@Autowired
    SmsService service;
	
	User user1;
	
	TempOTP sentotp;
	
	@GetMapping("")
	public String viewHomePage() {
		return "index";
	}
	
	@GetMapping("/register")
	public String showSignUpForm(Model model) {
		model.addAttribute("user", new User());
		return "signup_form";
	}
	
	
	@RequestMapping(path="/login", method = RequestMethod.GET )
	public String userLogin() {
		
		System.out.print("yooo");
		return "users";
	}
	
	
	@PostMapping("/process_register")
	public String processRegistration(User user, SmsService sms) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPassword = encoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		
		 try{
	        	System.out.println("hello");
	               service.send(user);
	            System.out.println("hello");
	        }
	        catch(Exception e){

	        	 
	        	return "error_page";
	        }
		 
		 
		user1=user;
	    //repo.save(user);
		return "otp_confirmation";
	}
	
	
	
	@GetMapping("/users")
	public String listUsers(Model model) {
       
	    return "users";
	}
	
	
	@RequestMapping(path="/otp_confirmation", method = RequestMethod.POST )
	public String UserConfirmation( @RequestParam("otp") String recivedOTP) {
		
		UserOTP otp= repoOTP.findByEmail(user1.getEmail());
		
		String otpSent= otp.getOtp();
		
		if(otpSent.equalsIgnoreCase(recivedOTP)) {
			repo.save(user1);
			return "register_sucess";
		}
		
		return "register_unsucessfull";
		
		
				
	}
	

}

