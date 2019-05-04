package com.boot.proj.easynote.controller;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boot.proj.easynote.exception.ResourceNotFoundException;
import com.boot.proj.easynote.model.Note;
import com.boot.proj.easynote.repository.NoteRepository;

/**
 * note controller class for all rest endpoints
 * 
 * @author Srinath.Rayabarapu
 *
 */
@RestController
@RequestMapping("/api")
public class NoteController {

	@Autowired
	NoteRepository noteRepository;

	// get all notes
	@GetMapping("/notes")
	public List<Note> getAllNotes() {
		return noteRepository.findAll();
	}

	// create a new note
	@PostMapping("notes")
	public Note createNote(@Valid @RequestBody Note note) { //@valid validates the note model
		return noteRepository.save(note);
	}

	// get a single note
	@GetMapping("/notes/{id}")
	public Note getNoteById(@PathVariable(value = "id") Long noteId) {
		Note note = noteRepository.findOne(noteId);
		if(Objects.isNull(note)) {
			throw new ResourceNotFoundException("Note", "id", noteId);
		}
		return note;
	}

	// update a note
	@PostMapping("/notes/{id}")
	public Note updateNote(@PathVariable(value = "id") Long noteId, @Valid @RequestBody Note noteDetails) {
		Note note = getNoteById(noteId);

		note.setTitle(noteDetails.getTitle());
		note.setContent(noteDetails.getContent());

		return noteRepository.save(note);

	}

	// delete a note
	@DeleteMapping("/notes/{id}")
	public ResponseEntity<?> deleteNote(@PathVariable(value = "id") Long noteId) {

		Note note = getNoteById(noteId);

		noteRepository.delete(note);

		return ResponseEntity.ok().build();

	}

}
