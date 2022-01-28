package com.comunidade.dto;

import java.sql.Date;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.comunidade.domain.Usuario;
import com.comunidade.enums.Nivel;
import com.comunidade.enums.Status;

public class InvitesDTO {
	
	private Integer id;
	
	private Usuario userId;
	
	private String receiverEmail;
	
	private int nivel;
	
	private Date expirationDate;
	
	private Date acceptDate;
	
	private String inviteCode;
	
	private Status status;
	
	private boolean seniorAproval;
	
	private boolean masterAproval;	
	
	public InvitesDTO() {}

	public InvitesDTO(Integer id, Usuario userId, String receiverEmail, int nivel, Date expirationDate,
			Date acceptDate, String inviteCode, Status status, boolean seniorAproval, boolean juniorAproval) {
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
	
	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
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

	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
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
	
}
