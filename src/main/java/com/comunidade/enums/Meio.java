package com.comunidade.enums;

public enum Meio {

	FEEDGERAL(1, "FEEDGERAL"),
	TIME(2, "TIME"),
	SQUAD(3, "SQUAD");
	
	private int cod;
	private String descricao;
	
	private Meio(int cod, String descricao) {
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
	
	public static Meio toEnum(Integer cod) {
		
		if (cod == null) {
			return null;
		}
		for (Meio x : Meio.values()) {
			if (cod.equals(x.getCod())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("Id inválido: " + cod);
	}

}