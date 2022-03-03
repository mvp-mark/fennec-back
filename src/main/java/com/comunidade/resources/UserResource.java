package com.comunidade.resources;

import java.util.Calendar;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.comunidade.domain.Invites;
import com.comunidade.domain.Usuario;
import com.comunidade.dto.UserPagination;
import com.comunidade.dto.UsuarioDTO;
import com.comunidade.enums.InvStatus;
import com.comunidade.enums.Nivel;
import com.comunidade.enums.Status;
import com.comunidade.services.InviteService;
import com.comunidade.services.UsersService;


@RestController
@RequestMapping(value = "/users")
public class UserResource {
	
	@Autowired
	private UsersService service;
	
	@Autowired
	private InviteService iservice;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity find(@PathVariable Integer id) {
		Usuario obj = null;
		
		obj = service.find(id);	
		return ResponseEntity.ok().body(obj);
	}	
	
	@RequestMapping(value="/findByPerfil/{id}/{page}", method=RequestMethod.GET)
	public ResponseEntity<UserPagination> findByPerfil(@PathVariable Integer id,@PathVariable Integer page) {
		Pageable paging = PageRequest.of(page, 10);		
		UserPagination obj = service.findByPerfil(id,paging);          
		
		return ResponseEntity.ok().body(obj);
	}
	
	
	@RequestMapping(value="/email/{email}", method=RequestMethod.GET)
	public ResponseEntity<Usuario> find(@PathVariable String email) {
		Usuario obj = service.findByEmail(email);
		return ResponseEntity.ok().body(obj);
	}
	
	@RequestMapping(value="/tell/{tell}", method=RequestMethod.GET)
	public ResponseEntity<Usuario> findTell(@PathVariable String tell) {
		Usuario obj = service.findByTell(tell);
		return ResponseEntity.ok().body(obj);
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity insert(@RequestBody @Valid UsuarioDTO objDto) {
		try {
			Usuario obj = service.fromDTO(objDto);
			
			obj.setNivel(Nivel.toEnum(2));
			obj.setHierarquia("2/");
			
			obj = service.insert(obj);
			return ResponseEntity.ok().body(obj);
		}catch(Exception e) {
			System.out.println("exception e"+e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops... Ocorreu um erro durante a sua requisicao.");
		}
	}
	
	@RequestMapping(value="/insert",method=RequestMethod.POST)
	public ResponseEntity insertInvite(@RequestBody @Valid UsuarioDTO objDto) {
		try {
			Usuario obj = service.fromDTO(objDto);
			//Verificar se existe um invite com o email desse usuario
			//se sim, e se o invite aprovado com a data de expiração correta (e com o status correto)
			//colocar o nivel do invite
			//verificar hierarquia do invite
			
			List<Invites> invites =  iservice.findByEmail(objDto.getEmail());
			Calendar cal = Calendar.getInstance();
			
			
			if(!invites.isEmpty()) {
				//if(invites.get(0).getExpirationDate()&&)
				//se os invites ainda estiverem dentro do prazo de validade
				if (((invites.get(0).getExpirationDate().getTime() - cal.getTime().getTime()) <= 0)
						&&(invites.get(0).getInvstatus().compareTo(InvStatus.ENVIADO)==0)) {
					
					obj.setNivel(invites.get(0).getNivel());
					//se o nivel do usuario convidador for master
					if(invites.get(0).getUserId().getNivel().compareTo(Nivel.MASTER)==0){

						//Verifica se o nivel do invite eh master
						//se sim o usuario recebe ele mesmo na hierarquia
						if(invites.get(0).getNivel().compareTo(Nivel.MASTER)==0) {
							Usuario usuario = service.insert(obj);
							obj.setHierarquia(usuario.getId()+"/");
							obj =  service.update(obj);
						//se nao, usuario recebe o master na hierarquia
						}else {
							obj.setHierarquia(invites.get(0).getUserId().getHierarquia());
							obj = service.insert(obj);
							
						}
					//se o nivel do usuario convidador nao for igual a master	
					}else {
						//se o nivel do convidador do invite for maior que o nivel do convidado
						//e o convidador for senior, colocar hierarquia do convidador, mas o seu id
						//na hierarquia do convidado.
						if(invites.get(0).getUserId().getNivel().compareTo(invites.get(0).getNivel())>0
							&&invites.get(0).getUserId().getNivel().compareTo(Nivel.SENIOR)==0) {
							
							obj.setHierarquia(invites.get(0).getUserId().getHierarquia()+
									invites.get(0).getUserId().getId()+"/");
							
						//se o nivel do convidado nao for maior ou ele nao for senior
						//o convidado recebe apenas o master na sua hierarquia.
						}else {
							obj.setHierarquia(invites.get(0).getUserId().getHierarquia());
						}
						obj = service.insert(obj);
						
					}
				}
			}
			//obj.setNivel(Nivel.toEnum(2));
			//obj.setHierarquia("134/");
			
			//obj = service.insert(obj);
			return ResponseEntity.ok().body(obj);
		}catch(Exception e) {
			System.out.println("exception e"+e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ops... Ocorreu um erro durante a sua requisicao.");
		}
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Void> update(@RequestBody Usuario objDto, @PathVariable Integer id) {
		Usuario obj = service.find(id);
		obj.setId(id);
		
		if(objDto.getName() != null)
			obj.setName(objDto.getName());

		if(objDto.getEmail() != null)
			obj.setEmail(objDto.getEmail());
		
		if(objDto.getValidated() != null)
			obj.setValidated(objDto.getValidated());	
		
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Usuario> delete(@PathVariable Integer id) {
		Usuario usuario = service.find(id);		
		usuario.addStatus(Status.INATIVO);
		usuario = service.update(usuario);
		return ResponseEntity.ok().body(usuario);
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<Usuario>> findAll() {
		List<Usuario> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}

}
