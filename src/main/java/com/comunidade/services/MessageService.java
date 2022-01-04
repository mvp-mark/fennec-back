package com.comunidade.services;

import java.text.ParseException;

import java.util.List;

import com.comunidade.domain.Message;
import com.comunidade.dto.MessageDTO;
import com.comunidade.exceptions.DataIntegrityException;
import com.comunidade.repositories.MessageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageService {

    @Autowired
	private MessageRepository repo;


    @Transactional
	public Message insert(Message obj) {
		obj.setIdMensagem(null);
		System.out.println("TeXTO"+ obj.getTexto());
		try {
		obj = repo.save(obj);
		}catch (Exception e) {
			// TODO: handle exception
			throw new DataIntegrityException("Erro de inserção de Menssagem");
		}
		return obj;
	}

	public List<Message> findAll() {
		return repo.findAll();
	}
	
	
    public Message fromDTO(MessageDTO objDto) throws ParseException {
		return new Message(
		objDto.getTexto(), 
        objDto.getTipo(), 
        objDto.getStatus(),
		objDto.getData(),
		objDto.getHora(),
		objDto.getUsuarioId()
        );
        // objDto.getHora(),
		// objDto.getData(),
        // objDto.getUsuarioId()

		
	}
	// System.out.print(id);
}
