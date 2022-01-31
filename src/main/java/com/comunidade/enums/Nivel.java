package com.comunidade.enums;

public enum Nivel {
	MASTER(1, "MASTER"),
	SENIOR(2, "SENIOR"),
	JUNIOR(3, "JUNIOR"),
	APRENDIZ(4, "APRENDIZ");
	
	private int cod;
	private String descricao;
	
	private Nivel(int cod, String descricao) {
		this.cod = cod;
		this.descricao = descricao;
	}

	public int getCod() {
		return cod;
	}

	public void setCod(int cod) {
		this.cod = cod;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public static Nivel toEnum(Integer cod) {
		
		if (cod == null) {
			return null;
		}
		
		for (Nivel x : Nivel.values()) {
			if (cod.equals(x.getCod())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("Id inválido: " + cod);
	}
	
	
}
