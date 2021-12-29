package com.comunidade.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.comunidade.domain.Usuario;
import com.comunidade.dto.UserPagination;
import com.comunidade.dto.UsuarioDTO;
import com.comunidade.exceptions.DataIntegrityException;
import com.comunidade.exceptions.ObjectNotFoundException;
import com.comunidade.repositories.UserRepository;


@Service
public class UsersService {
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private BCryptPasswordEncoder pe;

	
	public Usuario find(Integer id) {
				
		Optional<Usuario> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Usuario.class.getName()));
	}
	
	@Transactional
	public Usuario insert(Usuario obj) {
		obj.setId(null);
		try {
		obj = repo.save(obj);
		}catch (Exception e) {
			// TODO: handle exception
			throw new DataIntegrityException("Erro de inserção de usuário");
		}
		return obj;
	}
	
	public Usuario update(Usuario obj) {
		Usuario newObj = find(obj.getId());		
		return repo.save(obj);
	}

	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		}
		catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir porque há pedidos relacionados");
		}
	}
	
	public List<Usuario> findAll() {
		return repo.findAll();
	}
	
	public Usuario findByEmail(String email) {

		Usuario obj = repo.findByEmail(email);
		if (obj == null) {
			throw new ObjectNotFoundException(
					"Objeto não encontrado! Id: , Tipo: " + Usuario.class.getName());
		}
		return obj;
	}	
	public Usuario findByTell(String tell) {

		Usuario obj = repo.findByTell(tell);
		if (obj == null) {
			throw new ObjectNotFoundException(
					"Objeto não encontrado! Id: , Tipo: " + Usuario.class.getName());
		}
		return obj;
	}	

	
	public UserPagination findByPerfil(int perfil, Pageable pageable) {
		
		Page<Usuario> obj = repo.findByPerfis(perfil,pageable);
		if (obj == null) {
			throw new ObjectNotFoundException(
					"Objeto não encontrado! Id: , Tipo: " + Usuario.class.getName());
		}
		
		return new UserPagination(obj.getContent(),obj.getTotalPages(),obj.getNumberOfElements());
	}	

	
	public Usuario fromDTO(UsuarioDTO objDto) {
		Usuario cli = new Usuario(null, objDto.getName(), objDto.getRg(),objDto.getBirthDay(),objDto.getTell(),objDto.getCpf(), objDto.getEmail(), 
				pe.encode(objDto.getPassword()), objDto.getTipo(),objDto.getValidated(),
				objDto.getIsLead(),objDto.getUrlLatter(),objDto.getUrlLinkedin(),objDto.getAverageRating());

		return cli;
	}


}
