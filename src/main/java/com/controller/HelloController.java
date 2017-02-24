package com.controller;

import com.request.BookRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.entity.Book;
import com.services.BookServiceImpl;
import com.services.PublisherServiceImpl;

/**
 * @author Vinit Solanki
 *
 */
@RestController
public class HelloController {

	private static final Logger log = Logger.getLogger(HelloController.class);

	@Autowired
	private BookServiceImpl bookRepository;

	@Autowired
	private PublisherServiceImpl publisherRepository;

	@RequestMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}

	@RequestMapping("/getEnPass/{password}")
	public String getPass(@PathVariable("password") String password) {
		return new BCryptPasswordEncoder().encode(password);
	}

	@RequestMapping("/home")
	public ResponseEntity<?> home() {

		//throw new ArithmeticException();

		return ResponseEntity.ok("Home");

		//return "HOME Page";
	}

	@PreAuthorize("hasAuthority('FAKE')")
	@RequestMapping("/fake")
	public String fake() {
		return "fake Page";
	}

	@PreAuthorize("hasAuthority('GET_TEST')")
	@RequestMapping("/roletest")
	public String test() {
		return "hello Page";
	}

	@PreAuthorize("hasAuthority('USER')")
	@RequestMapping("/hasauthority")
	public String usertest() {
		return "usertest";
	}

	@PreAuthorize("hasAuthority('USER')")
	@RequestMapping("/hasrole")
	public String hasrole() {
		return "hello Page";
	}

	@PreAuthorize("hasAuthority('ROLE_USER')")
	@RequestMapping("/hasrole_role")
	public String hasrole_role() {
		return "hello Page";
	}

	@RequestMapping("/book")
	public ResponseEntity<?> getAllBooks() {

		return ResponseEntity.ok(bookRepository.findAll());

	}

	@RequestMapping("/onebook")
	public ResponseEntity<?> getBook() {

		Book book = bookRepository.findOne(2);

		BookRequest b1 = new BookRequest();
		b1.setName(book.getName());

		book.getPublishers().stream().forEach(publisher -> {
			System.out.println("publisher.getName() = " + publisher.getName());
		});

		b1.setPublishers(book.getPublishers());
		return ResponseEntity.ok(b1);

	}

	@RequestMapping("/publisher")
	public ResponseEntity<?> getAllPublishers() {

		return ResponseEntity.ok(publisherRepository.findAll());

	}

	@RequestMapping("/onepublisher")
	public ResponseEntity<?> getPublisher() {

		return ResponseEntity.ok(publisherRepository.findOne(2));

	}

}