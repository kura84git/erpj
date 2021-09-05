package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "usr")
public class User {
	@Id
	@Column(name = "user_id")
	private String user_id;
	
	@Column(name = "user_name")
	private String user_name;
	
	@Column(name="password")
	private String password;

	@Column(name="del_flg")
	private String del_flg;
}
