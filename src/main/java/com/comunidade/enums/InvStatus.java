package com.comunidade.enums;

public enum InvStatus {
	AGUARDANDO_APROV(0, "Aguardando Aprovação"),
	ENVIADO(1, "Enviado"),
	EXPIRADO(2, "Expirado"),
	EXCLUIDO(3, "Excluido"),
	UTILIZADO(4, "Utilizado");
	
	private int cod;
	private String descricao;
	
	private InvStatus(int cod, String descricao) {
		this.cod = cod;
		this.descricao = descricao;
	}
	
	public int getCod() {
		return cod;
	}
	
	public String getDescricao () {
		return descricao;
	}
	
	public static InvStatus toEnum(Integer cod) {
		
		if (cod == null) {
			return null;
		}
		
		for (InvStatus x : InvStatus.values()) {
			if (cod.equals(x.getCod())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("Id inválido: " + cod);
	}
	
}
