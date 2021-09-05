package com.example.demo.repository;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;

@Repository
public class LoginUserDao {
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public User findUser(String user_id, String password) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ");
		query.append("	usr.user_id ");
		query.append("	, usr.user_name ");
		query.append("	, usr.password ");
		query.append("	, usr.del_flg ");
		query.append("FROM ");
		query.append("	usr ");
		query.append("WHERE ");
		query.append("	usr.del_flg IS NOT NULL ");
		query.append("	AND usr.user_id = ? ");
		query.append("	AND usr.password = ? ");
		
		try {
			Map<String, Object> map = jdbcTemplate.queryForMap(query.toString(), user_id, password);
			User user = new User();
			user.setUser_id((String) map.get("user_id"));
			user.setUser_name((String) map.get("user_name"));
			user.setPassword((String) map.get("password"));
			user.setDel_flg((String) map.get("del_flg"));

			return user;
		// データが0件だった場合
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
}
