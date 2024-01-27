package com.bookstore.demo.model;

import jakarta.persistence.Entity;

@Entity
public class Admin extends User {
	
	private String job = "ADMIN";
	
	public String getJob() {
		return job;
	}
}
