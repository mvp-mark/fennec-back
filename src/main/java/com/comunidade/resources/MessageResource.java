package com.comunidade.resources;

import com.comunidade.services.MessageService;

import java.util.List;

import java.text.ParseException;

import javax.validation.Valid;

import com.comunidade.domain.Message;
import com.comunidade.domain.Usuario;
import com.comunidade.dto.MessageDTO;
import com.comunidade.security.JWTUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/feed")
public class MessageResource {

	@Autowired
	private MessageService service;

	@Autowired
	private JWTUtil jwt;

	// @RequestMapping(value="/send",method=RequestMethod.POST)
	@PostMapping("/send")
	public ResponseEntity<Message> insert(@RequestBody @Valid MessageDTO objDto,
			@RequestHeader(name = "Authorization") String token) throws ParseException {
				System.out.println("teste, texto: " + token);
				Usuario user =jwt.getUser(token.substring(7));
				objDto.setUsuarioId(user);
				Message obj = service.fromDTO(objDto);
		

		obj = service.insert(obj);
		return ResponseEntity.ok().body(obj);
	}

	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<Message>> findAll() {
		List<Message> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
}
