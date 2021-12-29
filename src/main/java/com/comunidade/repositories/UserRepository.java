package com.comunidade.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.comunidade.domain.Usuario;



public interface UserRepository extends JpaRepository<Usuario, Integer> {
	
//	@Transactional(readOnly=true)
//	public Page<Usuario> findByPerfisAndCompaniesIn(int perfil, List<Company> companies, Pageable pageable);
	
	@Transactional(readOnly=true)
	public Page<Usuario> findByPerfis(int perfil, Pageable pageable);
	
	@Transactional(readOnly=true)
	public Usuario findByEmail(String email);

	@Transactional(readOnly=true)
	public Usuario findByTell(String tell);


	


}

