package com.comunidade.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.comunidade.enums.Perfil;
import com.comunidade.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;


public class UsuarioDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	
	private Integer id;
	
	private String name;	

	private String email;		

	private String password;
	
	private Boolean validated;
	
	private Boolean isLead;
	
	private float averageRating;
	
	private Integer statusUser;
	
	private String urlLatter;
	
	private String urlLinkedin;
	
	private int tipo;
	
	public UsuarioDTO() {
	}

	public UsuarioDTO(Integer id, String name, String email, String password, int tipo, Boolean isValidated, Boolean isLead) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.validated = isValidated;
		this.isLead = isLead;
		this.tipo = tipo;
	
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	

	


	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public Boolean getValidated() {
		return validated;
	}

	public void setValidated(Boolean validated) {
		this.validated = validated;
	}	
	
	public Status getStatusUser() {
		return Status.toEnum(statusUser);
	}

	public void addStatus(Status statusl) {
		statusUser = statusl.getCod();
	}
	
	public Boolean getIsLead() {
		return isLead;
	}

	public void setIsLead(Boolean isLead) {
		this.isLead = isLead;
	}

	public float getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(float averageRating) {
		this.averageRating = averageRating;
	}

	public String getUrlLatter() {
		return urlLatter;
	}

	public void setUrlLatter(String urlLatter) {
		this.urlLatter = urlLatter;
	}

	public String getUrlLinkedin() {
		return urlLinkedin;
	}

	public void setUrlLinkedin(String urlLinkedin) {
		this.urlLinkedin = urlLinkedin;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsuarioDTO other = (UsuarioDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
