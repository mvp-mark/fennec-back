package com.comunidade.resources;

import com.comunidade.services.MessageService;
import com.comunidade.services.SquadService;
import com.comunidade.services.TimeService;

import java.util.Collections;
import java.util.List;

import java.text.ParseException;

import javax.validation.Valid;

import com.comunidade.domain.Message;
import com.comunidade.domain.Squad;
import com.comunidade.domain.Time;
import com.comunidade.domain.Usuario;
import com.comunidade.dto.APIResponse;
import com.comunidade.dto.MessageDTO;
import com.comunidade.enums.Meio;
import com.comunidade.security.JWTUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/feed")
public class MessageResource {
	private static final int APIResponse = 0;

	@Autowired
	private MessageService service;

	@Autowired
	private TimeService tservice;

	@Autowired
	private SquadService sservice;
	@Autowired
	private JWTUtil jwt;

	@Autowired
	SimpMessagingTemplate template;

	// @RequestMapping(value="/send",method=RequestMethod.POST)
	@PostMapping("/send")
	@SendTo("/topic/message")
	public ResponseEntity insert(@RequestBody @Valid MessageDTO objDto,
			@RequestHeader(name = "Authorization") String token) throws ParseException {
		try {
			System.out.println("teste, texto: " + token);
			Usuario user = jwt.getUser(token.substring(7));
			objDto.setUsuarioId(user);
			Message obj = service.fromDTO(objDto);
			template.convertAndSend("/topic/message", obj);

			obj.setMeio(Meio.FEEDGERAL);
			// Atribuir a mensagem sempre para o time geral

			obj = service.insert(obj);
			return ResponseEntity.ok().body(obj);
		} catch (Exception e) {
			System.out.println("exception e" + e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Ops... Ocorreu um erro durante a sua requisicao.");
		}

	}

	@PostMapping("/send/{meio}")
	@SendTo("/topic/message/{meio}")
	public ResponseEntity insertprocedure(@RequestBody @Valid MessageDTO objDto,
			@RequestHeader(name = "Authorization") String token,
			@PathVariable int meio) throws ParseException {

		try {
			System.out.println("teste, texto: " + token);
			Usuario user = jwt.getUser(token.substring(7));
			objDto.setUsuarioId(user);
			Message obj = service.fromDTO(objDto);
			System.out.println("MEIO =========<<>>" + meio);
			template.convertAndSend("/topic/message/" + meio, obj);
			// precisa verificar as permissões do usuario ao
			// enviar a mensagem

			// verificar se usuario pertence ao time ou nao

			switch (meio) {
				case (0): // feedgeral enum(1)
					//
					obj.setMeio(Meio.FEEDGERAL);
					obj = service.insert(obj);
					break;
				case (1): // time enum(2)
					//
					obj.setMeio(Meio.TIME);
					Time time = tservice.find(obj.getTime().getId());
					obj.setTime(time);
					if (time.getUsers().contains(user)) {
						System.out.println("user ok");
						obj = service.insert(obj);
					} else {
						return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
								.body("Ops... Você não faz parte desse time");
					}
					break;

				case (2): // squad enum(3)
					//
					obj.setMeio(Meio.SQUAD);
					Squad squad = sservice.find(obj.getSquad().getId());
					obj.setSquad(squad);
					if (squad.getUsers().contains(user)) {
						System.out.println("user ok");
						obj = service.insert(obj);
					} else {
						return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
								.body("Ops... Você não faz parte desse squad");
					}
					break;
				default:
					//
					break;
			}

			return ResponseEntity.ok().body(obj);

		} catch (Exception e) {
			System.out.println("exception e" + e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Ops... Ocorreu um erro durante a sua requisicao.");
		}

	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity findAll() {

		try {
			List<Message> list = service.findAll();

			return ResponseEntity.ok().body(list);

		} catch (Exception e) {
			System.out.println("exception e" + e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Ops... Ocorreu um erro durante a sua requisicao.");
		}

	}

	@GetMapping("/pagination/{offset}/{pageSize}")
	private ResponseEntity getListAllWithPagination(
			@PathVariable int offset, @PathVariable int pageSize) {
		try {
			// Page<Message> messagesWithPagination = service.listAllWithPagination(offset,
			// pageSize);

			Page<Message> messagesWithPagination = service.listGeral(offset, pageSize);

			return ResponseEntity.ok().body(messagesWithPagination);

		} catch (Exception e) {
			System.out.println("exception e" + e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Ops... Ocorreu um erro durante a sua requisicao.");
		}

	}

	@GetMapping("/pagbymaster/{offset}/{pageSize}")
	private ResponseEntity getListMasterWithPagination(
			@PathVariable int offset, @PathVariable int pageSize,
			@RequestHeader(name = "Authorization") String token) {
		try {
			Usuario user = jwt.getUser(token.substring(7));

			String master = user.getHierarquia().split("/")[0];
			Page<Message> messagesWithPagination = service.listByMasterWithPagination(master, offset, pageSize);

			return ResponseEntity.ok().body(messagesWithPagination);

		} catch (Exception e) {
			System.out.println("exception e" + e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Ops... Ocorreu um erro durante a sua requisicao.");
		}

	}

	@GetMapping("/pagbymastertom/{offset}/{pageSize}")
	private ResponseEntity getListMastertoMasterWithPagination(
			@PathVariable int offset, @PathVariable int pageSize,
			@RequestHeader(name = "Authorization") String token) {
		try {
			Usuario user = jwt.getUser(token.substring(7));

			String master = user.getId().toString();
			Page<Message> messagesWithPagination = service.listByMastertoMasterWithPagination(master, offset, pageSize);

			return ResponseEntity.ok().body(messagesWithPagination);

		} catch (Exception e) {
			System.out.println("exception e" + e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Ops... Ocorreu um erro durante a sua requisicao.");
		}

	}

	@GetMapping("/pagbytime/{offset}/{pageSize}/{timeid}")
	private ResponseEntity getListByTime(
			@PathVariable int offset, @PathVariable int pageSize, @PathVariable int timeid,
			@RequestHeader(name = "Authorization") String token) {

		try {
			Usuario user = jwt.getUser(token.substring(7));

			Time time = tservice.find(timeid);
			if (time.getUsers().contains(user)) {
				Page<Message> messagesWithPagination = service.listAllWithPaginationByTime(timeid, offset, pageSize);
				return ResponseEntity.ok().body(messagesWithPagination);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ops... Você não faz parte desse time");
			}

		} catch (Exception e) {
			System.out.println("exception e" + e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Ops... Ocorreu um erro durante a sua requisicao.");
		}

	}

	@GetMapping("/pagbysquad/{offset}/{pageSize}/{squadid}")
	private ResponseEntity getListBySquad(
			@PathVariable int offset, @PathVariable int pageSize, @PathVariable int squadid,
			@RequestHeader(name = "Authorization") String token) {

		try {

			Usuario user = jwt.getUser(token.substring(7));

			Squad squad = sservice.find(squadid);

			if (squad.getUsers().contains(user)) {
				Page<Message> messagesWithPagination = service.listAllWithPaginationBySquad(squadid, offset, pageSize);
				return ResponseEntity.ok().body(messagesWithPagination);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ops... Você não faz parte desse squad");
			}

		} catch (Exception e) {
			System.out.println("exception e" + e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Ops... Ocorreu um erro durante a sua requisicao.");
		}

	}

	/*
	 * @GetMapping("/pagination/{offset}/{pageSize}")
	 * private ResponseEntity<Page<Message>> getListTimeWithPagination(@PathVariable
	 * int offset, @PathVariable int pageSize) {
	 * //Mostra a paginacao com base no master do usuario
	 * Page<Message> messagesWithPagination = service.listAllWithPagination(offset,
	 * pageSize);
	 * 
	 * return ResponseEntity.ok().body(messagesWithPagination);
	 * }
	 * 
	 * @GetMapping("/pagination/{offset}/{pageSize}")
	 * private ResponseEntity<Page<Message>>
	 * getListSquadWithPagination(@PathVariable int offset, @PathVariable int
	 * pageSize) {
	 * //Mostra a paginacao com base no master do usuario
	 * Page<Message> messagesWithPagination = service.listAllWithPagination(offset,
	 * pageSize);
	 * 
	 * return ResponseEntity.ok().body(messagesWithPagination);
	 * }
	 */

}
