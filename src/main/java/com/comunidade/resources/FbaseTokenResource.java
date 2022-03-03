package com.comunidade.resources;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.comunidade.domain.FbaseToken;
import com.comunidade.domain.Usuario;
import com.comunidade.dto.FbaseTokenDTO;
import com.comunidade.dto.InvitesDTO;
import com.comunidade.security.JWTUtil;
import com.comunidade.services.FbaseTokenService;
import com.comunidade.services.UsersService;

@RestController
@RequestMapping(value = "/fbasetoken")
public class FbaseTokenResource {
	
	@Autowired
	private FbaseTokenService service;
	
	@Autowired
	private UsersService uservice;
	
	private JWTUtil jwtUtil;
	
	public FbaseTokenResource(JWTUtil jwtUtil) {
		super();
		this.jwtUtil = jwtUtil;
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity insert(@RequestBody @Valid FbaseTokenDTO fdto, HttpServletRequest request) {
		//Recebe o token do individuo, pesquisa no banco de dados
		//por algum token pelo usuario,
		//se existir ele verifica o token do banco
		//se for igual n faz nada, se for diferente atualiza.
		//se nao existir ele cria um token no banco.
		
		String header = request.getHeader("Authorization");
		String userToken = "";
		
		if (header != null && header.startsWith("Bearer ")) {
			userToken = header.substring(7);
		}
		
		String tell = jwtUtil.getUsername(userToken);
		Usuario user = uservice.findByTell(tell);
		
		List<FbaseToken> fbt = service.findByUser(user);
		
		if (fbt.isEmpty()) {
			FbaseToken fbti = service.insert(service.fromDTO(fdto));
		}else if (!fbt.get(0).getToken().equals(fdto.getToken())){
			List<FbaseToken> fbti = service.update(service.fromDTO(fdto));	
		}
		
		return ResponseEntity.status(HttpStatus.OK).body("Token criado com sucesso");
	}
}
