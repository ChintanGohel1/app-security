package com.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entities.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {

}
