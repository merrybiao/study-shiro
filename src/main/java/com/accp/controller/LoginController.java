package com.accp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.accp.base.BaseController;
import com.accp.common.utils.ServletUtils;
import com.accp.common.utils.StringUtils;
import com.accp.freamwork.web.domain.AjaxResult;

/**
 * 登录验证
 * 
 * @author Luyuan
 */
@Controller
public class LoginController extends BaseController {

	@GetMapping("/login")
	public String login(HttpServletRequest request, HttpServletResponse response) {
		// 如果是Ajax请求，返回Json字符串。
		if (ServletUtils.isAjaxRequest(request)) {
			return ServletUtils.renderString(response, "{\"code\":\"1\",\"msg\":\"未登录或登录超时。请重新登录\"}");
		}

		return "login";
	}

	@PostMapping("/login")
	@ResponseBody
	public AjaxResult ajaxLogin(String userName, String password, Boolean rememberMe) {
		UsernamePasswordToken token = new UsernamePasswordToken(userName, password, rememberMe);
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
			return success("登录成功~");
		} catch (AuthenticationException e) {
			String msg = "用户或密码错误";
			if (StringUtils.isNotEmpty(e.getMessage())) {
				msg = e.getMessage();
			}
			return error(msg);
		}
	}

	@GetMapping("/unauth")
	public String unauth() {
		return "error/unauth";
	}

}
