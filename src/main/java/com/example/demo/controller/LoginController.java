package com.example.demo.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.model.LoginForm;
import com.example.demo.model.User;
import com.example.demo.service.LoginUserService;


@Controller
public class LoginController {
	@Autowired
	LoginUserService loginUserService;
	
	/**
	 * ログイン画面
	 * @param form LoginForm
	 * @param mv ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping(value = {"/", "/login"}, method = RequestMethod.GET)
    private ModelAndView getLogin(@ModelAttribute LoginForm form, ModelAndView mv) {
        mv.setViewName("login");
        return mv;
    }
	
	/**
	 * ログイン画面ログイン時
	 * @param form LoginForm
	 * @param result BindingResult
	 * @param session HttpSession
	 * @param mv ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	private ModelAndView postlogin(@ModelAttribute @Validated LoginForm form,
			BindingResult result, HttpSession session, ModelAndView mv) {
		String toUrl = null;
		
		// エラーが発生した場合
		if (result.hasErrors()) {
			return getLogin(form, mv);
		}
		
		// フォーム値を元に該当するユーザを検索
		User user = loginUserService.findUser(form.getUser_id(), form.getPassword());
		// ユーザ名またはパスワードが不正の場合
		if (user == null) {
			mv.addObject("error", true);
			toUrl = "login";
		// ユーザが存在する場合
		} else {
			// ユーザIDをセッションスコープにセット
			session.setAttribute("login_user_id", user.getUser_id());
			// 権限をセッションスコープにセット
			session.setAttribute("login_user_authority", user.getAuthority());

			String user_name = user.getUser_name();
			// ユーザ名をセッションスコープにセット
			session.setAttribute("login_user_name", user_name);

			mv.addObject("login_user_name", user_name);
			toUrl = "top";
		}
		
		mv.setViewName(toUrl);

		return mv;	
	}
}
