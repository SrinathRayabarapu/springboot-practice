package com.repositories;

import com.models.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
