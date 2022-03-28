package com.boot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boot.models.Note;

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
