package com.registration.controller;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.registration.TempOTP;
import com.registration.TempUserDetails;
import com.registration.UserBean;
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
	
	@Autowired
	UserBean tempUser;
	
	
	
	
	//handler to handle our home page with a default url
	@GetMapping("")
	public String viewHomePage() {
		return "index";
	}
	
	
	//handler to handle send request for a new registration in db
	@GetMapping("/register")
	public String showSignUpForm(Model model) {
		model.addAttribute("user", new User());
		return "signup_form";
	}
	
	
	
	//for the login protection on user details
	@RequestMapping(path="/login", method = RequestMethod.GET )
	public String userLogin() {
		org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			
			return "login";
		}
		
		return "users";
	}
	
	//handler to authenticate the password and pass the username value to the next handler
	@RequestMapping(path="/loginX", method = RequestMethod.POST )
	public String userLoginX(@RequestParam("email") String username, @RequestParam("password") String password) {
		
		User obj= repo.findByEmail(username);
		String dbPassword = obj.getPassword();
		tempUser.setEmail(username);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		boolean encodedPassword = encoder.matches(password, dbPassword);
		if(encodedPassword) {
		 return "redirect:/users";
		}	
		return "login";
	}
	
	
	
	//handler to send the otp to the user for the registration process
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
		return "otp_confirmation";
	}
	
	
	//handler to authenticate  the otp
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
	
	//handler to show all the details of user
	@GetMapping("/users")
	public String showUserDetails(Model model) {
		
		String email = tempUser.getEmail();
		
		System.out.println(email);
	    User listUsers = repo.findByEmail(email);
	    model.addAttribute("listUsers", listUsers);
		     
	    return "users";
	}
	
}

