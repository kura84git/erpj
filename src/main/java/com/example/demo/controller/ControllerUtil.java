package com.example.demo.controller;

import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;

public class ControllerUtil {
	private ControllerUtil() {}
	
	/**
	 * ユーザ名をModelAndViewにセットする
	 * @param session セッション
	 * @param mv ModelAndView
	 */
	protected static void setUserNameToModelAndView(HttpSession session, ModelAndView mv) {
		mv.addObject("user_name", session.getAttribute("user_name"));
	}
}
