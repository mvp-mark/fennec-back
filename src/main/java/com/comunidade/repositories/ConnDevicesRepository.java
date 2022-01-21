package com.comunidade.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.comunidade.domain.ConnDevices;
import com.comunidade.domain.Usuario;

public interface ConnDevicesRepository  extends JpaRepository<ConnDevices, Integer> {
	
	@Transactional(readOnly=true)
	public ConnDevices findByDevices(String device);
	
	@Transactional(readOnly=true)
	public List<ConnDevices> findByUsuario(Usuario usuario);
	
	
}
