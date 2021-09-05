package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.LoginUserDao;

@Service
public class LoginUserService {
	@Autowired
	private LoginUserDao userDao;
	
	public User findUser(String user_id, String password) {
		return userDao.findUser(user_id, password);
	}
}
