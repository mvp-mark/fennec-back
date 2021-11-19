package com.comunidade.enums;

public enum Perfil {
	 
	DEVELOPER_FULLSTACK(1, "FULLSTACK"),
	DEVELOPER_FRONTEND(2, "FRONTED"),
	DEVELOPER_BACKEND(3, "BACKEND"),
	DESIGNER(4, "DESIGNER"),
	MANAGER(5, "MANAGER");
	
	
	private int cod;
	private String descricao;
	
	private Perfil(int cod, String descricao) {
		this.cod = cod;
		this.descricao = descricao;
	}
	
	public int getCod() {
		return cod;
	}
	
	public String getDescricao () {
		return descricao;
	}
	
	public static Perfil toEnum(Integer cod) {
		
		if (cod == null) {
			return null;
		}
		
		for (Perfil x : Perfil.values()) {
			if (cod.equals(x.getCod())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("Id inválido: " + cod);
	}

}
