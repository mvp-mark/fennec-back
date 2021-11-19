package com.comunidade.domain;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.comunidade.enums.Status;

@Entity
public class Vacancies implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String description;
	private Date creationDate;
	private Date deadLine;
	private float price;
	private Client clienteId;
	private Squad squadId;
	private String urlRepository;
	private Status status;
	
	@OneToOne(mappedBy = "vacancies")
	private Squad squad;
	
	@OneToOne(mappedBy = "vacanciesByClient")
	private Client client;
	
	public Vacancies() {
		
	}
	
	

	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(Date deadLine) {
		this.deadLine = deadLine;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public Client getClienteId() {
		return clienteId;
	}

	public void setClienteId(Client clienteId) {
		this.clienteId = clienteId;
	}

	public Squad getSquadId() {
		return squadId;
	}

	public void setSquadId(Squad squadId) {
		this.squadId = squadId;
	}

	public String getUrlRepository() {
		return urlRepository;
	}

	public void setUrlRepository(String urlRepository) {
		this.urlRepository = urlRepository;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	
}
