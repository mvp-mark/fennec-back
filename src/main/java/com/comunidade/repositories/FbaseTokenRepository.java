package com.comunidade.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.comunidade.domain.FbaseToken;
import com.comunidade.domain.Invites;
import com.comunidade.domain.Usuario;
import com.comunidade.enums.Nivel;

public interface FbaseTokenRepository extends JpaRepository<FbaseToken, Integer> {
	
	/*
	@Transactional(readOnly=true)
	public List<Invites> findByNivel(Nivel nivel);

	//Pesquisa invites por senior.
	@Query("SELECT DISTINCT i "
			+ " FROM Usuario as u INNER JOIN Invites as i\r\n"
			+ " on u.id = i.userId WHERE\r\n"
			+ " u.hierarquia LIKE CONCAT(:master,'/','%')")
	List<Invites> searchByMaster(@Param("master") String master);
	*/
	
	//@Transactional(readOnly=true)
	//public FbaseToken
	
	public List<FbaseToken> findByUserId(Usuario userId);
	
	@Query("SELECT DISTINCT t "
			+ " FROM Usuario as u INNER JOIN FbaseToken as t INNER JOIN u.squads as s\r\n"
			+ " on u.id = t.user WHERE\r\n"
			+ " s.id = :squadid")
	List<FbaseToken> searchBySquad(String squadid);
	
	@Query("SELECT DISTINCT t "
			+ " FROM Usuario as u INNER JOIN FbaseToken as t INNER JOIN u.times as i\r\n"
			+ " on u.id = t.user  WHERE\r\n"
			+ " i.id = :timeid")
	List<FbaseToken> searchByTime(String timeid);
	
	
}