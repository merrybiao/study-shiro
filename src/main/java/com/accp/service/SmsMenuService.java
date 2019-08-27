package com.accp.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.accp.dto.SmsMenuDto;
import com.accp.entity.SmsMenu;
import com.accp.entity.SmsUser;
import com.accp.freamwork.web.domain.Ztree;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 菜单 业务层
 * 
 * @author Luyuan
 */
public interface SmsMenuService extends IService<SmsMenu> {
	/**
	 * 根据用户ID查询菜单
	 * 
	 * @param user 用户信息
	 * @return 菜单列表
	 */
	List<SmsMenuDto> selectMenusByUser(SmsUser user);

	/**
	 * 查询系统菜单列表
	 * 
	 * @param menu 菜单信息
	 * @return 菜单列表
	 */
	List<SmsMenuDto> selectMenuList(String menuName);

	/**
	 * 查询菜单集合
	 * 
	 * @return 所有菜单信息
	 */
	List<SmsMenuDto> selectMenuAll();

	/**
	 * 根据用户ID查询权限
	 * 
	 * @param userId 用户ID
	 * @return 权限列表
	 */
	Set<String> selectPermsByRoleId(Integer roleId);

	/**
	 * 根据角色ID查询菜单
	 * 
	 * @param role 角色对象
	 * @return 菜单列表
	 */
	List<Ztree> roleMenuTreeData(Long roleId);

	/**
	 * 查询所有菜单信息
	 * 
	 * @return 菜单列表
	 */
	List<Ztree> menuTreeData();

	/**
	 * 查询系统所有权限
	 * 
	 * @return 权限列表
	 */
	Map<String, String> selectPermsAll();

	/**
	 * 删除菜单管理信息
	 * 
	 * @param menuId 菜单ID
	 * @return 结果
	 */
	boolean deleteMenuById(Long menuId);

	/**
	 * 根据菜单ID查询信息
	 * 
	 * @param menuId 菜单ID
	 * @return 菜单信息
	 */
	SmsMenuDto selectMenuById(Long menuId);

	/**
	 * 查询菜单数量
	 * 
	 * @param parentId 菜单父ID
	 * @return 结果
	 */
	int selectCountMenuByParentId(Long parentId);

	/**
	 * 校验菜单名称是否唯一
	 * 
	 * @param menu 菜单信息
	 * @return 结果
	 */
	Integer checkMenuNameUnique(SmsMenu menu);

	int insertMenu(SmsMenu menu);

	int updateMenu(SmsMenu menu);

	int updateRoleMenu(Long roleId, List<Long> menuIds);
}
