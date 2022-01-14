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

import com.comunidade.domain.Company;
import com.comunidade.dto.UserPagination;
import com.comunidade.dto.CompanyDTO;
import com.comunidade.exceptions.DataIntegrityException;
import com.comunidade.exceptions.ObjectNotFoundException;
import com.comunidade.repositories.CompanyRepository;


@Service
public class CompanyService {
	
	@Autowired
	private CompanyRepository repo;
	
	@Autowired
	private BCryptPasswordEncoder pe;

	
	public Company find(Integer id) {
				
		Optional<Company> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Company.class.getName()));
	}
	
	@Transactional
	public Company insert(Company obj) {
		obj.setId(null);
		try {
		obj = repo.save(obj);
		}catch (Exception e) {
			// TODO: handle exception
			throw new DataIntegrityException("Erro de inserção de Empresa");
		}
		return obj;
	}
	
	public Company update(Company obj) {
		Company newObj = find(obj.getId());		
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
	
	// public List<Company> findAll() {
	// 	return repo.findAll();
	// }
	
	// public Company findByEmail(String email) {

	// 	Company obj = repo.findByEmail(email);
	// 	if (obj == null) {
	// 		throw new ObjectNotFoundException(
	// 				"Objeto não encontrado! Id: , Tipo: " + Company.class.getName());
	// 	}
	// 	return obj;
	// }	

	public Company fromDTO(CompanyDTO objDto) {
		Company cli = new Company(null,
         objDto.getName(),
         objDto.getCnpj(),
         objDto.getEmail(),
         pe.encode(objDto.getPassword()),
         objDto.getPhone(),
         objDto.getRazaoSocial(),
         objDto.getFantasia());

		return cli;
	}


}
