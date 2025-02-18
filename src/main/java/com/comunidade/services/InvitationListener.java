package com.comunidade.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class InvitationListener  implements 
ApplicationListener<OnInvitationCompleteEvent> {
	//@Autowired
    //private IUserService service;
 
    @Autowired
    private MessageSource messages;
 
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnInvitationCompleteEvent event) {
        this.confirmRegistration(event);
    }
    
    private void confirmRegistration(OnInvitationCompleteEvent event) {
        //Usuario user = event.getUsuario();
        //String token = UUID.randomUUID().toString();
        //service.createVerificationToken(user, token);
        
        //String recipientAddress = user.getEmail();
    	String recipientAddress = "";
        String subject = "Registration Confirmation";
        String confirmationUrl = event.getAppUrl() + "/regitrationConfirm.html?token=";
        String message = messages.getMessage("message.regSucc", null, event.getLocale());
        
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + "http://localhost:8080" + confirmationUrl);
        mailSender.send(email);
    }
}
