package com.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.repository.BookRepository;
import com.entity.Book;

/**
 * @author Vinit Solanki
 *
 */
@Service
@Transactional
public class BookServiceImpl {

	@Autowired
	private BookRepository bookDAO;

	public Iterable<Book> findAll() {
		return bookDAO.findAll();
	}

	public Book findOne(int id) {
		return bookDAO.findOne(id);
	}

}
