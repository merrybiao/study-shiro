package com.accp.service.impl;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accp.common.constant.UserConstants;
import com.accp.common.utils.StringUtils;
import com.accp.common.utils.TreeUtils;
import com.accp.dto.SmsMenuDto;
import com.accp.entity.SmsMenu;
import com.accp.entity.SmsRoleMenu;
import com.accp.entity.SmsUser;
import com.accp.freamwork.web.domain.Ztree;
import com.accp.mapper.SmsMenuMapper;
import com.accp.mapper.SmsRoleMenuMapper;
import com.accp.service.SmsMenuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 菜单 业务层处理
 * 
 * @author Luyuan
 */
@Service
public class SmsMenuServiceImpl extends ServiceImpl<SmsMenuMapper, SmsMenu> implements SmsMenuService {

	public static final String PREMISSION_STRING = "perms[\"{0}\"]";

	@Autowired
	private SmsRoleMenuMapper roleMenuMapper;

	/**
	 * 根据用户查询菜单
	 * 
	 * @param user 用户信息
	 * @return 菜单列表
	 */
	@Override
	public List<SmsMenuDto> selectMenusByUser(SmsUser user) {
		List<SmsMenuDto> menus = new LinkedList<SmsMenuDto>();
		// 管理员显示所有菜单信息
		if (user.isAdmin()) {
			menus = selectMenuAll();
		} else {
			menus = selectMenusByRoleId(user.getRoleId());
		}
		return TreeUtils.getChildPerms(menus, 0);
	}

	/**
	 * 查询菜单集合
	 * 
	 * @return 所有菜单信息
	 */
	@SuppressWarnings({ "unchecked", })
	@Override
	public List<SmsMenuDto> selectMenuList(String menuName) {

		QueryWrapper<SmsMenu> query = new QueryWrapper<SmsMenu>();
		
		if(StringUtils.isNotEmpty(menuName)) {
			query.lambda().likeRight(SmsMenu::getMenuName, menuName);
		}
		
		query.lambda().eq(SmsMenu::getVisible, 0)
				.orderByAsc(SmsMenu::getParentId,SmsMenu::getOrderNum);

		return list(query).stream().filter(Objects::nonNull).map(menu -> {
			SmsMenuDto dto = new SmsMenuDto();
			dto.setMenuId(menu.getId());
			dto.setMenuName(menu.getMenuName());
			dto.setMenuType(menu.getMenuType());
			dto.setIcon(menu.getIcon());
			dto.setOrderNum(menu.getOrderNum());
			dto.setParentId(menu.getParentId());
			dto.setPerms(menu.getPerms());
			dto.setUrl(menu.getUrl());
			dto.setVisible(menu.getVisible());

			return dto;
		}).collect(Collectors.toList());
	}

	/**
	 * 查询菜单集合
	 * 
	 * @return 所有菜单信息
	 */
	@Override
	public List<SmsMenuDto> selectMenuAll() {
		QueryWrapper<SmsMenu> query = new QueryWrapper<SmsMenu>();
		query.lambda().in(SmsMenu::getMenuType, Arrays.asList("M", "C")).eq(SmsMenu::getVisible, 0)
				.orderByAsc(SmsMenu::getOrderNum);
		return list(query).stream().filter(Objects::nonNull).map(menu -> {
			SmsMenuDto dto = new SmsMenuDto();
			dto.setMenuId(menu.getId());
			dto.setMenuName(menu.getMenuName());
			dto.setMenuType(menu.getMenuType());
			dto.setIcon(menu.getIcon());
			dto.setOrderNum(menu.getOrderNum());
			dto.setParentId(menu.getParentId());
			dto.setPerms(menu.getPerms());
			dto.setUrl(menu.getUrl());
			dto.setVisible(menu.getVisible());
			return dto;
		}).collect(Collectors.toList());
	}

	/**
	 * 根据用户ID查询权限
	 * 
	 * @param userId 用户ID
	 * @return 权限列表
	 */
	@Override
	public Set<String> selectPermsByRoleId(Integer roleId) {
		QueryWrapper<SmsRoleMenu> query1 = new QueryWrapper<>();
		query1.lambda().eq(SmsRoleMenu::getRoleId, roleId);
		List<Long> menuIds = roleMenuMapper.selectList(query1).stream().filter(Objects::nonNull).map(roleMenu -> {
			return roleMenu.getMenuId();
		}).collect(Collectors.toList());

		if (menuIds == null || menuIds.isEmpty())
			return new HashSet<>();

		QueryWrapper<SmsMenu> query = new QueryWrapper<SmsMenu>();
		query.lambda().in(SmsMenu::getId, menuIds).eq(SmsMenu::getVisible, 0);
		List<String> perms = list(query).stream().filter(Objects::nonNull).map(menu -> {
			return menu.getPerms();
		}).collect(Collectors.toList());
		Set<String> permsSet = new HashSet<>();
		for (String perm : perms) {
			
			if(StringUtils.isEmpty(perm)) continue;
			
			permsSet.addAll(Arrays.asList(perm.trim().split(",")));				
			
			

		}
		return permsSet;
	}

