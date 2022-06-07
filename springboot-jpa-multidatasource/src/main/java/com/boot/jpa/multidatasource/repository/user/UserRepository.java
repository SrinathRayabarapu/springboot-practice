package com.boot.jpa.multidatasource.repository.user;

import com.boot.jpa.multidatasource.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}