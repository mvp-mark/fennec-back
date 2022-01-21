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
import com.comunidade.dto.TimeDTO;
import com.comunidade.exceptions.DataIntegrityException;
import com.comunidade.exceptions.ObjectNotFoundException;
import com.comunidade.repositories.SquadRepository;
import com.comunidade.repositories.TimeRepository;

@Service
public class TimeService {

	@Autowired
	private TimeRepository repo;

	public Time find(Integer id) {
		Optional<Time> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Client.class.getName()));
	}
	
	public Time insert(Time obj) {
		obj.setId(null);
		return repo.save(obj);
	}
	
	public List<Time> saveAll(Time obj) {
		obj.setId(null);
		return repo.saveAll(Arrays.asList(obj));
	}
	
	public List<Time> update(Time obj) {
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
	
	public Time fromDTO(TimeDTO objDto) {
		Time time = new Time(objDto.getId(),objDto.getDescription() ,
				objDto.getName(), objDto.getAverageRating(),
				objDto.getIsRandom(),objDto.getCreationDate(), 
				objDto.getLeadId(),
				objDto.getStatus(), new HashSet<>(objDto.getUsers()),objDto.getVacancies());
		//time.setDescription(objDto.getDescription());
		//time.setName(objDto.getName());
		return time;
	}
	
	public List<Time> findAll() {
		return repo.findAll();
	}

}
