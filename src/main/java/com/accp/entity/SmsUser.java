package com.accp.entity;

import java.time.LocalDateTime;

import com.accp.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户对象 bw_sms_user
 * 
 * @author Luyuan
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("bw_sms_user")
public class SmsUser extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/** 登录名称 */
	private String loginName;

	/** 密码 */
	private String password;

	/** 用户名称 */
	private String userName;

	/** 用户性别 0=男,1=女,2=未知 */
	private String sex;

	/** 用户邮箱 */
	private String email;

	/** 手机号码 */
	private String mobileNo;

	/** 用户头像 */
	private String avatar = "";

	/** 角色ID 1 管理员  2 普通角色*/
	private Integer roleId;

	/** 帐号状态（0正常 1停用） */
	private String status;

	/** 删除标志（0代表存在 1代表删除） */
	private Integer delFlag;

	/** 最后登陆IP */
	private String loginIp;

	/** 最后登陆时间 */
	private LocalDateTime lastLoginTime;

	/** 备注/说明 */
	private String remark;

	public boolean isAdmin() {
		return isAdmin(this.roleId);
	}

	public static boolean isAdmin(Integer roleId) {
		return roleId == 1;
	}

}
