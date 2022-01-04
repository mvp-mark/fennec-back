package com.comunidade.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.comunidade.domain.Message;



public interface MessageRepository extends JpaRepository<Message, Integer> {



}