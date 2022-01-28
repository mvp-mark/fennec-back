package com.comunidade.resources;

import java.security.SecureRandom;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.comunidade.domain.Invites;
import com.comunidade.domain.Usuario;
import com.comunidade.dto.InvitesDTO;
import com.comunidade.dto.UsuarioDTO;
import com.comunidade.enums.Nivel;
import com.comunidade.security.JWTUtil;
import com.comunidade.services.InviteService;
import com.comunidade.services.TimeService;
import com.comunidade.services.UsersService;

import lombok.extern.jackson.Jacksonized;

@RestController
@RequestMapping(value = "/invite")
public class InviteResources {
	@Autowired
    private JavaMailSender mailSender;
    
	@Autowired
	private InviteService service;
	
	@Autowired
	private UsersService uservice;
	
	private JWTUtil jwtUtil;
	
	public InviteResources(JWTUtil jwtUtil) {
		super();
		this.jwtUtil = jwtUtil;
	}
	
	/*
    @Jacksonized
    @RequestMapping(value="/mail/{to}/{token}", method=RequestMethod.GET)
	public ResponseEntity<String> run(@PathVariable String to,  @PathVariable String token) {
    	//gera código
    	//salva invite na memória
    	//apenas com a permissao do master, o invite vai ser enviado
    	
    	SecureRandom rand = new SecureRandom();
        var code = rand.nextInt(900000) + 100000;
    	
        /*
        //new Thread(new Runnable() {
        //public void run() {
        //    SimpleMailMessage email = new SimpleMailMessage();
        //    email.setTo(to);
        //    email.setSubject("Convite Fennec");
        //    email.setText("Olá, você recebeu um convite para participar da Fennec \n"+
        //    "\r\n" + "Clique no link à direita, ");
        //    // email.setText(token);
        //    //Do whatever
        //    mailSender.send(email);
        //}
	    //}).start();
	    //return ResponseEntity.ok().body(token);      
        
        return ResponseEntity.ok().body(token);
	}*/
	
	
	//usuario criar invite
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Invites> insert(@RequestBody @Valid InvitesDTO objDto, HttpServletRequest request) {
		Invites obj = service.fromDTO(objDto);
		
		//Pegar o nivel do usuario,
		String header = request.getHeader("Authorization");
		String userToken = "";
		
		if (header != null && header.startsWith("Bearer ")) {
			userToken = header.substring(7);
		}
		
		String tell = jwtUtil.getUsername(userToken);
		Usuario user = uservice.findByTell(tell);
		
		//Verifica o nivel selecionado no invite,
		//System.out.println(user.getNivel().equals(Nivel.MASTER));
		
		if(user.getNivel().equals(Nivel.MASTER)) {
			//admite qualquer nivel de convite
			
			if(obj.getNivel().compareTo(Nivel.MASTER)<=0) {
				//salva
				obj.setSeniorAproval(true);
				obj.setMasterAproval(false);
				
				obj = service.insert(obj);
				return ResponseEntity.ok().body(obj);
				
			}else {
				obj.setSeniorAproval(true);
				obj.setMasterAproval(true);
				
				obj = service.insert(obj);
				return ResponseEntity.ok().body(obj);
				//Enviar Invite
			}
			
		}else if(user.getNivel().equals(Nivel.SENIOR)) {
			//So admite de niveis senior pra cima
			if(obj.getNivel().compareTo(Nivel.SENIOR)>=0) {
				//salva
				obj.setSeniorAproval(true);
				obj.setMasterAproval(false);
				
				obj = service.insert(obj);
				return ResponseEntity.ok().body(obj);
			}
		}else if(user.getNivel().equals(Nivel.JUNIOR)||user.getNivel().equals(Nivel.APRENDIZ)) {
			//Se o usuario for junior ou aprendiz, so podem pedir por convite de junior ou aprendiz
			if(obj.getNivel().compareTo(Nivel.JUNIOR)>=0) {
				//Salva
				obj.setSeniorAproval(true);
				obj.setMasterAproval(false);
				
				obj = service.insert(obj);
				return ResponseEntity.ok().body(obj);
			}
		}
		return new ResponseEntity<>(null, HttpStatus.I_AM_A_TEAPOT);
	}
	
	
	
