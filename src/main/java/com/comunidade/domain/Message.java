package com.comunidade.domain;

import java.io.Serializable;
import java.util.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;


@Entity
@Table(name = "Mensagens")
public class Message implements Serializable {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMensagem")
    private Integer idMensagem;
    
    @Column(columnDefinition = "TIME")
    @JsonFormat(pattern = "HH:mm:ss",timezone = "GMT-04:00")
    private LocalTime hora;
    
    @Column(columnDefinition = "DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date data;
    
    @Column(name = "texto", columnDefinition = "TEXT")
    private String texto;
    
    @Column(name = "tipo")
    private String tipo;
    
    @Column(name = "meio")
    private String meio;
    
    @Column(name = "time")
    private Time time;
    
    @Column(name = "squad")
    private Time squad;
    
    @Column(name = "status")
    private String status;

    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = {"rg",
    "birthDay",
    "tell",
    "cpf",
    "email",
    "validated",
    "isLead",
    "averageRating",
    "statusUser",
    "urlLatter",
    "urlLinkedin",
    "perfis",
    "invites",
    "squads",})
    private Usuario usuarioId;
    
    
    public Message(){}
    
    public Message(String string, String string2, String string3, Object object){

    }
    public Message(String texto, String tipo, String status, Date data, LocalTime hora, Usuario usuarioId) throws ParseException{
        Date date =  new Date();
        String str = new SimpleDateFormat("yyyy-MM-dd").format(date);
        String str2 = new SimpleDateFormat("HH:mm").format(date);
        java.sql.Time sqlTime = new Time(new Date().getTime());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss[.nnnnnnn]");
        LocalTime now = LocalTime.now();
        String timeString = now.format(formatter);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");      
        Date c = sdf.parse(str);	
        LocalTime h = LocalTime.parse(timeString);
        System.out.println("HORA: "+ h);
        this.texto=texto;
        this.tipo = tipo;
        this.status = status;
        this.data = c;
        this.hora = h;
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

    public LocalTime getHora() {
        return this.hora;
    }

    public void setHora(LocalTime hora) {
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
