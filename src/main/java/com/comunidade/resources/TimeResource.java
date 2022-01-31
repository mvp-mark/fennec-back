package com.comunidade.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.comunidade.domain.Squad;
import com.comunidade.domain.Time;
import com.comunidade.domain.Usuario;
import com.comunidade.dto.TimeDTO;
import com.comunidade.dto.UsuarioDTO;
import com.comunidade.security.JWTUtil;
import com.comunidade.services.TimeService;
import com.comunidade.services.UsersService;
import com.google.common.collect.Sets;

import springfox.documentation.service.ResponseMessage;

@RestController
@RequestMapping(value = "/time")
public class TimeResource {
	
	@Autowired
	private TimeService service;
	
	@Autowired
	private UsersService uservice;
	
	private JWTUtil jwtUtil;
	
	public TimeResource(JWTUtil jwtUtil) {
		super();
		this.jwtUtil = jwtUtil;
	}

	//Insere um novo time
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity insert(@RequestBody @Valid TimeDTO objDto,HttpServletRequest request) {
		try {
			String header = request.getHeader("Authorization");
			String userToken = "";
			if (header != null && header.startsWith("Bearer ")) {
				userToken = header.substring(7);
			}
			String tell = jwtUtil.getUsername(userToken);
			Usuario user = uservice.findByTell(tell);
	
			Time obj = service.fromDTO(objDto);
			obj.setLeadId(user);
			
			
			
			List<Usuario> listaUsuario = Arrays.asList(user);
			Set<Usuario> targetSet = new HashSet<>(listaUsuario);
			obj.setUsers(targetSet);
			List<Time> times = service.saveAll(obj);
			
			/*List<Time> listaTimes = new ArrayList<>();
			listaTimes.add(obj);*/
			user.getTimes().addAll(Arrays.asList(obj));
			user = uservice.updateAllData(user);
			
			return ResponseEntity.ok().body(obj);

		}catch(Exception e) {
			System.out.println("exception e"+e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops... Ocorreu um erro durante a sua requisicao.");
		}
	}
	
	//Visualiza times por usuario
	@RequestMapping(value="/lista", method=RequestMethod.GET)
	public ResponseEntity findByUser(HttpServletRequest request) {
		//Encontra usuario por token
		try {
			String header = request.getHeader("Authorization");
			String userToken = "";
			if (header != null && header.startsWith("Bearer ")) {
				userToken = header.substring(7);
				
			}
			
			String tell = jwtUtil.getUsername(userToken);
			Usuario obj = uservice.findByTell(tell);
			
			List<Time> lista = new ArrayList<>(obj.getTimes());
			//Time tobj = service.findBy
			
			return ResponseEntity.ok().body(lista);
		}catch(Exception e) {
			System.out.println("exception e"+e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops... Ocorreu um erro durante a sua requisicao.");
		}
	}
	
	//Mostra o time
	@RequestMapping(value="/mostrar", method=RequestMethod.POST)
	public ResponseEntity findById(@RequestBody @Valid TimeDTO objDto, HttpServletRequest request) {
		//Encontra usuario por token
		try {
			/*String header = request.getHeader("Authorization");
			String userToken = "";
			if (header != null && header.startsWith("Bearer ")) {
				userToken = header.substring(7);
				
			}
			
			String tell = jwtUtil.getUsername(userToken);
			Usuario obj = uservice.findByTell(tell);*/
			Time time = service.find(objDto.getId());
			//List<Time> lista = new ArrayList<>(obj.getTimes());
			//Time tobj = service.findBy
			
			return ResponseEntity.ok().body(time);
		}catch(Exception e) {
			System.out.println("exception e"+e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops... Ocorreu um erro durante a sua requisicao.");
		}
	}
	
	//Altera configuracoes do time
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public ResponseEntity updateTime(@RequestBody @Valid TimeDTO objDto, HttpServletRequest request) {
		//Altera as configuracoes do time;
		try {
			String header = request.getHeader("Authorization");
			String userToken = "";
			
			if (header != null && header.startsWith("Bearer ")) {
				userToken = header.substring(7);
			}
			
			String tell = jwtUtil.getUsername(userToken);
			Usuario obj = uservice.findByTell(tell);
			
			Time time = service.find(objDto.getId());
			if (objDto.getName()!=null) time.setName(objDto.getName());
			if (objDto.getDescription()!=null) time.setDescription(objDto.getDescription());
			
			List<Time> lista = new ArrayList<>(obj.getTimes());
			if(obj.getId().equals(time.getLeadId().getId())) {
				List<Time> times = service.update(time);
				return ResponseEntity.ok().body(times);
			}
			else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ops... Você não é o dono desse time");
			}
		}catch(Exception e) {
			System.out.println("exception e"+e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops... Ocorreu um erro durante a sua requisicao.");
		}
	}
	
	//Adiciona usuarios ao time
	@RequestMapping(value="/addusuario", method=RequestMethod.POST)
	public ResponseEntity addusuario(@RequestBody @Valid TimeDTO timeDto, HttpServletRequest request) {
		//Encontra usuario atraves do token;
		try {
			String header = request.getHeader("Authorization");
			String userToken = "";
			if (header != null && header.startsWith("Bearer ")) {
				userToken = header.substring(7);
			}
			String tell = jwtUtil.getUsername(userToken);
			Usuario obj = uservice.findByTell(tell);
			
			
			//Coleta todos os usuarios a serem adicionados que vieram na requisicao
			List<Usuario> addU = new ArrayList<>();
			Time time = service.find(timeDto.getId());
			List<Time> lista = new ArrayList<>(obj.getTimes());
			
			if(obj.getId().equals(time.getLeadId().getId())) {
				for(int i=0;i<timeDto.getUsers().size();i++) {
					addU.add(timeDto.getUsers().get(i));
				}
				time.getUsers().addAll(addU);
				List<Time> times = service.update(time);
				return ResponseEntity.ok().body(times.get(0).getUsers());
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ops... Você não é o dono desse time");
			}
		
		}catch(Exception e) {
			System.out.println("exception e"+e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops... Ocorreu um erro durante a sua requisicao.");
		}
}
	
	//Mostra Lista de usuarios do time
	@RequestMapping(value="/listausuarios", method=RequestMethod.POST)
	public ResponseEntity usuariostime(@RequestBody @Valid TimeDTO timeDto,HttpServletRequest request) {
		//Encontra usuario por token
		try {
			Time time = service.find(timeDto.getId());
			
			List<Usuario> lista = new ArrayList<>(time.getUsers());
			
			//Time tobj = service.findBy
			return ResponseEntity.ok().body(lista);
		}catch(Exception e) {
			System.out.println("exception e"+e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops... Ocorreu um erro durante a sua requisicao.");
		}
		
	}
	
	//Mostra Lista de squads do time
	@RequestMapping(value="/listasquads", method=RequestMethod.POST)
	public ResponseEntity squadstime(@RequestBody @Valid TimeDTO timeDto,HttpServletRequest request) {
		//Encontra usuario por token
		try {
			Time time = service.find(timeDto.getId());
			List<Squad> lista = new ArrayList<>(time.getSquads());
			//Time tobj = service.findBy
			return ResponseEntity.ok().body(lista);
		}catch(Exception e) {
			System.out.println("exception e"+e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops... Ocorreu um erro durante a sua requisicao.");
		}
	}
		
	//Remove usuarios do time
	@RequestMapping(value="/removeusuario", method=RequestMethod.POST)
	public ResponseEntity find(@RequestBody @Valid TimeDTO timeDto, HttpServletRequest request) {
		//Recupera dados do usuario que fez a requisicao
		try {
			String header = request.getHeader("Authorization");
			String userToken = "";
			if (header != null && header.startsWith("Bearer ")) {
				userToken = header.substring(7);
			}
			String tell = jwtUtil.getUsername(userToken);
			//Usuario encontrado a partir do token.
			Usuario obj = uservice.findByTell(tell);
			
			//Recupera time a partir do time fornecido;
			Time time = service.find(timeDto.getId());
			Set<Usuario> usuarios = Sets.newHashSet();
			
			
			//Verifica se leadid do time fornecido eh igual 
			if(time.getLeadId().getId().equals(obj.getId())) {
				
				//verifica a lista de usuarios
				for(int i =0;i<timeDto.getUsers().size();i++) {
					//verifica se o usuario na posicao atual eh diferente do usuario que
					//fez a requisicao
					if(!timeDto.getId().equals(obj.getId())) {					
						usuarios.add(uservice.find(timeDto.getId()));
					}else {
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops... Ocorreu um erro durante a sua requisicao.");
					}
				}
				
				time.getUsers().removeAll(usuarios);
				List<Time> times = service.update(time);
				return ResponseEntity.ok().body(times.get(0).getUsers());
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ops... Você não é o dono desse time");
				//Usuario nao autorizado
			}
		}catch(Exception e) {
			System.out.println("exception e"+e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops... Ocorreu um erro durante a sua requisicao.");
		}
		//Usuario obj = service.findByEmail(email);
	}
	
	//deleta time
	@RequestMapping(value="/deletatime", method=RequestMethod.POST)
	public ResponseEntity deletatime(@RequestBody @Valid TimeDTO timeDto, HttpServletRequest request) {
		//Recupera dados do usuario que fez a requisicao
		try {
			String header = request.getHeader("Authorization");
			String userToken = "";
			if (header != null && header.startsWith("Bearer ")) {
				userToken = header.substring(7);
			}
			String tell = jwtUtil.getUsername(userToken);
			//Usuario encontrado a partir do token.
			Usuario obj = uservice.findByTell(tell);
			Time time = service.find(timeDto.getId());
					
			if(obj.getId().equals(time.getLeadId().getId())) {
				service.delete(timeDto.getId());
				return new ResponseEntity<>(null,HttpStatus.OK);
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ops... Você não é o dono desse time");
			}
		}catch(Exception e) {
			System.out.println("exception e"+e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops... Ocorreu um erro durante a sua requisicao.");
		}
	}
	
}
