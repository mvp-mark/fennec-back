package com.comunidade.domain;

import java.io.Serializable;
import java.util.Date;
// import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Table(name = "Mensagens")
public class Message implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMensagem")
    private Integer idMensagem;

    @JsonFormat(pattern = "HH:mm:ss")
    private Date hora;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date data;

    @Column(name = "texto", columnDefinition = "TEXT")
    private String texto;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "status")
    private String status;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuarioId;


    public Message(String string, String string2, String string3, Object object){

    }
    public Message(String texto, String tipo, String status, Date data, Date hora, Usuario usuarioId) throws ParseException{
        Date date =  new Date();
        String str = new SimpleDateFormat("yyyy-MM-dd").format(date);
                    
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date c = sdf.parse(str);	
        this.texto=texto;
        this.tipo = tipo;
        this.status = status;
        this.data = c;
        this.hora = c;
        this.usuarioId = usuarioId;
    }

    // public Message(Object object, String texto, Time hora, Date data, String tipo, String status,
    //         Usuario usuarioId) {

    // }

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
        return this.usuarioId;
    }

    public void setUsuarioId(Usuario usuarioId) {
        this.usuarioId = usuarioId;
    }




    // @OneToMany(targetEntity = Usuario.class, cascade = CascadeType.ALL)
    // @JoinColumn(name="idUsuario", referencedColumnName = "idMensagem")
    // private String idCanal;

}
