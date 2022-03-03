package com.comunidade.dto;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import com.comunidade.domain.Usuario;

public class FbaseTokenDTO {
	private Integer id;
	private String token;
	private Usuario usuario;
	private String timestamp;
	private boolean mute;
	
	public FbaseTokenDTO(Integer id, String token, Usuario usuario, String timestamp, boolean mute) {
		super();
		this.id = id;
		this.token = token;
		this.usuario = usuario;
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
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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
