package com.comunidade.enums;

public enum Status {
	ATIVO(1, "Usuario ativo"),
	INATIVO(2, "Usuario Inativo"),
	BLOQUEADO(3, "Usuario Bloqueado");
	
	private int cod;
	private String descricao;
	
	private Status(int cod, String descricao) {
		this.cod = cod;
		this.descricao = descricao;
	}
	
	public int getCod() {
		return cod;
	}
	
	public String getDescricao () {
		return descricao;
	}
	
	public static Status toEnum(Integer cod) {
		
		if (cod == null) {
			return null;
		}
		
		for (Status x : Status.values()) {
			if (cod.equals(x.getCod())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("Id inválido: " + cod);
	}
	
}
