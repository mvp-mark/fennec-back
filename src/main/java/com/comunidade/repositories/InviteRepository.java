package com.comunidade.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.comunidade.domain.Invites;
import com.comunidade.domain.Message;
import com.comunidade.domain.Usuario;
import com.comunidade.enums.Nivel;

public interface InviteRepository extends JpaRepository<Invites, Integer> {
	
//	@Transactional(readOnly=true)
//	public Page<Usuario> findByPerfisAndCompaniesIn(int perfil, List<Company> companies, Pageable pageable);
	
	//@Transactional(readOnly=true)
	//public Page<Usuario> findByPerfis(int perfil, Pageable pageable);
	
	@Transactional(readOnly=true)
	public List<Invites> findByReceiverEmail(String receiverEmail);
	
	@Transactional(readOnly=true)
	public List<Invites> findByUserId(Usuario userId);
	
	@Transactional(readOnly=true)
	public List<Invites> findByNivel(Nivel nivel);

	//Pesquisa invites por senior.
	@Query("SELECT DISTINCT i "
			+ " FROM Usuario as u INNER JOIN Invites as i\r\n"
			+ " on u.id = i.userId WHERE\r\n"
			+ " u.hierarquia LIKE CONCAT(:master,'/','%')")
	List<Invites> searchByMaster(@Param("master") String master);
	
	//Pesquisa invites por senior.
	@Query("SELECT DISTINCT i "
			+ " FROM Usuario as u INNER JOIN Invites as i\r\n"
			+ " on u.id = i.userId WHERE\r\n"
			+ " u.hierarquia LIKE CONCAT('%','/',:senior,'/','%')")
	List<Invites> searchBySenior(@Param("senior") String senior);
	
	//@Transactional(readOnly=true)
	//public Usuario findByTell(String tell);

}
