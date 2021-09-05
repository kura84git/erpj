package com.example.demo.model;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LoginForm {
	private static final String NOT_NULL_MSG = "必須入力です。";
	
	@NotBlank(message = NOT_NULL_MSG)
	private String user_id;
	
	@NotBlank(message = NOT_NULL_MSG)
	private String password;
}
