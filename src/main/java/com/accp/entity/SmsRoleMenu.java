package com.accp.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("bw_sms_role_menu")
public class SmsRoleMenu implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	/** 角色ID **/
	private Integer roleId;
	/** 菜单ID **/
	private Long menuId;
	/** 创建时间 **/
	private LocalDateTime createTime;

}
