package com.example.demo.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;

@Repository
public class UserMasterDao {
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	/**
	 * 検索条件に紐づくユーザを検索する
	 * @param user_id ユーザID
	 * @param user_name ユーザ名
	 * @param authority_checkbox 権限チェックボックス
	 * @param live_del_checkbox 正規ユーザ／削除済ユーザチェックボックス
	 * @return 検索条件に紐づく【ユーザ】データ
	 */
	public List<User> findUser(String user_id, String user_name, String[] authority_checkbox, String[] live_del_checkbox) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ");
		query.append("	usr.user_id ");
		query.append("	, usr.user_name ");
		query.append("	, usr.del_flg ");
		query.append("	, usr.authority ");
		query.append("FROM ");
		query.append("	usr ");

		// WHERE句が1条件でも適用されているかのフラグ
		boolean existsCondition = false;
		// PreparedStatementに渡す引数
		List<String> args = new ArrayList<>();

		// ユーザIDが入力されている場合
		if (!user_id.isEmpty()) {
			query.append("WHERE ");
			query.append("	usr.user_id LIKE ? ");
			args.add("%" + user_id + "%");
			existsCondition = true;
		}
		
		// ユーザ名が入力されている場合
		if (!user_name.isEmpty()) {
			if (existsCondition) {
				query.append("	AND ");
			} else {
				query.append("WHERE ");
				existsCondition = true;
			}
			query.append("	usr.user_name LIKE ? ");
			args.add("%" + user_name + "%");
		}
		
		// 権限チェックボックスがチェックされている場合
		if (authority_checkbox != null) {
			List<String> authorityCheckbox = Arrays.asList(authority_checkbox);
			// 一般ユーザがチェックされている かつ 管理者がチェックされていない場合
			if (authorityCheckbox.contains("general") && !authorityCheckbox.contains("admin")) {
				if (existsCondition) {
					query.append("	AND ");
				} else {
					query.append("WHERE ");
					existsCondition = true;
				}
				
				query.append("	usr.authority = 'GENERAL' ");
			// 一般ユーザがチェックされていない かつ 管理者がチェックされている場合
			} else if (!authorityCheckbox.contains("general") && authorityCheckbox.contains("admin")) {
				if (existsCondition) {
					query.append("	AND ");
				} else {
					query.append("WHERE ");
					existsCondition = true;
				}
				
				query.append("	usr.authority = 'ADMIN' ");
			}
		}
		
		// 正規ユーザ／削除済ユーザチェックボックスがチェックされている場合
		if (live_del_checkbox != null) {
			List<String> liveDelCheckbox = Arrays.asList(live_del_checkbox);
			// 正規ユーザがチェックされている かつ 削除済ユーザがチェックされていない場合
			if (liveDelCheckbox.contains("live") && !liveDelCheckbox.contains("del")) {
				if (existsCondition) {
					query.append("	AND ");
				} else {
					query.append("WHERE ");
					existsCondition = true;
				}
				
				query.append("	usr.del_flg IS NOT NULL ");
			// 正規ユーザがチェックされていない かつ 削除済ユーザがチェックされている場合
			} else if (!liveDelCheckbox.contains("live") && liveDelCheckbox.contains("del")) {
				if (existsCondition) {
					query.append("	AND ");
				} else {
					query.append("WHERE ");
					existsCondition = true;
				}
				
				query.append("	usr.del_flg IS NULL ");
			}
		}
		
