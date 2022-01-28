package com.comunidade.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.comunidade.domain.Squad;
import com.comunidade.domain.Time;

public interface TimeRepository extends JpaRepository<Time, Integer> {
	
	@Transactional(readOnly=true)
	public Time findById(Long id);
	
}
