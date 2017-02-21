package com.entities;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.dto.BookDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Book {
	private int id;
	private String name;
	private Set<Publisher> publishers;

	public Book() {

	}

	public Book(String name) {
		this.name = name;
	}

	public Book(String name, Set<Publisher> publishers) {
		this.name = name;
		this.publishers = publishers;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "book_publisher", joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "publisher_id", referencedColumnName = "id"))
	@JsonBackReference
	public Set<Publisher> getPublishers() {
		return publishers;
	}

	public void setPublishers(Set<Publisher> publishers) {
		this.publishers = publishers;
	}

//	public BookDTO toDTO() {
//		BookDTO bookDto = new BookDTO();
//		bookDto.setId(this.id);
//		bookDto.setName(this.name);
//		bookDto.setPublishers(this.publishers);
//		return bookDto;
//	}

	@Override
	public String toString() {
		String result = String.format(
		        "Book [id=%d, name='%s']%n",
		        id, name);
		if (publishers != null) {
			for (Publisher publisher : publishers) {
				result += String.format(
				        "Publisher[id=%d, name='%s']%n",
				        publisher.getId(), publisher.getName());
			}
		}

		return result;
	}
}
