package com.comunidade.services;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import com.comunidade.domain.Usuario;

public class OnInvitationCompleteEvent extends ApplicationEvent {
    private String appUrl;
    private Locale locale;
    private Usuario user;

    public OnInvitationCompleteEvent(
      Usuario user, Locale locale, String appUrl) {
        super(user);
        
        this.user = user;
        this.locale = locale;
        this.appUrl = appUrl;
    }

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public Usuario getUser() {
		return user;
	}

	public void setUser(Usuario user) {
		this.user = user;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
    
	
    // standard getters and setters
    
}