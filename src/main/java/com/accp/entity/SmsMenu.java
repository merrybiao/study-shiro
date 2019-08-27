package com.accp.entity;

import com.accp.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 菜单表 sms_menu
 * 
 * @author Luyuan
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bw_sms_menu")
public class SmsMenu extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/** 菜单名称 */
	private String menuName;

	/** 父菜单ID */
	private Long parentId;

	/** 显示顺序 */
	private Integer orderNum;

	/** 菜单URL */
	private String url;

	private String target;

	/** 类型:0目录,1菜单,2按钮 */
	private String menuType;

	/** 菜单状态:0显示,1隐藏 */
	private Integer visible;

	/** 权限字符串 */
	private String perms;

	/** 菜单图标 */
	private String icon;

	private String remark;

}
