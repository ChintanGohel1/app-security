package com.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * @author Vinit Solanki
 *
 */
@Entity
public class PasswordResetToken extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -1113183893586199589L;

	private String token;

	@OneToOne
	private User user;

	private Date createdOn;

	public PasswordResetToken() {
		super();
	}

	public PasswordResetToken(String token, User user) {
		super();
		this.token = token;
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

}
