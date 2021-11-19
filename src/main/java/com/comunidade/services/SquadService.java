package com.comunidade.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.comunidade.domain.Client;
import com.comunidade.domain.Squad;
import com.comunidade.exceptions.DataIntegrityException;
import com.comunidade.exceptions.ObjectNotFoundException;
import com.comunidade.repositories.SquadRepository;


@Service
public class SquadService {

	@Autowired
	private SquadRepository repo;

	public Squad find(Integer id) {
		Optional<Squad> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Client.class.getName()));
	}
	
	public Squad insert(Squad obj) {
		obj.setId(null);
		return repo.save(obj);
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
	
	public List<Squad> findAll() {
		return repo.findAll();
	}

}
