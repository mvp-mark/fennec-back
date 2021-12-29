package com.comunidade.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.comunidade.domain.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;


public class MessageDTO implements Serializable  {
    
    private Integer idMensagem;
    @DateTimeFormat
    private Date hora;
    @DateTimeFormat
    private Date data;
    @NotBlank
    private String texto;
    private String tipo;
    private String status;
    private Usuario usuarioId;
    
    public MessageDTO() {
    }

    public Integer getIdMensagem() {
        return this.idMensagem;
    }

    public void setIdMensagem(Integer idMensagem) {
        this.idMensagem = idMensagem;
    }

    public Date getHora() {
        return this.hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public Date getData() {
        return this.data;
    }

    public void setData(Date data) {

        this.data = data;
    }

    public String getTexto() {
        return this.texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Usuario getUsuarioId() {
        return this.usuarioId ;
    }

    public void setUsuarioId(Usuario usuarioId) {
        this.usuarioId = usuarioId;
    }

    public MessageDTO(Integer idMensagem, Date hora, Date data, String texto, String tipo, String status, Usuario usuarioId) {
        super();
        this.idMensagem = idMensagem;
        this.hora = hora;
        this.data = data;
        this.texto = texto;
        this.tipo = tipo;
        this.status = status;
        this.usuarioId = usuarioId;
    }
    

    // public Object setData(java.util.Date cData) {
    //     return null;
    // }


}
