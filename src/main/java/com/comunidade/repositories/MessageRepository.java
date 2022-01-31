package com.comunidade.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
//import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.comunidade.domain.Message;
import com.comunidade.domain.Squad;
import com.comunidade.domain.Time;



@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    // @Query("SELECT * FROM mensagens order by id_mensagem desc")Page<Message> search(
    //     @Param("searchTerm") String searchTerm, 
    //     Pageable pageable);
	//@Query("SELECT * FROM mensagens order by id_mensagem desc")
	//Page<Message> searchByMaster(@Param("master") String master, Pageable pageable);
	
	@Query("SELECT DISTINCT mn "
			+ " FROM Usuario as u INNER JOIN Message as mn\r\n"
			+ " on u.id = mn.usuarioId.id WHERE\r\n"
			+ " u.hierarquia LIKE CONCAT(:master,'/','%')"
			+ " AND mn.meio = 0")
			//+ " 1=1")
	Page<Message> searchByMaster(@Param("master") String master, Pageable pageable);
	
	@Query("SELECT DISTINCT mn "
			+ " FROM Usuario as u INNER JOIN Message as mn\r\n"
			+ " on u.id = mn.usuarioId.id WHERE\r\n"
			+ " u.hierarquia LIKE CONCAT(:master,'/','%')"
			+ " AND u.nivel = 0 AND mn.meio = 0")
			//+ " 1=1")
	Page<Message> searchByMastertoMaster(@Param("master") String master, Pageable pageable);
	
	@Query("SELECT DISTINCT mn "
			+ " FROM Time as t INNER JOIN Message as mn\r\n"
			+ " on t.id = mn.time.id WHERE\r\n"
			+ " t.id = :timeid"
			+ " AND mn.meio = 1")
			//+ " 1=1")
	Page<Message> searchByTime(@Param("timeid") int timeid, Pageable pageable);
	
	@Query("SELECT DISTINCT mn "
			+ " FROM Squad as s INNER JOIN Message as mn\r\n"
			+ " on s.id = mn.squad.id WHERE\r\n"
			+ " s.id = :squadid"
			+ " AND mn.meio = 2")
	Page<Message> searchBySquad(@Param("squadid") int squadid, Pageable pageable);
	
	@Query(""
			+ " FROM Message mn"
			+ " WHERE\r\n"
			+ " mn.meio = 0")
	Page<Message> searchGeral(Pageable pageable);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM Message m WHERE m.time = :timeid")
	Integer deleteByTime(@Param("timeid") Time timeid);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM Message m WHERE m.squad = :squadid")
	Integer deleteBySquad(@Param("squadid") Squad squadid);
}