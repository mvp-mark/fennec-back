package com.comunidade.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.comunidade.enums.Nivel;
import com.comunidade.enums.Perfil;
import com.comunidade.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.br.CPF;

@Entity
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	@Column(unique=true)
	private String rg;
	
	private Date birthDay;
	
	@Column(unique=true)
	private String tell;
	
	@CPF
	@Column(unique=true)
	@JsonIgnore
	private String cpf;

	@Column(unique = true)
	private String email;

	@JsonIgnore
	private String password;
	
	@JsonIgnore
	private Boolean validated;
	
	@JsonIgnore
	private Boolean isLead;
	
	private String hierarquia;
	
	private float averageRating;
	
	@JsonIgnore
	private Integer statusUser;
	
	private String urlLatter;
	
	private String urlLinkedin;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "PERFIS")
	private Set<Integer> perfis = new HashSet<>();
	
	//*@ElementCollection(fetch = FetchType.EAGER)
	//*@CollectionTable(name = "NIVEL")
	private Nivel nivel;// = new HashSet<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "userId")
	private Set<Invites> invites;

	@JsonIgnore
	@ManyToMany(mappedBy = "users")	
	Set<Squad> squads = new HashSet<>();
	
	@JsonIgnore
	@ManyToMany(mappedBy = "users")	
	Set<Time> times = new HashSet<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "usuarioId")
	private Set<Message> messages = new HashSet<>();
	
	@Enumerated(EnumType.ORDINAL)
	public Nivel getNivel() {
		return nivel;
	}

	public void setNivel(Nivel nivel) {
		this.nivel = nivel;
	}

	public String getHierarquia() {
		return hierarquia;
	}

	public void setHierarquia(String hierarquia) {
		this.hierarquia = hierarquia;
	}

	public Boolean isValidated() {
		return this.validated;
	}

	public Boolean isIsLead() {
		return this.isLead;
	}

	public void setStatusUser(Integer statusUser) {
		this.statusUser = statusUser;
	}

	public Set<Message> getMessages() {
		return this.messages;
	}

	public void setMessages(Set<Message> messages) {
		this.messages = messages;
	}

	public Set<Invites> getInvites() {
		return this.invites;
	}

	public void setInvites(Set<Invites> invites) {
		this.invites = invites;
	}

	public Set<Squad> getSquads() {
		return this.squads;
	}

	public void setSquads(Set<Squad> squads) {
		this.squads = squads;
	}
	
	public Set<Time> getTimes() {
		return times;
	}

	public void setTimes(Set<Time> times) {
		this.times = times;
	}
	
	public Usuario() {
	}

	public Usuario(Integer id, String name,
			String rg,
			Date birthDay,
			String tell,
			String cpf,
			String email, String password, int perfil, Boolean isValidated,
			Boolean isLead, String urlLatter, String urlLinkedin, float averageRating) {
		super();
		this.id = id;
		this.name = name;
		this.rg = rg;
		this.birthDay = birthDay;
		this.tell = tell;
		this.cpf = cpf;
		this.email = email;
		this.password = password;
		this.validated = isValidated;
		this.isLead = isLead;
		this.urlLatter = urlLatter;
		this.urlLinkedin = urlLinkedin;
		this.averageRating = averageRating;
		addPerfil(Perfil.toEnum(perfil));
	}
	
	/*
	public Set<Integer> getNivel() {
		return nivel;
	}

	public void setNivel(Set<Integer> nivel) {
		this.nivel = nivel;
	}
	*/

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

	public Set<Perfil> getPerfis() {
		return perfis.stream().map(x -> Perfil.toEnum(x)).collect(Collectors.toSet());
	}

	public void addPerfil(Perfil perfil) {
		perfis.add(perfil.getCod());
	}
	/*
	public void addNivel(Nivel nivel) {
		perfis.add(nivel.getCod());
	}*/

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

	public void setPerfis(Set<Integer> perfis) {
		this.perfis = perfis;
	}

	public String getRg() {
		return this.rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public Date getBirthDay() {
		return this.birthDay;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}

	public String getTell() {
		return this.tell;
	}

	public void setTell(String tell) {
		this.tell = tell;
	}

	public String getCpf() {
		return this.cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
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
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
