package com.comunidade.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.comunidade.domain.Usuario;
import com.comunidade.enums.Perfil;
import com.comunidade.repositories.UserRepository;


public class UserSS implements UserDetails {
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private UserRepository repo;


	private Integer id;
	private String tell;
	private String senha;


	
	private Collection<? extends GrantedAuthority> authorities;
	
	public UserSS() {
	}
	    // public JWTAuthenticationFilter(UserRepository userRepository) {
    //     this.userRepository = userRepository;
    // }
	public UserSS(Integer id, String tell, String senha, Set<Perfil> perfis,UserRepository userRepository) {
		super();
		this.id = id;
		this.tell = tell;
		this.senha = senha;
		this.repo = userRepository;
		this.authorities = perfis.stream().map(x -> new SimpleGrantedAuthority(x.getDescricao())).collect(Collectors.toList());
	}

	public Integer getId() {
		return id;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return senha;
	}

	@Override
	public String getUsername() {
		return tell;
	}
	
	public Usuario getUser(String tell) {
		Usuario obj = repo.findByTell(tell);
		System.out.println("teste nome"+obj.getName());
		return obj;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	public boolean hasRole(Perfil perfil) {
		return getAuthorities().contains(new SimpleGrantedAuthority(perfil.getDescricao()));
	}
}
