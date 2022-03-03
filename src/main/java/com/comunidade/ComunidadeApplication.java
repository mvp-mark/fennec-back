package com.comunidade;

import java.io.IOException;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ComunidadeApplication  extends SpringBootServletInitializer implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(ComunidadeApplication.class, args);
	}

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ComunidadeApplication.class);
    }
	
	@Override
	public void run(String... args) throws Exception {
      try {
         Init.initAdminSdk();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
