package com.comunidade.resources;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.comunidade.domain.Usuario;
import com.comunidade.dto.UserPagination;
import com.comunidade.dto.UsuarioDTO;
import com.comunidade.enums.Status;
import com.comunidade.services.UsersService;


@RestController
@RequestMapping(value = "/users")
public class UserResource {
	
	@Autowired
	private UsersService service;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<Usuario> find(@PathVariable Integer id) {
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
	public ResponseEntity<Usuario> insert(@RequestBody @Valid UsuarioDTO objDto) {
		Usuario obj = service.fromDTO(objDto);
		obj = service.insert(obj);
		
		
		
		return ResponseEntity.ok().body(obj);
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