	//usuario aprova outros invites
	@RequestMapping(value="/aprovar/",method=RequestMethod.POST)
	public ResponseEntity<Invites> aprovar(@RequestBody @Valid InvitesDTO objDto, HttpServletRequest request) {
		Invites obj = service.fromDTO(objDto);
		//Pegar o nivel do usuario,
		String header = request.getHeader("Authorization");
		String userToken = "";
		if (header != null && header.startsWith("Bearer ")) {
			userToken = header.substring(7);
		}
		String tell = jwtUtil.getUsername(userToken);
		Usuario user = uservice.findByTell(tell);
		//Coleta usuario
		//verifica nivel do usuario
		//Verifica Permissoes Invite
		//Se usuario for senior ou master do usuario 
		//que realizou o invite, atribuir permissao 
		//corresponpondente como true
		
		//Verificar primeiro se usuario esta na hierarquia
		if(user.getNivel().equals(Nivel.MASTER)) {
			obj.setMasterAproval(true);
			if(obj.isMasterAproval()&&obj.isSeniorAproval()) {
				//envia para o usuario
			}
			List<Invites> invites = service.update(obj);
		}else if(user.getNivel().equals(Nivel.SENIOR)) {
			obj.setSeniorAproval(true);
			if(obj.isMasterAproval()&&obj.isSeniorAproval()) {
				//envia para o usuario
			}
			List<Invites> invites = service.update(obj);			
		}else {
			//usuario nao tem permissao
		}
		
		return ResponseEntity.ok().body(obj);
	}
	
	//invite aprovado foi utilizado
	/*
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Invites> utilizar(@RequestBody @Valid InvitesDTO objDto) {
		Invites obj = service.fromDTO(objDto);
		
		obj.setNivel(Nivel.toEnum(3));
		
		obj = service.insert(obj);
		return ResponseEntity.ok().body(obj);
	}
	*/

	//Mostrar invite.
	@RequestMapping(value="/mostrar/{id}/", method=RequestMethod.GET)
	public ResponseEntity<Invites> mostrar(@PathVariable Integer id) {
		Invites obj = service.find(id);
		
		return ResponseEntity.ok().body(obj);
	}
	
	//usuario solicita re-envio do invite;
	@RequestMapping(value="/reenvio/{id}/", method=RequestMethod.GET)
	public ResponseEntity<Invites> reenvio(@PathVariable Integer id) {
		Invites obj = service.find(id);
		obj.setExpirationDate(obj.calculateExpiryDate((60*24*7)));
		
		//Envia o invite;
		
		List<Invites> invite = service.update(obj);
		return ResponseEntity.ok().body(obj);
	}
	
	/*
	//usuario solicita re-aprovacao do invite
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Invites> reaprovacao(@RequestBody @Valid InvitesDTO objDto) {
		Invites obj = service.fromDTO(objDto);
		
		obj.setNivel(Nivel.toEnum(3));
		
		obj = service.insert(obj);
		return ResponseEntity.ok().body(obj);
	}
	*/
	
	/*
	//Ver solicitacoes de invite por usuario
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Invites> solusuario(@RequestBody @Valid InvitesDTO objDto) {
		Invites obj = service.fromDTO(objDto);
		
		obj.setNivel(Nivel.toEnum(3));
		
		obj = service.insert(obj);
		return ResponseEntity.ok().body(obj);
	}
	
	//Ver solicitacoes de invite por master
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Invites> solmaster(@RequestBody @Valid InvitesDTO objDto) {
		Invites obj = service.fromDTO(objDto);
		
		obj.setNivel(Nivel.toEnum(3));
		
		obj = service.insert(obj);
		return ResponseEntity.ok().body(obj);
	}
	
	//Ver solicitacoes de invite por senior
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Invites> solmaster(@RequestBody @Valid InvitesDTO objDto) {
		Invites obj = service.fromDTO(objDto);
		
		obj.setNivel(Nivel.toEnum(3));
		
		obj = service.insert(obj);
		return ResponseEntity.ok().body(obj);
	}
	
	//deletar invite
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Invites> selinvite(@RequestBody @Valid InvitesDTO objDto) {
		Invites obj = service.fromDTO(objDto);
		
		obj.setNivel(Nivel.toEnum(3));
		
		obj = service.insert(obj);
		return ResponseEntity.ok().body(obj);
	}
	*/
}