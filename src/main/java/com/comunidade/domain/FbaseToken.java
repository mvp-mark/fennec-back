package com.comunidade.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;

@Entity
public class FbaseToken {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String token;
	
	@OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fbaseToken")
	private Usuario user;
	
	private String timestamp;
	
	private boolean mute;
	
	public FbaseToken() {
		
	}
	
	public FbaseToken(Integer id, String token, Usuario usuario, String timestamp, boolean mute) {
		super();
		this.id = id;
		this.token = token;
		this.user = usuario;
		this.timestamp = timestamp;
		this.mute = mute;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	
	public Usuario getUser() {
		return user;
	}

	public void setUser(Usuario user) {
		this.user = user;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isMute() {
		return mute;
	}

	public void setMute(boolean mute) {
		this.mute = mute;
	}
}
