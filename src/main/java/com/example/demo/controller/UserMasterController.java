package com.example.demo.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.model.User;
import com.example.demo.model.UserMasterForm;
import com.example.demo.service.UserMasterService;


@Controller
public class UserMasterController {
	@Autowired
	UserMasterService userMasterService;
	
	@RequestMapping(value = {"/userMaster", "/userMasterSearch"}, method = RequestMethod.GET)
    private ModelAndView getUserMaster(@ModelAttribute UserMasterForm form, HttpSession session, ModelAndView mv) {
		String toUrl = "userMasterSearch";
		
		// 管理者ユーザでない場合
		if (!session.getAttribute("authority").equals("ADMIN")) {
			toUrl = "noAuthority";
		}
		
		// 権限チェックボックス
		mv.addObject("authority_checkbox", getAuthorityCheckBox());
		
		// 正規ユーザ／削除済ユーザチェックボックス
		mv.addObject("live_del_checkbox", getLiveDelCheckBox());
		
		// ユーザ名をModelAndViewにセット
		ControllerUtil.setUserNameToModelAndView(session, mv);
		mv.setViewName(toUrl);
        return mv;
    }
	
	@RequestMapping(value = "/userMasterSearch", method = RequestMethod.POST)
	private ModelAndView postuserMasterSearch(@ModelAttribute @Validated UserMasterForm form,
			BindingResult result, HttpSession session, ModelAndView mv) {
		
		// エラーが発生した場合
		if (result.hasErrors()) {
			return getUserMaster(form, session, mv);
		}
		
		// フォーム値を元に該当するユーザを検索
		List<User> userList = userMasterService.findUser(form.getUser_id(), form.getUser_name(),
				form.getAuthority_checkbox(), form.getLive_del_checkbox());
		// 結果が0件の場合
		if (userList.size() == 0) {
			mv.addObject("error", true);
			return getUserMaster(form, session, mv);
		// ユーザが存在する場合
		} else {
			mv.addObject("userList", userList);
			// フォーム値を元に該当するユーザ件数を取得しセット
			mv.addObject("recordCount", userMasterService.findUserCount(form.getUser_id(), form.getUser_name(),
					form.getAuthority_checkbox(), form.getLive_del_checkbox()) + "件");
		}

		// 権限チェックボックス
		mv.addObject("authority_checkbox", getAuthorityCheckBox());
		
		// 正規ユーザ／削除済ユーザチェックボックス
		mv.addObject("live_del_checkbox", getLiveDelCheckBox());
		
		// ユーザ名をModelAndViewにセット
		ControllerUtil.setUserNameToModelAndView(session, mv);
		mv.setViewName("userMasterSearchResult");
		return mv;	
	}
	
	/**
	 * 権限チェックボックスを取得する
	 * @return 権限チェックボックス
	 */
	private Map<String, String> getAuthorityCheckBox() {
		Map<String, String> authorityCheckBox = new LinkedHashMap<>();
		authorityCheckBox.put("general", "一般ユーザ");
		authorityCheckBox.put("admin", "管理者");
		return authorityCheckBox;
	}
	
	/**
	 * 正規ユーザ／削除済ユーザチェックボックスを取得する
	 * @return 正規ユーザ／削除済ユーザチェックボックス
	 */
	private Map<String, String> getLiveDelCheckBox() {
		Map<String, String> authorityCheckBox = new LinkedHashMap<>();
		authorityCheckBox.put("live", "正規ユーザ");
		authorityCheckBox.put("del", "削除済ユーザ");
		return authorityCheckBox;
	}
}
