package com.example.demo.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UserMasterUpdForm {
	private static final String NOT_NULL_MSG = "必須入力です。";
	
	private String user_id;

	@NotBlank(message = NOT_NULL_MSG)
	@Size(min = 1, max = 50, message = "1文字以上50文字以内で入力して下さい。")
	private String user_name;
	
	@NotBlank(message = NOT_NULL_MSG)
	@Size(min = 1, max = 20, message = "1文字以上20文字以内で入力して下さい。")
	private String password;
	
	private String[] authority_radiobutton;
	
	private String[] live_del_radiobutton;
}
