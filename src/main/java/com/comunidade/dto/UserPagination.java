package com.comunidade.dto;

import java.util.List;

import com.comunidade.domain.Usuario;


public class UserPagination {

	
	private List<Usuario> usuarios;
	private int totalPages;
	private int totalElements;
	
	
	public UserPagination(List<Usuario> usuarios, int totalPages, int totalElements) {
		super();
		this.usuarios = usuarios;
		this.totalPages = totalPages;
		this.totalElements = totalElements;
	}


	public List<Usuario> getUsuarios() {
		return usuarios;
	}


	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}


	public int getTotalPages() {
		return totalPages;
	}


	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}


	public int getTotalElements() {
		return totalElements;
	}


	public void setTotalElements(int totalElements) {
		this.totalElements = totalElements;
	}
	
	
		
		
}
