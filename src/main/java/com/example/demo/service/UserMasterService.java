package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserMasterDao;

@Service
public class UserMasterService {
	@Autowired
	private UserMasterDao userMasterDao;
	
	/**
	 * 検索条件に紐づくユーザを検索する
	 * @param user_id ユーザID
	 * @param user_name ユーザ名
	 * @param authority_checkbox 権限チェックボックス
	 * @param live_del_checkbox 正規ユーザ／削除済ユーザチェックボックス
	 * @return 検索条件に紐づく【ユーザ】データ
	 */
	public List<User> findUser(String user_id, String user_name, String[] authority_checkbox, String[] live_del_checkbox) {
		return userMasterDao.findUser(user_id, user_name, authority_checkbox, live_del_checkbox);
	}
	
	/**
	 * 検索条件に紐づくユーザ件数を検索する
	 * @param user_id ユーザID
	 * @param user_name ユーザ名
	 * @param authority_checkbox 権限チェックボックス
	 * @param live_del_checkbox 正規ユーザ／削除済ユーザチェックボックス
	 * @return 検索条件に紐づく【ユーザ】データ件数
	 */
	public int findUserCount(String user_id, String user_name, String[] authority_checkbox, String[] live_del_checkbox) {
		return userMasterDao.findUserCount(user_id, user_name, authority_checkbox, live_del_checkbox);
	}
	
	/**
	 * ユーザの存在チェック
	 * @param user_id ユーザID
	 * @return ユーザが存在する場合はtrue、存在しない場合はfalse
	 */
	public boolean existsUser(String user_id) {
		return userMasterDao.existsUser(user_id);
	}
	
	/**
	 * ユーザを登録する
	 * @param user_id ユーザID
	 * @param user_name ユーザ名
	 * @param password　パスワード
	 * @param authority_radiobutton 権限ラジオボタン
	 * @return 登録に成功した場合はtrue、失敗した場合はfalse
	 */
	public int insUser(String user_id, String user_name, String password, String[] authority_radiobutton) {
		return userMasterDao.insUser(user_id, user_name, password, authority_radiobutton);
	}
	
	/**
	 * ユーザIDに紐づくユーザ情報を取得する
	 * @param user_id ユーザID
	 * @return ユーザIDに紐づく【ユーザ】データ
	 */
	public User getUserInfo(String user_id) {
		return userMasterDao.getUserInfo(user_id);
	}
}
