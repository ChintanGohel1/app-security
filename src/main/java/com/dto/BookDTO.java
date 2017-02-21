package com.dto;

import java.util.Set;

import com.entities.Publisher;

/**
 * @author Vinit Solanki
 *
 */
public class BookDTO {

	private int id;
	private String name;
	private Set<Publisher> publishers;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Publisher> getPublishers() {
		return publishers;
	}

	public void setPublishers(Set<Publisher> publishers) {
		this.publishers = publishers;
	}

}
