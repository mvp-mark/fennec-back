package com.comunidade.services;

import java.text.ParseException;

import java.util.List;

import com.comunidade.domain.Message;
import com.comunidade.dto.MessageDTO;
import com.comunidade.exceptions.DataIntegrityException;
import com.comunidade.repositories.MessageRepository;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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
	@MessageMapping("/message")
	@SendTo("/topic/messages")
	public Message insert(Message obj) {
		obj.setIdMensagem(null);
		System.out.println("TeXTO"+ obj.getTexto());
		try {
		obj = repo.save(obj);
		}catch (Exception e) {
			// TODO: handle exception
			throw new DataIntegrityException("Erro de inserção de Menssagem "+e);
		}
		return obj;
	}

	public List<Message> findAll() {
		
		return  repo.findAll(Sort.by(Sort.Direction.DESC,"idMensagem"));
		
	}


	public Page<Message> listAllWithPagination(int offset, int pageSize){
	Page<Message> messages = repo.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(Sort.Direction.DESC,"idMensagem")));
	
		return messages;
	}
	
	public Page<Message> listAllWithPaginationByTime(int timeid,int offset, int pageSize){
		Page<Message> messages = repo.searchByTime(timeid,PageRequest.of(offset, pageSize).withSort(Sort.by(Sort.Order.desc("mn.data"),Sort.Order.desc("mn.hora"))));
		//Page<Message> messages2 = repo.findAll
		return messages;
	}
	
	public Page<Message> listAllWithPaginationBySquad(int squadid, int offset, int pageSize){
		Page<Message> messages = repo.searchBySquad(squadid,PageRequest.of(offset, pageSize).withSort(Sort.by(Sort.Order.desc("mn.data"),Sort.Order.desc("mn.hora"))));
		//Page<Message> messages2 = repo.findAll
		return messages;
	}
	
	public Page<Message> listByMasterWithPagination(String master,int offset, int pageSize){
		//Page<Message> messages = repo.searchByMaster(master,PageRequest.of(offset, pageSize).withSort(Sort.by(Sort.Direction.DESC,"idMensagem")));
		
		Page<Message> messages = repo.searchByMaster(master,PageRequest.of(offset, pageSize).withSort(Sort.by(Sort.Order.desc("mn.data"),Sort.Order.desc("mn.hora"))));
		
		return messages;
	}
	
	public Page<Message> listByMastertoMasterWithPagination(String master,int offset, int pageSize){
		//Page<Message> messages = repo.searchByMaster(master,PageRequest.of(offset, pageSize).withSort(Sort.by(Sort.Direction.DESC,"idMensagem")));
		
		Page<Message> messages = repo.searchByMastertoMaster(master,PageRequest.of(offset, pageSize).withSort(Sort.by(Sort.Order.desc("mn.data"),Sort.Order.desc("mn.hora"))));
		
		return messages;
	}
	
	public Page<Message> listGeral(int offset, int pageSize){
		//Page<Message> messages = repo.searchByMaster(master,PageRequest.of(offset, pageSize).withSort(Sort.by(Sort.Direction.DESC,"idMensagem")));
		
		Page<Message> messages = repo.searchGeral(PageRequest.of(offset, pageSize).withSort(Sort.by(Sort.Order.desc("data"),Sort.Order.desc("hora"))));
		
		return messages;
	}
	
	/*public Message fromDTO(MessageDTO objDto) throws ParseException {
		return new Message(
			 objDto.getTexto(), 
			 objDto.getTipo(), 
			 objDto.getStatus(),
			 objDto.getData(),
			 objDto.getHora(),
			 objDto.getUsuarioId()
	   );
	}*/
	
	public Message fromDTO(MessageDTO objDto) throws ParseException {
		return new Message(
			objDto.getData(),
			objDto.getTexto(),
			objDto.getTipo(),
			objDto.getTime(),
			objDto.getSquad(),
			objDto.getStatus(),
			objDto.getUsuarioId()
		);
	}
}


