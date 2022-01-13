package com.comunidade.security;

import java.util.Date;

import com.comunidade.domain.Usuario;
import com.comunidade.repositories.UserRepository;
import com.comunidade.services.UsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {
	

	@Autowired
	private UserRepository repo;

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private Long expiration;
	
	@Autowired
	private UsersService service;

	public String generateToken(String tell) {	

		// Usuario obj = repo.findByTell(tell);

		
		Usuario obj = repo.findByTell(tell);
		return Jwts.builder()
				.setSubject(obj.getTell())
				// .setPayload()
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(SignatureAlgorithm.HS512, secret.getBytes())
				.compact();
	}
	
	public boolean tokenValido(String token) {
		Claims claims = getClaims(token);
		if (claims != null) {
			String username = claims.getSubject();
			Date expirationDate = claims.getExpiration();
			Date now = new Date(System.currentTimeMillis());
			if (username != null && expirationDate != null && now.before(expirationDate)) {
				return true;
			}
		}
		return false;
	}

	public String getUsername(String token) {
		Claims claims = getClaims(token);
		if (claims != null) {
			return claims.getSubject();
		}
		return null;
	}


	
	public Usuario getUser(String token) {
		Claims claims = getClaims(token);
		Usuario obj = repo.findByTell(claims.getSubject());
		if (claims != null) {
			System.out.println("deu bom + -"+ obj.getName());
			return obj;
			// return claims.getSubject();
		}
		System.out.println("deu ruim");
		return null;
	}


	
	private Claims getClaims(String token) {
		try {
			return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
		}
		catch (Exception e) {
			return null;
		}
	}
}
