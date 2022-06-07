package com.boot.jpa.multidatasource;

import com.boot.jpa.multidatasource.model.Book;
import com.boot.jpa.multidatasource.model.User;
import com.boot.jpa.multidatasource.repository.book.BookRepository;
import com.boot.jpa.multidatasource.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Srinath.Rayabarapu
 */
@SpringBootApplication
@RestController
public class SpringBootJPAMultiDatasourceApp {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootJPAMultiDatasourceApp.class, args);
	}

	@PostConstruct
	public void addDataToDB(){
		userRepository.saveAll(Stream.of(new User(744, "John"), new User(455, "Pitter")).collect(Collectors.toList()));
		bookRepository.saveAll(
				Stream.of(new Book(111, "Core Java"), new Book(222, "Spring Boot")).collect(Collectors.toList()));
	}

	@GetMapping("/getUsers")
	public List<User> getUsers(){
		return userRepository.findAll();
	}

	@GetMapping("/getBooks")
	public List<Book> getBooks(){
		return bookRepository.findAll();
	}
	
}