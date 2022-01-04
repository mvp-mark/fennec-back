package com.comunidade.dto;

import java.io.Serializable;

public class CredenciaisDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	// private String email;
	private String tell;
	private String senha;
	
	public CredenciaisDTO() {
	}
	
	// public String getEmail() {
	// 	return email;
	// }
	// public void setEmail(String email) {
	// 	this.email = email;
	// }


	public String getTell() {
		return tell;
	}

	public void setTell(String tell) {
		this.tell = tell;
	}

	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
}
