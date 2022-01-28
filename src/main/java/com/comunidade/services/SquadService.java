package com.comunidade.services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.comunidade.domain.Client;
import com.comunidade.domain.Squad;
import com.comunidade.domain.Time;
import com.comunidade.dto.SquadDTO;
import com.comunidade.dto.TimeDTO;
import com.comunidade.exceptions.DataIntegrityException;
import com.comunidade.exceptions.ObjectNotFoundException;
import com.comunidade.repositories.MessageRepository;
import com.comunidade.repositories.SquadRepository;


@Service
public class SquadService {

	@Autowired
	private SquadRepository repo;
	
	@Autowired
	private MessageRepository mrepo;

	public Squad find(Integer id) {
		Optional<Squad> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Client.class.getName()));
	}
	
	public Squad insert(Squad obj) {
		obj.setId(null);
		return repo.save(obj);
	}
	
	public List<Squad> saveAll(Squad obj) {
		obj.setId(null);
		return repo.saveAll(Arrays.asList(obj));
	}
	
	public Squad fromDTO(SquadDTO objDto) {
		Squad squad = new Squad(objDto.getId(), objDto.getName(), objDto.getDescription(),
				objDto.getAverageRating(), objDto.getIsRandom(),
				objDto.getCreationDate(), objDto.getLeadId(),
				objDto.getStatus(), new HashSet<>(objDto.getUsers()),
				objDto.getTime(), objDto.getVacancies()
			);
		//time.setDescription(objDto.getDescription());
		//time.setName(objDto.getName());
		return squad;
	}
	
	
	public void delete(Integer id) {
		Squad squad = 	find(id);
		System.out.println("id "+id);
		try {
			System.out.println("linha 65");
			mrepo.deleteBySquad(squad);
			System.out.println("linha 67");
			repo.deleteById(id);
			System.out.println("linha 69");
		}
		catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos");
		}
	}
	
	public List<Squad> findAll() {
		return repo.findAll();
	}

	public List<Squad> update(Squad obj) {
		return repo.saveAll(Arrays.asList(obj));
	}
	
	public Squad updates(Squad obj) {
		return repo.save(obj);
	}
}
