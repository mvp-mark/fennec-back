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
import org.springframework.web.bind.annotation.RequestHeader;
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
import com.comunidade.util.EmailServiceImpl;

import lombok.extern.jackson.Jacksonized;

@RestController
@RequestMapping(value = "/invite")
public class InviteResources {
	
	@Autowired
    private JavaMailSender emailSender;
	
	@Autowired
	private InviteService service;
	
	@Autowired
	private UsersService uservice;
	
	private JWTUtil jwtUtil;
	
	
	
	public InviteResources(JWTUtil jwtUtil) {
		super();
		this.jwtUtil = jwtUtil;
	}
	
	//usuario criar invite
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity insert(@RequestBody @Valid InvitesDTO objDto, HttpServletRequest request) {
		try {
			Invites obj = service.fromDTO(objDto);
			//Pegar o nivel do usuario,
			String header = request.getHeader("Authorization");
			String userToken = "";
			
			if (header != null && header.startsWith("Bearer ")) {
				userToken = header.substring(7);
			}
			String tell = jwtUtil.getUsername(userToken);
			Usuario user = uservice.findByTell(tell);
			// Procurar por email
			
			if(!service.findByEmail(obj.getReceiverEmail()).isEmpty()) {
				System.out.println("linha 73 findbyemail is not empty");
				
				List<Invites> invites = service.findByEmail(obj.getReceiverEmail());
				invites.get(0).setExpirationDate(obj.calculateExpiryDate((60*24*7)));
				List<Invites> sinvites = service.update(invites.get(0));
				
				if((sinvites.get(0).isMasterAproval())) {	
					String mensagem = "Olá, "
						+ user.getName() + " convidou você para fazer parte da Fennec. \n\n"
						+ "A Fennec é uma comunidade de desenvolvedores e programadores, "
						+ "para fazer parte crie sua conta no nosso aplicativo utilizando "
						+ "o email referente à sua conta.\n"
						+ "Este convite irá expirar em uma semana.";
					String para = sinvites.get(0).getReceiverEmail();
					String assunto = "Convite Fennec";
					EmailServiceImpl esi = new EmailServiceImpl();
					esi.sendSimpleMessage(para,assunto,mensagem,emailSender);
					return ResponseEntity.status(HttpStatus.OK).body("Convite ja existe, data de expiração atualizada e convite re-enviado.");
					
				}
				//return ResponseEntity.ok().body(obj);
				return ResponseEntity.status(HttpStatus.OK).body("Convite ja existe, data de expiração atualizada.");
				
			}else {
				System.out.println("linha 97 findbyemail is");
				
				obj.setExpirationDate(obj.calculateExpiryDate((60*24*7)));
				obj.setUserId(user);
				
				//se o convidante for master
				//o nivel pode ser definido no momento, se não o nivel deve ser definido como null.
				if(user.getNivel().equals(Nivel.MASTER)) {
					//Verificar nivel, se master, nao enviar (esperar aprov), se nao, enviar
					obj.setNivel(objDto.getNivel()!=0?Nivel.toEnum(objDto.getNivel()):Nivel.JUNIOR);
					if(obj.getNivel().compareTo(Nivel.MASTER)==0) {
						obj.setMasterAproval(false);
						obj = service.insert(obj);
						
						return ResponseEntity.status(HttpStatus.OK).body("Convite criado com sucesso");
					}else {
						obj.setMasterAproval(true);
						obj = service.insert(obj);
						String mensagem = "Olá, "
								+ user.getName() + " convidou você para fazer parte da Fennec. \n\n"
								+ "A Fennec é uma comunidade de desenvolvedores e programadores, "
								+ "para fazer parte crie sua conta no nosso aplicativo utilizando "
								+ "o email referente à sua conta.\n"
								+ "Este convite irá expirar em uma semana.";
						String para = objDto.getReceiverEmail();
						String assunto = "Convite Fennec";
						EmailServiceImpl esi = new EmailServiceImpl();
						esi.sendSimpleMessage(para,assunto,mensagem,emailSender);
						
						return ResponseEntity.status(HttpStatus.OK).body("Convite criado e enviado com sucesso");
					}
					
				}else {
					obj.setNivel(null);
					obj.setMasterAproval(false);
					obj = service.insert(obj);
					
					return ResponseEntity.status(HttpStatus.OK).body("Convite criado com sucesso");
				}
				
			} //else (service.find(obj.getId))
			
		}catch(Exception e) {
			System.out.println("exception e"+e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops... Ocorreu um erro durante a sua requisicao.");
		}
		//return new ResponseEntity<>(null, HttpStatus.I_AM_A_TEAPOT);
		
	}
	
	//usuario aprova outros invites do nivel master
	@RequestMapping(value="/aprovar/{id}",method=RequestMethod.GET)
	public ResponseEntity aprovarMaster(@PathVariable Integer id, HttpServletRequest request) {
		try {
			Invites obj = service.find(id);
			String header = request.getHeader("Authorization");
			String userToken = "";
			
			if (header != null && header.startsWith("Bearer ")) {
				userToken = header.substring(7);
			}
			
			String tell = jwtUtil.getUsername(userToken);
			Usuario user = uservice.findByTell(tell);	
			
			obj.setExpirationDate(obj.calculateExpiryDate((60*24*7)));
			//Verifica os niveis do usuario se ele eh senior ou master
			
			//Essa chamada é para aprovar invites que já são do nivel master
			//Primeiro verificar se invite eh realmente do nivel master
			//depois verificar se usuario eh do nivel master
			//se sim adicionar usuario ao set
			//se nao, sem permissao
			//após isso verificar quantidade de usuarios no set, se maior ou igual a 3
			//masteraproval = true
			//se masteraproval =  true, enviar mensagem.
			
			if((obj.getNivel().compareTo(Nivel.MASTER)==0)&&(user.getNivel().compareTo(Nivel.MASTER)==0)) {
				if(!(obj.getUserId().getId().equals(user.getId()))) {
					obj.getMasters().add(user);
				}else {
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ops, criador do invite master não pode fazer parte da aprovação.");
				}
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ops, você não tem permissão para isso.");
			}
			
			if(obj.getMasters().size()>=3){
				obj.setMasterAproval(true);
			}
			
			List<Invites> invites = service.update(obj);
			
			if(obj.isMasterAproval()) {
				String mensagem = "Olá, "
						+ user.getName() + " convidou você para fazer parte da Fennec. \n\n"
						+ "A Fennec é uma comunidade de desenvolvedores e programadores, "
						+ "para fazer parte crie sua conta no nosso aplicativo utilizando "
						+ "o email referente à sua conta.\n"
						+ "Este convite irá expirar em uma semana.";
				String para = obj.getReceiverEmail();
				String assunto = "Convite Fennec";
				EmailServiceImpl esi = new EmailServiceImpl();
				esi.sendSimpleMessage(para,assunto,mensagem,emailSender);
				
				return ResponseEntity.status(HttpStatus.OK).body("Usuario aprovado e enviado com sucesso.");
			}
			
			return ResponseEntity.status(HttpStatus.OK).body("Usuario aprovado com sucesso.");
		}catch(Exception e) {
			System.out.println("exception e"+e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops... Ocorreu um erro durante a sua requisicao.");
		}
	}
	
	//usuario aprova outros invites que sejam abaixo do master
	@RequestMapping(value="/aprovarm/{id}/{nivel}",method=RequestMethod.GET)
	public ResponseEntity aprovar(@PathVariable Integer id,@PathVariable Integer nivel, HttpServletRequest request) {
		try {
			Invites obj = service.find(id);
			String header = request.getHeader("Authorization");
			String userToken = "";
			if (header != null && header.startsWith("Bearer ")) {
				userToken = header.substring(7);
			}
			
			String tell = jwtUtil.getUsername(userToken);
			Usuario user = uservice.findByTell(tell);	
			
			obj.setExpirationDate(obj.calculateExpiryDate((60*24*7)));
			
			//Primeiro verificar se o invite é de nivel inferior a master
			//e user é igual a master.
			//adicionar master ao set e aprovar invite.
			//definir nivel do invite.
			//se master aproval enviar invite.
			
			if(obj.getNivel()!=null) {
				if((obj.getNivel().compareTo(Nivel.MASTER)>0)&&(user.getNivel().compareTo(Nivel.MASTER)==0)) {
					obj.getMasters().add(user);
					obj.setMasterAproval(true);
					obj.setNivel(Nivel.toEnum(nivel));
				}else {
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ops, você não tem permissão para isso.");
				}				
			}else {
				if(user.getNivel().compareTo(Nivel.MASTER)==0) {
					obj.getMasters().add(user);
					obj.setMasterAproval(true);
					System.out.println("linha 246 inviteresources");
					obj.setNivel(Nivel.toEnum(nivel));
					System.out.println("linha 248 inviteresourcesb "+obj.getNivel());
				}else {
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ops, você não tem permissão para isso.");
				}
			}
				
			
			if(obj.getMasters().size()>=1) {
				obj.setMasterAproval(true);
			}
			
			List<Invites> invites = service.update(obj);

			if(obj.isMasterAproval()) {
				String mensagem = "Olá, "
						+ user.getName() + " convidou você para fazer parte da Fennec. \n\n"
						+ "A Fennec é uma comunidade de desenvolvedores e programadores, "
						+ "para fazer parte crie sua conta no nosso aplicativo utilizando "
						+ "o email referente à sua conta.\n"
						+ "Este convite irá expirar em uma semana.";
				String para = obj.getReceiverEmail();
				String assunto = "Convite Fennec";
				EmailServiceImpl esi = new EmailServiceImpl();
				esi.sendSimpleMessage(para,assunto,mensagem,emailSender);
				
				return ResponseEntity.status(HttpStatus.OK).body("Usuario aprovado e enviado com sucesso.");
			}
			
			return ResponseEntity.status(HttpStatus.OK).body("Usuario aprovado com sucesso.");
		}catch(Exception e) {
			System.out.println("exception e"+e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops... Ocorreu um erro durante a sua requisicao.");
		}
	}
	
	//Mostrar invite.
	@RequestMapping(value="/mostrar/{id}", method=RequestMethod.GET)
	public ResponseEntity mostrar(@PathVariable Integer id) {
		try {
			Invites obj = service.find(id);
			return ResponseEntity.ok().body(obj);
			
		}catch(Exception e) {
			System.out.println("exception e"+e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops... Ocorreu um erro durante a sua requisicao.");
		}
	}
	
	//Mostrar invite por usuario que criou o invite.
	@RequestMapping(value="/mostrar", method=RequestMethod.GET)
	public ResponseEntity mostrarPorUsuario(@RequestHeader(name = "Authorization") String token) {
		try {
			Usuario user =jwtUtil.getUser(token.substring(7));
			System.out.println(user.getId().toString());
			List<Invites> invites = service.findByUserId(user);
			
			return ResponseEntity.ok().body(invites);			
		}catch(Exception e) {
			System.out.println("exception e"+e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops... Ocorreu um erro durante a sua requisicao.");
		}
	}
	
	//usuario solicita re-envio do invite;
	@RequestMapping(value="/reenvio/{id}", method=RequestMethod.GET)
	public ResponseEntity reenvio(@PathVariable Integer id) {		
		try {
			Invites obj = service.find(id);
			obj.setExpirationDate(obj.calculateExpiryDate((60*24*7)));
			
			//Envia o invite;
			List<Invites> invite = service.update(obj);
			
			if(!obj.isMasterAproval())
				obj.setMasterAproval(((obj.getNivel().compareTo(Nivel.MASTER)==0)&&obj.getMasters().size()>=3)
						||((obj.getNivel().compareTo(Nivel.MASTER)>0)&&obj.getMasters().size()>=1));
				
			
			if(obj.isMasterAproval()) {		
				String mensagem = "Olá, "
						+ obj.getUserId().getName() + " convidou você para fazer parte da Fennec. \n\n"
						+ "A Fennec é uma comunidade de desenvolvedores e programadores, "
						+ "para fazer parte crie sua conta no nosso aplicativo utilizando "
						+ "o email referente à sua conta.\n"
						+ "Este convite irá expirar em uma semana.";
				
				String para = obj.getReceiverEmail();
				String assunto = "Convite Fennec";
				EmailServiceImpl esi = new EmailServiceImpl();
				
				esi.sendSimpleMessage(para,assunto,mensagem,emailSender);
				return ResponseEntity.status(HttpStatus.OK).body("Convite re-enviado com sucesso");
				
			}else {
				return ResponseEntity.status(HttpStatus.OK).body("Convite ainda requer aprovação de master ou senior");
				
			}
			
			//return ResponseEntity.ok().body(obj);
			
		}catch(Exception e) {
			System.out.println("exception e"+e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops... Ocorreu um erro durante a sua requisicao.");
		}
	}
	
	//Ver solicitacoes de invite por master
	@RequestMapping(value="/solnivelmaster",method=RequestMethod.GET)
	public ResponseEntity solNivelMaster(@RequestHeader(name = "Authorization") String token) {		
		try {
			
			Usuario user =jwtUtil.getUser(token.substring(7));
			
			if(user.getNivel().compareTo(Nivel.MASTER)==0) {
				List<Invites> invites = service.listByNivel(Nivel.MASTER);
				return ResponseEntity.ok().body(invites);
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ops, você não tem permissão para isso.");
			}
		}catch(Exception e) {
			System.out.println("exception e"+e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops... Ocorreu um erro durante a sua requisicao.");
		}
	}
	
	
	//Ver solicitacoes de invite por master
	@RequestMapping(value="/solmaster",method=RequestMethod.GET)
	public ResponseEntity solMaster(@RequestHeader(name = "Authorization") String token) {		
		try {
			Usuario user =jwtUtil.getUser(token.substring(7));
			System.out.println(user.getId().toString());
			List<Invites> invites = service.listByMaster(user.getId().toString());
			
			return ResponseEntity.ok().body(invites);
		}catch(Exception e) {
			System.out.println("exception e"+e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops... Ocorreu um erro durante a sua requisicao.");
		}
	}
	
	/*
	//Ver solicitacoes de invite por senior
	@RequestMapping(value="/solsenior",method=RequestMethod.GET)
	public ResponseEntity solSenior(@RequestHeader(name = "Authorization") String token) {
		try {
			Usuario user =jwtUtil.getUser(token.substring(7));			
			List<Invites> invites = service.listBySenior(user.getId().toString());
			return ResponseEntity.ok().body(invites);
			
		}catch(Exception e) {
			System.out.println("exception e"+e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops... Ocorreu um erro durante a sua requisicao.");
		}
	}
	*/
	
}