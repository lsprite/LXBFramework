package com.nemo.cloudedittext.bean;

import java.io.Serializable;

/**
 * 联系人
 */
public class ContactBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name = "";
	private String email = "";

	public ContactBean() {
		// TODO Auto-generated constructor stub
	}

	public ContactBean(String name, String email) {
		super();
		this.name = name;
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
