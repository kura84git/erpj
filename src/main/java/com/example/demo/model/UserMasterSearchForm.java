package com.example.demo.model;

import lombok.Data;

@Data
public class UserMasterSearchForm {
	private String user_id;

	private String user_name;
	
	private String[] authority_checkbox;
	
	private String[] live_del_checkbox;
}
