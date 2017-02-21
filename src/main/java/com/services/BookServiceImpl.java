package com.services;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dao.BookRepository;
import com.entities.Book;
import com.exceptions.AlreadyExist;
import com.exceptions.EntityNotFound;
import com.google.common.base.Joiner;

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
