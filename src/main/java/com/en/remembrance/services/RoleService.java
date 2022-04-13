package com.en.remembrance.services;

import com.en.remembrance.domain.Role;
import com.en.remembrance.exceptions.RecordNotFoundException;
import com.en.remembrance.exceptions.RoleNotFoundException;
import com.en.remembrance.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RoleService {

	private final RoleRepository roleRepository;

	@Autowired
	public RoleService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	public Role findByNameOrElseThrow(String name) {
		return roleRepository.findByName(name).orElseThrow(RoleNotFoundException::new);
	}
}
