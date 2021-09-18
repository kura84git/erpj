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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.model.User;
import com.example.demo.model.UserMasterRegForm;
import com.example.demo.model.UserMasterSearchForm;
import com.example.demo.service.UserMasterService;


@Controller
public class UserMasterController {
	@Autowired
	UserMasterService userMasterService;
	
	/**
	 * ユーザメンテナンス検索画面
	 * @param form UserMasterSearchForm
	 * @param session HttpSession
	 * @param mv ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping(value = {"/userMaster", "/userMasterSearch"}, method = RequestMethod.GET)
    private ModelAndView getUserMaster(@ModelAttribute UserMasterSearchForm form, HttpSession session, ModelAndView mv) {
		String toUrl = "userMasterSearch";
		
		// 管理者ユーザでない場合
		if (!session.getAttribute("authority").equals("ADMIN")) {
			mv.addObject("error", "権限がありません。");
			toUrl = "failure";
		}
		
		// 権限チェックボックス
		mv.addObject("authority_checkbox", getAuthorityCheckbox());
		
		// 正規ユーザ／削除済ユーザチェックボックス
		mv.addObject("live_del_checkbox", getLiveDelCheckbox());
		
		// フォーム入力値
		mv.addObject("userMasterSearchForm", form);
		
		// ユーザ名をModelAndViewにセット
		ControllerUtil.setUserNameToModelAndView(session, mv);
		mv.setViewName(toUrl);
        return mv;
    }
	
	/**
	 * ユーザメンテナンス検索画面検索時
	 * @param form UserMasterSearchForm
	 * @param result BindingResult
	 * @param session HttpSession
	 * @param mv ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/userMasterSearch", method = RequestMethod.POST)
	private ModelAndView postUserMasterSearch(@ModelAttribute @Validated UserMasterSearchForm form,
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
		mv.addObject("authority_checkbox", getAuthorityCheckbox());
		
		// 正規ユーザ／削除済ユーザチェックボックス
		mv.addObject("live_del_checkbox", getLiveDelCheckbox());
		
		// フォーム入力値
		mv.addObject("userMasterSearchForm", form);
		
		// ユーザ名をModelAndViewにセット
		ControllerUtil.setUserNameToModelAndView(session, mv);
		mv.setViewName("userMasterSearchResult");
		return mv;	
	}
	
	/**
	 * ユーザメンテナンス登録画面
	 * @param form UserMasterRegForm
	 * @param session HttpSession
	 * @param mv ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping(value = "userMasterReg", method = RequestMethod.GET)
    private ModelAndView getUserMasterReg(@ModelAttribute UserMasterRegForm form,
    		HttpSession session, ModelAndView mv) {
		// 権限ラジオボタン
		mv.addObject("authority_radiobutton", getAuthorityRadiobutton());
		
		// ラジオボタンの初期値セット
		form.setAuthority_radiobutton(new String[] {"general"});
		
		// フォーム入力値
		mv.addObject("userMasterRegForm", form);
		
		// ユーザ名をModelAndViewにセット
		ControllerUtil.setUserNameToModelAndView(session, mv);
		mv.setViewName("userMasterReg");
        return mv;
    }
	
	/**
	 * ユーザメンテナンス登録確認画面
	 * @param form UserMasterRegForm
	 * @param result BindingResult
	 * @param session HttpSession
	 * @param mv ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/userMasterRegConfirm", method = RequestMethod.POST)
	private ModelAndView postUserMasterRegConfirm(@ModelAttribute @Validated UserMasterRegForm form,
			BindingResult result, HttpSession session, ModelAndView mv) {
		String toUrl = "userMasterRegConfirm";
		// エラーが発生した場合
		if (result.hasErrors()) {
			toUrl = "userMasterReg";
			// 権限ラジオボタン
			mv.addObject("authority_radiobutton", getAuthorityRadiobutton());
		}
		
		// ユーザが既に存在する場合
		if (userMasterService.existsUser(form.getUser_id())) {
			toUrl = "userMasterReg";
			mv.addObject("error_userExists", true);
			// 権限ラジオボタン
			mv.addObject("authority_radiobutton", getAuthorityRadiobutton());
		}
		
		// フォーム入力値をセッションスコープにセット
		session.setAttribute("userMasterRegForm", form);
		
		// フォーム入力値
		mv.addObject("userMasterRegForm", form);
		
		// 一般ユーザを選択した場合
		if (form.getAuthority_radiobutton()[0].equals("general")) {
			mv.addObject("authority", "一般ユーザ");
		// 管理者を選択した場合
		} else {
			mv.addObject("authority", "管理者");
		}
		
		// ユーザ名をModelAndViewにセット
		ControllerUtil.setUserNameToModelAndView(session, mv);
		mv.setViewName(toUrl);
		return mv;	
	}
	
	/**
	 * ユーザメンテナンス登録確認画面中止ボタン押下時
	 * @param form UserMasterRegForm
	 * @param session HttpSession
	 * @param mv ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping(value = "userMasterRegConfirm/cancel", method = RequestMethod.GET)
    private ModelAndView getUserMasterRegConfirmCancel(@ModelAttribute UserMasterRegForm form,
    		HttpSession session, ModelAndView mv) {
		// 権限ラジオボタン
		mv.addObject("authority_radiobutton", getAuthorityRadiobutton());
		
		// フォーム入力値をセッションスコープから取得
		mv.addObject("userMasterRegForm", session.getAttribute("userMasterRegForm"));
		
		// ユーザ名をModelAndViewにセット
		ControllerUtil.setUserNameToModelAndView(session, mv);
		mv.setViewName("userMasterReg");
        return mv;
    }
	
	/**
	 * ユーザメンテナンス登録確認画面登録ボタン押下時
	 * @param form UserMasterRegForm
	 * @param result BindingResult
	 * @param session HttpSession
	 * @param mv ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/userMasterReg", method = RequestMethod.POST)
	private ModelAndView postUserMasterReg(@ModelAttribute @Validated UserMasterRegForm form,
			BindingResult result, HttpSession session, ModelAndView mv) {
		String toUrl = "userMasterRegResult";
		
		// フォーム入力値をセッションスコープから取得
		form = (UserMasterRegForm) session.getAttribute("userMasterRegForm");
		
		// ユーザを登録
		if (userMasterService.insUser(form.getUser_id(),
				form.getUser_name(),
				form.getPassword(),
				form.getAuthority_radiobutton()) != 1) {
			mv.addObject("error", "処理に失敗しました。");
			toUrl = "failure";
		}
		
		// フォーム入力値
		mv.addObject("userMasterRegForm", form);
		
		// 一般ユーザを選択した場合
		if (form.getAuthority_radiobutton()[0].equals("general")) {
			mv.addObject("authority", "一般ユーザ");
		// 管理者を選択した場合
		} else {
			mv.addObject("authority", "管理者");
		}
		
		// ユーザ名をModelAndViewにセット
		ControllerUtil.setUserNameToModelAndView(session, mv);
		mv.setViewName(toUrl);
		return mv;	
	}
	
	/**
	 * ユーザメンテナンス照会画面
	 * @param user_id ユーザID
	 * @param session HttpSession
	 * @param mv ModelAndView
	 * @return ModelAndView
	 */
	@RequestMapping(value = "userMasterDetail/{user_id}", method = RequestMethod.GET)
    private ModelAndView getUserMasterDetail(@PathVariable("user_id") String user_id,
    		HttpSession session, ModelAndView mv) {
		// ユーザ情報を取得
		User user = userMasterService.getUserInfo(user_id);
		
		// ユーザ情報をセット
		mv.addObject("userMasterDetail", user);
		
		// ユーザ情報をセッションスコープにセット
		session.setAttribute("userMasterDetail", user);
		
		// ユーザ名をModelAndViewにセット
		ControllerUtil.setUserNameToModelAndView(session, mv);
		mv.setViewName("userMasterDetail");
        return mv;
    }
	
	/**
	 * 権限チェックボックスを取得する
	 * @return 権限チェックボックス
	 */
	private Map<String, String> getAuthorityCheckbox() {
		Map<String, String> authorityCheckbox = new LinkedHashMap<>();
		authorityCheckbox.put("general", "一般ユーザ");
		authorityCheckbox.put("admin", "管理者");
		return authorityCheckbox;
	}
	
	/**
	 * 権限ラジオボタンを取得する
	 * @return 権限チェックボックス
	 */
	private Map<String, String> getAuthorityRadiobutton() {
		Map<String, String> authorityRadiobutton = new LinkedHashMap<>();
		authorityRadiobutton.put("general", "一般ユーザ");
		authorityRadiobutton.put("admin", "管理者");
		return authorityRadiobutton;
	}
	
	/**
	 * 正規ユーザ／削除済ユーザチェックボックスを取得する
	 * @return 正規ユーザ／削除済ユーザチェックボックス
	 */
	private Map<String, String> getLiveDelCheckbox() {
		Map<String, String> liveDelCheckbox = new LinkedHashMap<>();
		liveDelCheckbox.put("live", "正規ユーザ");
		liveDelCheckbox.put("del", "削除済ユーザ");
		return liveDelCheckbox;
	}
}
