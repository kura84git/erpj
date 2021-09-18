package com.example.demo.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class LogoutController {
	
	/**
	 * ログアウト画面
	 * @param session HttpSession
	 * @return logout.htmlに遷移
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	private String getLogout(HttpSession session) {
		// セッション破棄
		session.invalidate();
		
		return "logout";
	}
}
