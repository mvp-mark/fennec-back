package com.comunidade.dto;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.comunidade.domain.Squad;
import com.comunidade.domain.Usuario;
import com.comunidade.domain.Vacancies;
import com.comunidade.enums.Status;

public class TimeDTO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String description;
	private float averageRating;
	private Boolean isRandom;
	private Date creationDate;
	private Usuario leadId;
	private Status status;
	List<Usuario> users = new ArrayList<>() ;
	List<Squad> squads = new ArrayList<>() ;
	private Vacancies vacancies;
	
	public Vacancies getVacancies() {
		return vacancies;
	}
	public void setVacancies(Vacancies vacancies) {
		this.vacancies = vacancies;
	}
	public TimeDTO(String name, String description, Usuario leadId, List<Usuario> users) {
		super();
		this.name = name;
		this.description = description;
		this.leadId = leadId;
		this.users = users;
	}
	public TimeDTO() {
		super();
	}
	public TimeDTO(Integer id, String name, String description, float averageRating, Boolean isRandom, Date creationDate,
			Usuario leadId, Status status, List<Usuario> users) {
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
	}
	public Integer getId() {
		return id;
	}
	
	
	public List<Squad> getSquads() {
		return squads;
	}
	public void setSquads(List<Squad> squads) {
		this.squads = squads;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public List<Usuario> getUsers() {
		return users;
	}
	public void setUsers(List<Usuario> users) {
		this.users = users;
	}
	
	
	
}
