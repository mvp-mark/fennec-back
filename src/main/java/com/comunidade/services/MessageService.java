package com.comunidade.services;

import java.text.ParseException;

import java.util.List;

import com.comunidade.domain.Message;
import com.comunidade.dto.MessageDTO;
import com.comunidade.exceptions.DataIntegrityException;
import com.comunidade.repositories.MessageRepository;
import org.springframework.data.domain.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

	// public List<Message> findAll() {
	// 	return repo.findAll();
	// }
	

    public Page<Message> search(
            String searchTerm,
            int page,
            int size) {
        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.Direction.ASC,
                "name");

        return repo.search(
                searchTerm.toLowerCase(),
                pageRequest);
    }

	public Page<Message> findAll() {
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.Direction.ASC,
                "id_mensagem");
        return new PageImpl<>(

				repo.findAll(), 
                pageRequest, size);
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
