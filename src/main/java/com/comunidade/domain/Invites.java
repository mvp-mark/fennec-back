package com.comunidade.domain;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.comunidade.enums.Nivel;
import com.comunidade.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
public class Invites implements Serializable {
	private static final int EXPIRATION = 60 * 24;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	//Usuario que criou o invite
	@ManyToOne
    @JoinColumn(name = "user_id")
	private Usuario userId;
	
	private String receiverEmail;
	
	private Nivel nivel;
	
	private Date expirationDate;
	private Date acceptDate;
	private String inviteCode;
	private Status status;
	private boolean seniorAproval;
	private boolean masterAproval;	
	
	public Invites() {}
	
	public Invites(Integer id, Usuario userId, String receiverEmail, Nivel nivel, Date expirationDate, Date acceptDate,
			String inviteCode, Status status, boolean seniorAproval, boolean juniorAproval) {
		super();
		this.id = id;
		this.userId = userId;
		this.receiverEmail = receiverEmail;
		this.nivel = nivel;
		this.expirationDate = expirationDate;
		this.acceptDate = acceptDate;
		this.inviteCode = inviteCode;
		this.status = status;
		this.seniorAproval = seniorAproval;
		this.masterAproval = juniorAproval;
	}

	public Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }
	
	@Enumerated(EnumType.ORDINAL)
	public Nivel getNivel() {
		return nivel;
	}

	public void setNivel(Nivel nivel) {
		this.nivel = nivel;
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	public boolean isSeniorAproval() {
		return seniorAproval;
	}

	public void setSeniorAproval(boolean seniorAproval) {
		this.seniorAproval = seniorAproval;
	}

	public boolean isMasterAproval() {
		return masterAproval;
	}

	public void setMasterAproval(boolean masterAproval) {
		this.masterAproval = masterAproval;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Usuario getUserId() {
		return userId;
	}

	public void setUserId(Usuario userId) {
		this.userId = userId;
	}

	public String getReceiverEmail() {
		return receiverEmail;
	}

	public void setReceiverEmail(String receiverEmail) {
		this.receiverEmail = receiverEmail;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Date getAcceptDate() {
		return acceptDate;
	}

	public void setAcceptDate(Date acceptDate) {
		this.acceptDate = acceptDate;
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}