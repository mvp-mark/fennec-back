package com.comunidade.resources;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.comunidade.domain.Company;
import com.comunidade.dto.CompanyDTO;
import com.comunidade.services.CompanyService;


@RestController
@RequestMapping(value = "/company")
public class CompanyResource {
	
	@Autowired
	private CompanyService service;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<Company> find(@PathVariable Integer id) {
		Company obj = null;
		
		obj = service.find(id);	
		return ResponseEntity.ok().body(obj);
	}	

	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Company> insert(@RequestBody @Valid CompanyDTO objDto) {
		Company obj = service.fromDTO(objDto);
		obj = service.insert(obj);
		return ResponseEntity.ok().body(obj);
	}
	
	
}
