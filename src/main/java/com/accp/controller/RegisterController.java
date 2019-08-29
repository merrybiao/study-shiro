package com.accp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.accp.base.BaseController;
import com.accp.common.utils.ServletUtils;
import com.accp.entity.SmsUser;
import com.accp.freamwork.web.domain.AjaxResult;
import com.accp.service.SmsUserService;
import com.accp.vo.SmsUserVo;
import com.google.code.kaptcha.Constants;

/**
 * 注册用户
 * 
 * @author Luyuan
 */
@Controller
public class RegisterController extends BaseController {

	@Autowired
	private SmsUserService smsUserService;

	@GetMapping("/register")
	public String register(HttpServletRequest request, HttpServletResponse response) {
		// 如果是Ajax请求，返回Json字符串。
		if (ServletUtils.isAjaxRequest(request)) {
			return ServletUtils.renderString(response, "{\"code\":\"1\",\"msg\":\"未登录或登录超时。请重新登录\"}");
		}

		return "register";
	}

	@GetMapping("/register_success")
	public String registerSuccess(HttpServletRequest request, HttpServletResponse response) {
		return "register_success";
	}

	@PostMapping("/register")
	@ResponseBody
	public AjaxResult ajaxRegister(@RequestBody SmsUserVo vo, HttpServletRequest request) {
		HttpSession session = request.getSession();
		Object obj = session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
		String code = String.valueOf(obj);

		if (!vo.getValidateCode().equals(code)) {
			return error("验证码不正确~");
		}

		SmsUser user = new SmsUser();
		user.setLoginName(vo.getUserName());
		user.setUserName(vo.getUserName());
		user.setPassword(vo.getPassword());
		user.setMobileNo(vo.getMobileNo());
		user.setEmail(vo.getEmail());

		boolean flag = smsUserService.insertUser(user);
		if (flag) {
			return success();
		} else {
			return error("注册失败");
		}
	}
}
