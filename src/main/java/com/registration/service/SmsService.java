package com.registration.service;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import com.registration.TempOTP;
import com.registration.UserOTPRepository;
import com.registration.dto.User;
import com.registration.dto.UserOTP;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Component
public class SmsService {
     private final String ACCOUNT_SID ="ACf4aebee92289d0e2cac166359bf889c9";

    private final String AUTH_TOKEN = "8ec5010053bcfc4eea2ce452efb0fb17";

    private final String FROM_NUMBER = "+19106598779";
    
    @Autowired
	private UserOTPRepository repoOTP;
  

    public void send(User sms) throws ParseException {
    	Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
      
    	
        int min = 100000;  
         int max = 999999; 
        int number=(int)(Math.random()*(max-min+1)+min);
      
        
        String msg ="Your OTP - "+number+ " please verify this OTP in your Application by Shaurya";
       
        
        Message message = Message.creator(new PhoneNumber(sms.getMobileNumber()), new PhoneNumber(FROM_NUMBER), msg).create();
       
         
        UserOTP otp = new UserOTP();
        otp.setEmail(sms.getEmail());
        otp.setOtp(String.valueOf(number));
        repoOTP.save(otp);
        
    }

    

}
