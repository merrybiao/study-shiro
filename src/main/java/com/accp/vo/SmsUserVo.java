package com.accp.vo;

import lombok.Data;

@Data
public class SmsUserVo {

	/** 用户名称 */
	private String userName;

	/** 密码 */
	private String password;

	/** 用户邮箱 */
	private String email;

	/** 手机号码 */
	private String mobileNo;

	/** 验证码 */
	private String validateCode;

}
