package com.comunidade.resources;

import java.net.URI;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.comunidade.domain.Client;
import com.comunidade.domain.Squad;
import com.comunidade.domain.Time;
import com.comunidade.domain.Usuario;
import com.comunidade.dto.SquadDTO;
import com.comunidade.dto.TimeDTO;
import com.comunidade.security.JWTUtil;
import com.comunidade.services.SquadService;
import com.comunidade.services.TimeService;
import com.comunidade.services.UsersService;
import com.google.common.collect.Sets;

@RestController
@RequestMapping(value="/squad")
public class SquadResource {
	
	@Autowired
	private SquadService service;
	
	@Autowired
	private UsersService uservice;
	
	@Autowired
	private TimeService tservice;
	
	private JWTUtil jwtUtil;
	
	public SquadResource(JWTUtil jwtUtil) {
		super();
		this.jwtUtil = jwtUtil;
	}

	//Insere um novo squad
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity insert(@RequestBody @Valid SquadDTO objDto,HttpServletRequest request) {
		try {
			String header = request.getHeader("Authorization");
			String userToken = "";
			if (header != null && header.startsWith("Bearer ")) {
				userToken = header.substring(7);
			}
			
			String tell = jwtUtil.getUsername(userToken);
			Usuario user = uservice.findByTell(tell);
	
			Squad obj = service.fromDTO(objDto);
			obj.setLeadId(user);
			
			List<Usuario> listaUsuario = Arrays.asList(user);
			Set<Usuario> targetSet = new HashSet<>(listaUsuario);
			obj.setUsers(targetSet);
			
			Time time = tservice.find(obj.getTime().getId());
			obj.setTime(time);
			//verifica se criador do squad eh o dono do time.
			if(time.getLeadId().getId().equals(user.getId())) {
				
				List<Squad> squads = service.saveAll(obj);
				
				//salva squad no usuario
				user.getSquads().addAll(Arrays.asList(obj));
				user = uservice.updateAllData(user);
				
				//salva squad no time
				time.getSquads().add(obj);
				List<Time> times = tservice.update(time);
				
				return ResponseEntity.ok().body(obj);
				
			}else{
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ops... Você não é dono desse squad");
			}
		}catch(Exception e) {
			System.out.println("exception e"+e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops... Ocorreu um erro durante a sua requisicao.");
		}
	}
	
	
	
	//Visualiza squads por usuario
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
			System.out.println("e "+e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops... Ocorreu um erro durante a sua requisicao.");
		}
	}
	
