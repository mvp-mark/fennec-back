package com.comunidade.services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.comunidade.domain.Client;
import com.comunidade.domain.FbaseToken;
import com.comunidade.domain.Time;
import com.comunidade.domain.Usuario;
import com.comunidade.dto.FbaseTokenDTO;
import com.comunidade.dto.TimeDTO;
import com.comunidade.exceptions.DataIntegrityException;
import com.comunidade.exceptions.ObjectNotFoundException;
import com.comunidade.repositories.FbaseTokenRepository;
import com.comunidade.repositories.MessageRepository;
import com.comunidade.repositories.TimeRepository;

@Service
public class FbaseTokenService {
	
	@Autowired
	private FbaseTokenRepository repo;
	
	@Autowired
	private MessageRepository mrepo;

	public FbaseToken find(Integer id) {
		Optional<FbaseToken> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Client.class.getName()));
	}
	
	public FbaseToken insert(FbaseToken obj) {
		obj.setId(null);
		return repo.save(obj);
	}
	
	public List<FbaseToken> findByUser(Usuario usuario) {
		List<FbaseToken> obj = repo.findByUserId(usuario);
		return obj;
	}
	
	public List<FbaseToken> saveAll(FbaseToken obj) {
		obj.setId(null);
		return repo.saveAll(Arrays.asList(obj));
	}
	
	public List<FbaseToken> update(FbaseToken obj) {
		return repo.saveAll(Arrays.asList(obj));
	}
	
	public void delete(Integer id) {
		FbaseToken fbt = find(id);
		try {
			repo.deleteById(id);
		}
		catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos");
		}
	}
	
	public FbaseToken fromDTO(FbaseTokenDTO fbt) {
		
		FbaseToken fbtoken = new FbaseToken(
				fbt.getId(),
				fbt.getTimestamp(),
				fbt.getUsuario(),
				fbt.getToken(),
				fbt.isMute()
				);
		
		//time.setDescription(objDto.getDescription());
		//time.setName(objDto.getName());
		return fbtoken;
	}
	
	public List<FbaseToken> findAll() {
		return repo.findAll();
	}
	
}
