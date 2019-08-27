package com.accp.dto;

import java.util.ArrayList;
import java.util.List;

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
public class SmsMenuDto {

	/** 菜单ID **/
	private Long menuId;

	/** 菜单名称 */
	private String menuName;

	/** 父菜单名称 */
	private String parentName;

	/** 父菜单ID */
	private Long parentId;

	/** 显示顺序 */
	private Integer orderNum;

	/** 菜单URL */
	private String url;

	/** 类型:0目录,1菜单,2按钮 */
	private String menuType;

	/** 菜单状态:0显示,1隐藏 */
	private Integer visible;

	/** 权限字符串 */
	private String perms;

	/** 菜单图标 */
	private String icon;

	/** 子菜单 */
	private List<SmsMenuDto> children = new ArrayList<SmsMenuDto>();

}