	/**
	 * 根据角色ID查询菜单
	 * 
	 * @param role 角色对象
	 * @return 菜单列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Ztree> roleMenuTreeData(Long roleId) {
		List<Ztree> ztrees = new ArrayList<Ztree>();
		QueryWrapper<SmsMenu> querys = new QueryWrapper<>();
		querys.lambda().orderByAsc(SmsMenu::getParentId,SmsMenu::getOrderNum);
		List<SmsMenu> menuList = list(querys);
		if (StringUtils.isNotNull(roleId)) {
			QueryWrapper<SmsRoleMenu> query = new QueryWrapper<>();
			query.lambda().eq(SmsRoleMenu::getRoleId, roleId);
            List<Long> roleMenuList = roleMenuMapper.selectList(query).stream().map(roleMenu ->{
            	return roleMenu.getMenuId();
            }).collect(Collectors.toList());
            
            ztrees = initZtree(menuList, roleMenuList, true);
		} else {
			ztrees = initZtree(menuList, null, true);
		}
		return ztrees;
	}

	/**
	 * 查询所有菜单
	 * 
	 * @return 菜单列表
	 */
	@Override
	public List<Ztree> menuTreeData() {
		List<SmsMenu> menuList = list();
		List<Ztree> ztrees = initZtree(menuList);
		return ztrees;
	}

	/**
	 * 查询系统所有权限
	 * 
	 * @return 权限列表
	 */
	@Override
	public LinkedHashMap<String, String> selectPermsAll() {
		LinkedHashMap<String, String> section = new LinkedHashMap<>();
		List<SmsMenu> permissions = list();
		if (StringUtils.isNotEmpty(permissions)) {
			for (SmsMenu menu : permissions) {
				section.put(menu.getUrl(), MessageFormat.format(PREMISSION_STRING, menu.getPerms()));
			}
		}
		return section;
	}

	/**
	 * 对象转菜单树
	 * 
	 * @param menuList 菜单列表
	 * @return 树结构列表
	 */
	public List<Ztree> initZtree(List<SmsMenu> menuList) {
		return initZtree(menuList, null, false);
	}

	/**
	 * 对象转菜单树
	 * 
	 * @param menuList     菜单列表
	 * @param roleMenuList 角色已存在菜单列表
	 * @param permsFlag    是否需要显示权限标识
	 * @return 树结构列表
	 */
	public List<Ztree> initZtree(List<SmsMenu> menuList, List<Long> roleMenuList, boolean permsFlag) {
		List<Ztree> ztrees = new ArrayList<Ztree>();
		boolean isCheck = StringUtils.isNotNull(roleMenuList) && !roleMenuList.isEmpty();
		for (SmsMenu menu : menuList) {
			Ztree ztree = new Ztree();
			ztree.setId(menu.getId());
			ztree.setpId(menu.getParentId());
			ztree.setName(transMenuName(menu, roleMenuList, permsFlag));
			ztree.setTitle(menu.getMenuName());
			if (isCheck) {
				ztree.setChecked(roleMenuList.contains(menu.getId().longValue()));
			}
			ztrees.add(ztree);
		}
		return ztrees;
	}

	public String transMenuName(SmsMenu menu, List<Long> roleMenuList, boolean permsFlag) {
		StringBuffer sb = new StringBuffer();
		sb.append(menu.getMenuName());
		if (permsFlag) {
			sb.append("<font color=\"#888\">&nbsp;&nbsp;&nbsp;" + menu.getPerms() + "</font>");
		}
		return sb.toString();
	}

	/**
	 * 删除菜单管理信息
	 * 
	 * @param menuId 菜单ID
	 * @return 结果
	 */
	@Override
	public boolean deleteMenuById(Long menuId) {
		QueryWrapper<SmsRoleMenu> query = new QueryWrapper<>();
		query.lambda().eq(SmsRoleMenu::getMenuId, menuId);
		roleMenuMapper.delete(query);
		
		QueryWrapper<SmsMenu> wrapper = new QueryWrapper<>();
		wrapper.lambda().eq(SmsMenu::getId, menuId).or();
		return remove(wrapper);
	}

	/**
	 * 根据菜单ID查询信息
	 * 
	 * @param menuId 菜单ID
	 * @return 菜单信息
	 */
	@Override
	public SmsMenuDto selectMenuById(Long menuId) {
		SmsMenu menu =  getById(menuId);
		if(null == menu) return new SmsMenuDto();
		SmsMenuDto dto = new SmsMenuDto();
		dto.setMenuId(menu.getId());
		dto.setMenuName(menu.getMenuName());
		dto.setMenuType(menu.getMenuType());
		dto.setIcon(menu.getIcon());
		dto.setOrderNum(menu.getOrderNum());
		dto.setParentId(menu.getParentId());
		dto.setPerms(menu.getPerms());
		dto.setUrl(menu.getUrl());
		dto.setVisible(menu.getVisible());
		dto.setParentName(getMenuName(menu.getParentId()));
		return dto;
	}

