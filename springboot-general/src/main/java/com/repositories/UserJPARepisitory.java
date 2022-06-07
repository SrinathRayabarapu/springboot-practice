package com.repositories;

import com.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

//@Component //required to autowire
public interface UserJPARepisitory extends JpaRepository<Users, Long> {

	Users findByName(String name);
	
}