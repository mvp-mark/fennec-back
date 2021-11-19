package com.comunidade.domain;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import com.comunidade.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
public class Squad implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private float averageRating;
	private Boolean isRandom;
	private Date creationDate;
	private Usuario leadId;
	private Status status;
	
	@JsonIgnore
	@Getter
	@Setter
	@ManyToMany(mappedBy = "squads")	
    List<Usuario> users = new ArrayList<>() ;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vancacies_id", referencedColumnName = "id")
	private Vacancies vacancies;
	
	public Squad() {
		
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
	
	
}
