package com.blog.model;

import java.io.Serializable;


import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class Emp implements Serializable {

	private static final long serialVersionUID = -7552576367347894947L;
	
	private int id;

	
	private String First_Name;


	private String Last_Name;

	
	private String Username;

	@org.hibernate.validator.constraints.Email
	private String Email;


	private String Password;

	public int getId() {

		return id;
	}

	public void setId(int id) {

		this.id = id;
	}

	public String getFirst_Name() {
		return First_Name;
	}

	public void setFirst_Name(String first_Name) {

		this.First_Name = first_Name;
	}

	public String getLast_Name() {
		return Last_Name;
	}

	public void setLast_Name(String last_Name) {
		Last_Name = last_Name;
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}