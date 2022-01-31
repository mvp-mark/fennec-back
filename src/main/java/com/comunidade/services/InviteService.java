package com.comunidade.services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.comunidade.domain.Client;
import com.comunidade.domain.Invites;
import com.comunidade.domain.Time;
import com.comunidade.dto.InvitesDTO;
import com.comunidade.dto.TimeDTO;
import com.comunidade.enums.Nivel;
import com.comunidade.exceptions.DataIntegrityException;
import com.comunidade.exceptions.ObjectNotFoundException;
import com.comunidade.repositories.InviteRepository;
import com.comunidade.repositories.TimeRepository;

@Service
public class InviteService {
	
	@Autowired
	private InviteRepository repo;

	public Invites find(Integer id) {
		Optional<Invites> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Client.class.getName()));
	}
	
	public Invites insert(Invites obj) {
		obj.setId(null);
		return repo.save(obj);
	}
	
	public List<Invites> saveAll(Invites obj) {
		obj.setId(null);
		return repo.saveAll(Arrays.asList(obj));
	}
	
	public List<Invites> update(Invites obj) {
		return repo.saveAll(Arrays.asList(obj));
	}
	
	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		}
		catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos");
		}
	}
	
	public Invites fromDTO(InvitesDTO objDto) {
		Invites invite = new Invites(
				objDto.getId(),objDto.getUserId(),objDto.getReceiverEmail(),
				Nivel.toEnum(objDto.getNivel()),objDto.getExpirationDate(),objDto.getAcceptDate(),
				objDto.getInviteCode(),objDto.getStatus(),objDto.isSeniorAproval(),
				objDto.isMasterAproval());
		//time.setDescription(objDto.getDescription());
		//time.setName(objDto.getName());
		return invite;
	}
	
	public List<Invites> findAll() {
		return repo.findAll();
	}
	
}