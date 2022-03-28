package com.boot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boot.models.Users;

//@Component //required to autowire
public interface UserJPARepisitory extends JpaRepository<Users, Long> {

	Users findByName(String name);
	
}