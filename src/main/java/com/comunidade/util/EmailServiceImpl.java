package com.comunidade.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailServiceImpl {  //implements EmailService {
	
	
	
	
    public void sendSimpleMessage(
      String to, String subject, String text, JavaMailSender emailSender) {
        
    	new Thread(new Runnable() {
            public void run() {
		        
		       SimpleMailMessage email = new SimpleMailMessage();
		       email.setTo(to);
		       email.setSubject(subject);
		       email.setText(text);
		       emailSender.send(email);
		       
            }
        }).start();
    }
    
}