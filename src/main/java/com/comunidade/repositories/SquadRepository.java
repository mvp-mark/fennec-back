package com.comunidade.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.comunidade.domain.ConnDevices;
import com.comunidade.domain.Squad;

public interface SquadRepository extends JpaRepository<Squad, Integer> {
	
	@Transactional(readOnly=true)
	public Squad findById(Long id);
	

}

