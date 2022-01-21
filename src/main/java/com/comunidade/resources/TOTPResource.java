package com.comunidade.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.jackson.Jacksonized;



@RestController
@RequestMapping(value = "/totp")
public class TOTPResource {

    @Autowired
    private JavaMailSender mailSender;
    
    @Jacksonized
    @RequestMapping(value="/mail/{to}/{token}", method=RequestMethod.GET)
	public ResponseEntity<String> run(@PathVariable String to,  @PathVariable String token) {
        new Thread(new Runnable() {
        public void run() {

            // Usuario user = event.getUser();
            // System.out.println(user.getEmail());
            // String token = UUID.randomUUID().toString();
            // service.createVerificationToken(user, token);
            
            // String recipientAddress = user.getEmail();
            // String subject = "Confirmação de Email";
            // String confirmationUrl 
            //   = event.getAppUrl() + "/api/cadastro/confirmarregistro?token=" + token;
            //String message = messages.getMessage("message.regSucc", null, event.getLocale());
            
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(to);
            email.setSubject("Atualização do Token da Fennec");
            email.setText("Click no link para atualizar o Token");
            email.setText("\r\n" + "` <a href=\"http://3.12.228.3:5000/?token=`" + token);
            // email.setText(token);
            //Do whatever
            mailSender.send(email);
        }
    }).start();
    return ResponseEntity.ok().body(token);       
}
}

