package com.comunidade.domain;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.comunidade.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Entity
public class Time implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String description;
	private float averageRating;
	
	@JsonIgnore
	private Boolean isRandom;
	
	private Date creationDate;
	
	@JsonIgnoreProperties(value = {"rg",
		    "birthDay",
		    "tell",
		    "cpf",
		    "email",
		    "validated",
		    "isLead",
		    "averageRating",
		    "statusUser",
		    "urlLatter",
		    "urlLinkedin",
		    "perfis",
		    "invites",
		    "squads",})
	private Usuario leadId;
	
	private Status status;
	
	@JsonIgnore
	@Getter
	@Setter
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinTable(name = "user_times", joinColumns = @JoinColumn(name = "time_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	Set<Usuario> users = new HashSet<>();
	
	@JsonIgnore
	@Getter
	@Setter
	@OneToMany(mappedBy = "time", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Squad> squads = new HashSet<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "time")
	private Set<Message> messages = new HashSet<>();
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vancacies_id", referencedColumnName = "id")
	private Vacancies vacancies;
	
	public Time() {
		
	}

	public Time(Integer id, String name, String description, float averageRating, Boolean isRandom, Date creationDate,
			Usuario leadId, Status status, Set<Usuario> users, Vacancies vacancies) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.averageRating = averageRating;
		this.isRandom = isRandom;
		this.creationDate = creationDate;
		this.leadId = leadId;
		this.status = status;
		this.users = users;
		this.vacancies = vacancies;
	}

	
	
	public Set<Message> getMessages() {
		return messages;
	}

	public void setMessages(Set<Message> messages) {
		this.messages = messages;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Usuario> getUsers() {
		return users;
	}

	public void setUsers(Set<Usuario> users) {
		this.users = users;
	}

	public Vacancies getVacancies() {
		return vacancies;
	}

	public void setVacancies(Vacancies vacancies) {
		this.vacancies = vacancies;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getAverageRating() {
		return averageRating;
	}
	public void setAverageRating(float averageRating) {
		this.averageRating = averageRating;
	}
	public Boolean getIsRandom() {
		return isRandom;
	}
	public void setIsRandom(Boolean isRandom) {
		this.isRandom = isRandom;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Usuario getLeadId() {
		return leadId;
	}
	public void setLeadId(Usuario leadId) {
		this.leadId = leadId;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}

	public Set<Squad> getSquads() {
		return squads;
	}

	public void setSquads(Set<Squad> squads) {
		this.squads = squads;
	}
		
}