	//Mostra a Squad
	@RequestMapping(value="/mostrar", method=RequestMethod.POST)
	public ResponseEntity findById(@RequestBody @Valid SquadDTO objDto, HttpServletRequest request){
		//Encontra usuario por token
		try {
			/*String header = request.getHeader("Authorization");
			String userToken = "";
			if (header != null && header.startsWith("Bearer ")) {
				userToken = header.substring(7);
				
			}
			
			String tell = jwtUtil.getUsername(userToken);
			Usuario obj = uservice.findByTell(tell);*/
			Squad squad= service.find(objDto.getId());
			//List<Time> lista = new ArrayList<>(obj.getTimes());
			//Time tobj = service.findBy
			
			return ResponseEntity.ok().body(squad);
		}catch(Exception e) {
			System.out.println("e "+e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops... Ocorreu um erro durante a sua requisicao.");
		}
	}
	
	//Altera configuracoes do squad
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public ResponseEntity updateSquad(@RequestBody @Valid SquadDTO objDto, HttpServletRequest request) {
		//Altera as configuracoes do time;
		try {
			String header = request.getHeader("Authorization");
			String userToken = "";
			
			if (header != null && header.startsWith("Bearer ")) {
				userToken = header.substring(7);
			}
			
			String tell = jwtUtil.getUsername(userToken);
			Usuario obj = uservice.findByTell(tell);
			
			Squad squadn= service.fromDTO(objDto);
			Squad squad = service.find(objDto.getId());
			if (squadn.getName()!=null) squad.setName(squadn.getName());
			if (squadn.getDescription()!=null) squad.setDescription(squadn.getDescription());
			
			
			//List<Time> lista = new ArrayList<>(obj.getTimes());
			
			if(obj.getId().equals(squad.getLeadId().getId())) {
				Squad squads = service.updates(squad);
				return ResponseEntity.ok().body(squads);
			}
			else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ops... Você não é dono desse squad");
			}
			
		}catch(Exception e) {
			System.out.println("e "+e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops... Ocorreu um erro durante a sua requisicao.");
		}
	}
	
	//Adiciona usuarios ao squad
	@RequestMapping(value="/addusuario", method=RequestMethod.POST)
	public ResponseEntity addusuario(@RequestBody @Valid SquadDTO squadDto, HttpServletRequest request) {
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
			Squad squad = service.find(squadDto.getId());
			List<Squad> lista = new ArrayList<>(obj.getSquads());
			
			if(obj.getId().equals(squad.getLeadId().getId())) {
				for(int i=0;i<squadDto.getUsers().size();i++) {
					addU.add(squadDto.getUsers().get(i));
				}
				squad.getUsers().addAll(addU);
				List<Squad> squads= service.update(squad);
				return ResponseEntity.ok().body(squads.get(0).getUsers());
			}else {
				
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ops... Você não é dono desse squad");
			}
		
		}catch(Exception e) {
			System.out.println("e "+e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops... Ocorreu um erro durante a sua requisicao.");
		}
	}
	
	//Mostra Lista de usuarios do squad
	@RequestMapping(value="/listausuarios", method=RequestMethod.POST)
	public ResponseEntity usuariostime(@RequestBody @Valid SquadDTO squadDto,HttpServletRequest request) {
		//Encontra usuario por token
		try {
			Squad squad = service.find(squadDto.getId());
			
			List<Usuario> lista = new ArrayList<>(squad.getUsers());
			
			//Time tobj = service.findBy
			return ResponseEntity.ok().body(lista);
		}catch(Exception e) {
			System.out.println("e "+e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops... Ocorreu um erro durante a sua requisicao.");
		}
		
		
	}
		
	//Remove usuarios do squad
	@RequestMapping(value="/removeusuario", method=RequestMethod.POST)
	public ResponseEntity removeUsuario(@RequestBody @Valid SquadDTO squadDto, HttpServletRequest request) {
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
			Squad squad= service.find(squadDto.getId());
			Set<Usuario> usuarios = Sets.newHashSet();
			
			
			//Verifica se leadid do time fornecido eh igual 
			if(squad.getLeadId().getId().equals(obj.getId())) {
				
				//verifica a lista de usuarios
				for(int i =0;i<squadDto.getUsers().size();i++) {
					//verifica se o usuario na posicao atual eh diferente do usuario que
					//fez a requisicao
					if(!squadDto.getUsers().get(i).getId().equals(squad.getLeadId().getId())) {					
						usuarios.add(uservice.find(squadDto.getUsers().get(i).getId()));
					}else {
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops... Ocorreu um erro durante a sua requisicao.");
					}
				}
				
				squad.getUsers().removeAll(usuarios);
				
				System.out.println();
				List<Squad> squads = service.update(squad);
				return ResponseEntity.ok().body(squads.get(0).getUsers());
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ops... Você não é dono desse squad");
				//Usuario nao autorizado
			}
		}catch(Exception e) {
			System.out.println("e "+e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops... Ocorreu um erro durante a sua requisicao.");
		}
		//Usuario obj = service.findByEmail(email);
	}
	
	
	
	//deleta squad
	@RequestMapping(value="/deletasquad", method=RequestMethod.POST)
	public ResponseEntity deletatime(@RequestBody @Valid SquadDTO squadDto, HttpServletRequest request) {
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
			Squad squad = service.find(squadDto.getId());
			Time time = tservice.find(squad.getTime().getId());
			
			if(obj.getId().equals(squad.getLeadId().getId())) {
				
				time.getSquads().removeAll(new HashSet<>(Arrays.asList(squad)));
				List<Time> times = tservice.update(time);
				service.delete(squadDto.getId());
				
				return new ResponseEntity<>(null,HttpStatus.OK);
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ops... Você não é dono desse squad");
			}
		}catch(Exception e) {
			System.out.println("e "+e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops... Ocorreu um erro durante a sua requisicao.");
		}
	}
	
	
}
