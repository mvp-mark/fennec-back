package com.comunidade.repositories;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.comunidade.domain.Message;



@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    @Query("SELECT * FROM mensagens order by id_mensagem desc")Page<Message> search(
        @Param("searchTerm") String searchTerm, 
        Pageable pageable);

}