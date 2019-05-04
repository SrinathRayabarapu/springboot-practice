package com.boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boot.model.Users;

//@Component //required to autowire
public interface UserJPARepisitory extends JpaRepository<Users, Long> {

	Users findByName(String name);
	
}