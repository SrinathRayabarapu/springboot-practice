package com.controllers;

import com.models.Note;
import com.repositories.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * note controller class for all rest endpoints
 * 
 * @author Srinath.Rayabarapu
 */
@RestController
@RequestMapping("/api")
public class NoteController {

	@Autowired
	NoteRepository noteRepository;

	/**
	 * get all notes
	 *
	 * @return
	 */
	@GetMapping("/notes")
	public List<Note> getAllNotes() {
		return noteRepository.findAll();
	}

	/**
	 * create a new note
	 *
	 * @param note
	 * @return
	 */
	@PostMapping("/notes")
	public Note createNote(@Valid @RequestBody Note note) { //@valid validates the note model
		return noteRepository.save(note);
	}

	/**
	 * get a single note
	 *
	 * @param noteId
	 * @return
	 */
//	@GetMapping("/notes/{id}")
//	public Note getNoteById(@PathVariable(value = "id") Long noteId) {
//		Note note = noteRepository.findOne(noteId);
//		if(Objects.isNull(note)) {
//			throw new ResourceNotFoundException("Note", "id", noteId);
//		}
//		return note;
//	}

	/**
	 * update a note
	 *
	 * @param noteId
	 * @param noteDetails
	 * @return
	 */
//	@PostMapping("/notes/{id}")
//	public Note updateNote(@PathVariable(value = "id") Long noteId, @Valid @RequestBody Note noteDetails) {
//		Note note = getNoteById(noteId);
//		note.setTitle(noteDetails.getTitle());
//		note.setContent(noteDetails.getContent());
//		return noteRepository.save(note);
//	}

	/**
	 * delete a note
	 *
	 * @param noteId
	 * @return
	 */
//	@DeleteMapping("/notes/{id}")
//	public ResponseEntity<?> deleteNote(@PathVariable(value = "id") Long noteId) {
//		Note note = getNoteById(noteId);
//		noteRepository.delete(note);
//		return ResponseEntity.ok().build();
//	}

}