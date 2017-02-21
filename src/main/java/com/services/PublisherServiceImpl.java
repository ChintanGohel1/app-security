package com.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dao.PublisherRepository;
import com.entities.Publisher;

/**
 * @author Vinit Solanki
 *
 */
@Service
@Transactional
public class PublisherServiceImpl {

	@Autowired
	private PublisherRepository publisherDAO;

	public List<Publisher> findAll() {
		return publisherDAO.findAll();
	}

	public Publisher findOne(int id) {
		return publisherDAO.findOne(id);
	}

}