		try {
			List<Map<String, Object>> userList = jdbcTemplate.queryForList(query.toString(), args.toArray());
			
			List<User> result = new ArrayList<>();
			
			for (Map<String, Object> map : userList) {
				User user = new User();

				user.setUser_id((String) map.get("user_id"));
				user.setUser_name((String) map.get("user_name"));
				user.setDel_flg((String) map.get("del_flg"));
				user.setAuthority((String) map.get("authority"));
				result.add(user);
			}
			return result;
		// データが0件だった場合
		} catch (EmptyResultDataAccessException e) {
			return Collections.emptyList();
		}
	}

	/**
	 * 検索条件に紐づくユーザ件数を検索する
	 * @param user_id ユーザID
	 * @param user_name ユーザ名
	 * @param authority_checkbox 権限チェックボックス
	 * @param live_del_checkbox 正規ユーザ／削除済ユーザチェックボックス
	 * @return 検索条件に紐づく【ユーザ】データ件数
	 */
	@SuppressWarnings("deprecation")
	public int findUserCount(String user_id, String user_name, String[] authority_checkbox, String[] live_del_checkbox) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ");
		query.append("	COUNT(*) ");
		query.append("FROM ");
		query.append("	usr ");

		// WHERE句が1条件でも適用されているかのフラグ
		boolean existsCondition = false;
		// PreparedStatementに渡す引数
		List<String> args = new ArrayList<>();

		// ユーザIDが入力されている場合
		if (!user_id.isEmpty()) {
			query.append("WHERE ");
			query.append("	usr.user_id LIKE ? ");
			args.add("%" + user_id + "%");
			existsCondition = true;
		}
		
		// ユーザ名が入力されている場合
		if (!user_name.isEmpty()) {
			if (existsCondition) {
				query.append("	AND ");
			} else {
				query.append("WHERE ");
				existsCondition = true;
			}
			query.append("	usr.user_name LIKE ? ");
			args.add("%" + user_name + "%");
		}
		
		// 権限チェックボックスがチェックされている場合
		if (authority_checkbox != null) {
			List<String> authorityCheckbox = Arrays.asList(authority_checkbox);
			// 一般ユーザがチェックされている かつ 管理者がチェックされていない場合
			if (authorityCheckbox.contains("general") && !authorityCheckbox.contains("admin")) {
				if (existsCondition) {
					query.append("	AND ");
				} else {
					query.append("WHERE ");
					existsCondition = true;
				}
				
				query.append("	usr.authority = 'GENERAL' ");
			// 一般ユーザがチェックされていない かつ 管理者がチェックされている場合
			} else if (!authorityCheckbox.contains("general") && authorityCheckbox.contains("admin")) {
				if (existsCondition) {
					query.append("	AND ");
				} else {
					query.append("WHERE ");
					existsCondition = true;
				}
				
				query.append("	usr.authority = 'ADMIN' ");
			}
		}
		
		// 正規ユーザ／削除済ユーザチェックボックスがチェックされている場合
		if (live_del_checkbox != null) {
			List<String> liveDelCheckbox = Arrays.asList(live_del_checkbox);
			// 正規ユーザがチェックされている かつ 削除済ユーザがチェックされていない場合
			if (liveDelCheckbox.contains("live") && !liveDelCheckbox.contains("del")) {
				if (existsCondition) {
					query.append("	AND ");
				} else {
					query.append("WHERE ");
					existsCondition = true;
				}
				
				query.append("	usr.del_flg IS NOT NULL ");
			// 正規ユーザがチェックされていない かつ 削除済ユーザがチェックされている場合
			} else if (!liveDelCheckbox.contains("live") && liveDelCheckbox.contains("del")) {
				if (existsCondition) {
					query.append("	AND ");
				} else {
					query.append("WHERE ");
					existsCondition = true;
				}
				
				query.append("	usr.del_flg IS NULL ");
			}
		}
		
		return jdbcTemplate.queryForObject(query.toString(), args.toArray(), Integer.class);
	}
	
	/**
	 * ユーザの存在チェック
	 * @param user_id ユーザID
	 * @return ユーザが存在する場合はtrue、存在しない場合はfalse
	 */
	@SuppressWarnings("deprecation")
	public boolean existsUser(String user_id) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ");
		query.append("	1 ");
		query.append("FROM ");
		query.append("	usr ");
		query.append("WHERE ");
		query.append("	usr.user_id = ? ");
		
		try {
			jdbcTemplate.queryForObject(query.toString(), new String[] {user_id}, Integer.class);
			
			return true;
		// データが0件だった場合
		} catch (EmptyResultDataAccessException e) {
			return false;
		}
	}
	
	/**
	 * ユーザを登録する
	 * @param user_id ユーザID
	 * @param user_name ユーザ名
	 * @param password パスワード
	 * @param authority_radiobutton 権限ラジオボタン
	 * @return 登録に成功した場合はtrue、失敗した場合はfalse
	 */
	public int insUser(String user_id, String user_name, String password, String[] authority_radiobutton) {
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO ");
		query.append("	usr( ");
		query.append("		user_id ");
		query.append("		, user_name ");
		query.append("		, password ");
		query.append("		, del_flg ");
		query.append("		, authority ");
		query.append("	) ");
		query.append("	VALUES( ");
		query.append("		? ");
		query.append("		, ? ");
		query.append("		, ? ");
		query.append("		, ? ");
		query.append("		, ? ");
		query.append("	) ");
		
		return jdbcTemplate.update(query.toString(), user_id, user_name, password, user_id, authority_radiobutton[0].toUpperCase());
	}
	
	/**
	 * ユーザIDに紐づくユーザ情報を取得する
	 * @param user_id ユーザID
	 * @return ユーザIDに紐づく【ユーザ】データ
	 */
	public User getUserInfo(String user_id) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ");
		query.append("	usr.user_id ");
		query.append("	, usr.user_name ");
		query.append("	, usr.password ");
		query.append("	, usr.del_flg ");
		query.append("	, usr.authority ");
		query.append("FROM ");
		query.append("	usr ");
		query.append("WHERE ");
		query.append("	usr.user_id = ? ");
		
		try {
			Map<String, Object> map = jdbcTemplate.queryForMap(query.toString(), user_id);
			User user = new User();
			user.setUser_id((String) map.get("user_id"));
			user.setUser_name((String) map.get("user_name"));
			user.setPassword((String) map.get("password"));
			user.setDel_flg((String) map.get("del_flg"));
			user.setAuthority((String) map.get("authority"));

			return user;
		// データが0件だった場合
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
}
