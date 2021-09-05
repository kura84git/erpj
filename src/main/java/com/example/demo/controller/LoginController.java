package com.example.demo.controller;

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
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
    public String getLogin(@ModelAttribute LoginForm form, ModelAndView model) {
        return "login";
    }
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	private String postlogin(@ModelAttribute @Validated LoginForm form,
			BindingResult result, ModelAndView model) {
		String toUrl = null;
		
		// エラーが発生した場合
		if (result.hasErrors()) {
			return getLogin(form, model);
		}
		
		User user = loginUserService.findUser(form.getUser_id(), form.getPassword());
		// ユーザ名またはパスワードが不正の場合
		if (user == null) {
			model.addObject("error", true);
			toUrl = "login";
		} else {
			model.addObject("user_name", user.getUser_name());
			toUrl = "main";
		}

		return toUrl;	
	}
}
