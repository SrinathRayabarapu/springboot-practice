package com.springboot.proj.easynote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.proj.easynote.model.Note;

/**
 * Repository class to access data from database.
 * 
 * JpaRepository default implementation is SimpleJpaRepository.
 * 
 * @author Srinath.Rayabarapu
 *
 */
@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

}