	public String getMenuName(Long menuId) {
		SmsMenu menu =  getById(menuId);
		return null == menu ? null : menu.getMenuName();
	}
	
	/**
	 * 查询子菜单数量
	 * 
	 * @param parentId 菜单ID
	 * @return 结果
	 */
	@Override
	public int selectCountMenuByParentId(Long parentId) {
		QueryWrapper<SmsMenu> query = new QueryWrapper<>();

		query.lambda().eq(SmsMenu::getParentId, parentId);
		return this.count(query);
	}

	/**
	 * 校验菜单名称是否唯一
	 * 
	 * @param menu 菜单信息
	 * @return 结果
	 */
	@Override
	public Integer checkMenuNameUnique(SmsMenu menu) {

		Long menuId = StringUtils.isNull(menu.getId()) ? -1L : menu.getId();
		QueryWrapper<SmsMenu> query = new QueryWrapper<>();

		query.lambda().eq(SmsMenu::getMenuName, menu.getMenuName()).eq(SmsMenu::getParentId, menu.getParentId());
		
		SmsMenu info = getOne(query);
		
		if (StringUtils.isNotNull(info) && info.getId().longValue() != menuId.longValue()) {
			return UserConstants.MENU_NAME_NOT_UNIQUE;
		}
		return UserConstants.MENU_NAME_UNIQUE;
	}

	public List<SmsMenuDto> selectMenusByRoleId(Integer roleId) {

		QueryWrapper<SmsRoleMenu> query1 = new QueryWrapper<>();
		query1.lambda().eq(SmsRoleMenu::getRoleId, roleId);
		List<Long> menuIds = roleMenuMapper.selectList(query1).stream().filter(Objects::nonNull).map(roleMenu -> {
			return roleMenu.getMenuId();
		}).collect(Collectors.toList());

		if (menuIds == null || menuIds.isEmpty())
			return new ArrayList<>();

		QueryWrapper<SmsMenu> query = new QueryWrapper<SmsMenu>();
		query.lambda().in(SmsMenu::getId, menuIds).in(SmsMenu::getMenuType, Arrays.asList("M", "C"))
				.eq(SmsMenu::getVisible, 0).orderByAsc(SmsMenu::getOrderNum);
		return list(query).stream().filter(Objects::nonNull).map(menu -> {
			SmsMenuDto dto = new SmsMenuDto();
			dto.setMenuId(menu.getId());
			dto.setMenuName(menu.getMenuName());
			dto.setMenuType(menu.getMenuType());
			dto.setIcon(menu.getIcon());
			dto.setOrderNum(menu.getOrderNum());
			dto.setParentId(menu.getParentId());
			dto.setPerms(menu.getPerms());
			dto.setUrl(menu.getUrl());
			dto.setVisible(menu.getVisible());
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public int insertMenu(SmsMenu menu) {
		menu.setCreateTime(LocalDateTime.now());
		return save(menu) ? 1 : 0;
	}

	@Override
	public int updateMenu(SmsMenu menu) {
		menu.setUpdateTime(LocalDateTime.now());
		return updateById(menu) ? 1 : 0;
	}

	@Override
	public int updateRoleMenu(Long roleId, List<Long> menuIds) {
		
		
		QueryWrapper<SmsRoleMenu> query = new QueryWrapper<>();
		query.lambda().eq(SmsRoleMenu::getRoleId, roleId);
		List<Long> list = this.roleMenuMapper.selectList(query).stream().filter(Objects::nonNull).map(roleMenu ->{
			return roleMenu.getMenuId();
		}).collect(Collectors.toList());
		
		
		
		List<Long> addMenuIds = new ArrayList<>(menuIds);
		List<Long> delList = new ArrayList<>(list);
		
		boolean flag = addMenuIds.removeAll(list);//求差集，然后添加到表
		if(flag && !addMenuIds.isEmpty()) {
			for(Long menuId : addMenuIds) {
				SmsRoleMenu roleMenu = new SmsRoleMenu();
				roleMenu.setRoleId(roleId.intValue());
				roleMenu.setMenuId(menuId);
				roleMenuMapper.insert(roleMenu);
			}
		}
		
		flag = delList.removeAll(menuIds);//求差集，然后删除表中数据
		
		if(flag && !delList.isEmpty()) {
			QueryWrapper<SmsRoleMenu> wrapper = new QueryWrapper<>();
			wrapper.lambda().eq(SmsRoleMenu::getRoleId, roleId).in(SmsRoleMenu::getMenuId, delList);
			roleMenuMapper.delete(wrapper);
		}
		return flag ? 1 : 0;
	}

}
