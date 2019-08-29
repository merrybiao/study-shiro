package com.accp.shiro.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.accp.common.constant.ShiroConstants;
import com.accp.common.constant.UserConstants;
import com.accp.common.utils.ServletUtils;
import com.accp.common.utils.ShiroUtils;
import com.accp.entity.SmsUser;
import com.accp.enums.UserStatus;
import com.accp.service.SmsUserService;

/**
 * 登录校验方法
 * 
 * @author Luyuan
 */
@Component
public class LoginService {

	@Autowired
	private SmsUserService userService;

	/**
	 * 登录
	 */
	public SmsUser login(String username, String password) {
		// 验证码校验
		if (!StringUtils.isEmpty(ServletUtils.getRequest().getAttribute(ShiroConstants.CURRENT_CAPTCHA))) {
			throw new RuntimeException();
		}
		// 用户名或密码为空 错误
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			throw new RuntimeException();
		}

		// 用户名不在指定范围内 错误
		if (username.length() < UserConstants.USERNAME_MIN_LENGTH
				|| username.length() > UserConstants.USERNAME_MAX_LENGTH) {
			throw new RuntimeException();
		}

		// 查询用户信息
		SmsUser user = userService.selectUserByLoginName(username);

		if (user == null) {
			throw new RuntimeException("用户名不正确~");
		}

		if (UserStatus.DELETED.getCode().equals(user.getStatus())) {
			throw new RuntimeException("用户不存在或已删除");
		}

		if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
			throw new RuntimeException("用户已停用，请联系管理员");
		}
		
		if(!password.equals(user.getPassword())){
			throw new RuntimeException("用户名或密码错误");
		}

		recordLoginInfo(user);
		return user;
	}

	/**
	 * 记录登录信息
	 */
	public void recordLoginInfo(SmsUser user) {
		user.setLoginIp(ShiroUtils.getIp());
		user.setLastLoginTime(LocalDateTime.now());
		userService.updateUserInfo(user);
	}
}
