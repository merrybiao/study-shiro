package com.accp.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.accp.base.BaseController;
import com.accp.dto.SmsMenuDto;
import com.accp.entity.SmsMenu;
import com.accp.freamwork.web.domain.AjaxResult;
import com.accp.freamwork.web.domain.Ztree;
import com.accp.service.SmsMenuService;

/**
 * 菜单信息
 * 
 * @author Luyuan
 */
@Controller
@RequestMapping("/system/menu")
public class SmsMenuController extends BaseController {
	private String prefix = "system/menu";

	@Autowired
	private SmsMenuService menuService;

	@RequiresPermissions("system:menu:view")
	@GetMapping()
	public String menu() {
		return prefix + "/menu";
	}

	@RequiresPermissions("system:menu:list")
	@GetMapping("/list")
	@ResponseBody
	public List<SmsMenuDto> list(String menuName) {
		List<SmsMenuDto> menuList = menuService.selectMenuList(menuName);
		return menuList;
	}

	/**
	 * 删除菜单
	 */
	@RequiresPermissions("system:menu:remove")
	@GetMapping("/remove/{menuId}")
	@ResponseBody
	public AjaxResult remove(@PathVariable("menuId") Long menuId) {
		if (menuService.selectCountMenuByParentId(menuId) > 1) {
			return AjaxResult.warn("存在子菜单,不允许删除");
		}
		return toAjax(menuService.deleteMenuById(menuId));
	}

	/**
	 * 新增
	 */
	@GetMapping("/add/{parentId}")
	public String add(@PathVariable("parentId") Long parentId, ModelMap mmap) {
		SmsMenuDto menu = null;
		if (0L != parentId) {
			menu = menuService.selectMenuById(parentId);
		} else {
			menu = new SmsMenuDto();
			menu.setMenuId(0L);
			menu.setMenuName("主目录");
		}
		mmap.put("menu", menu);
		return prefix + "/add";
	}
	
    /**
     * 新增保存菜单
     */
    @RequiresPermissions("system:menu:add")
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SmsMenu menu)
    {
        return toAjax(menuService.insertMenu(menu));
    }
	
	/**
	 * 修改菜单
	 */
	@GetMapping("/edit/{menuId}")
	public String edit(@PathVariable("menuId") Long menuId, ModelMap mmap) {
		mmap.put("menu", menuService.selectMenuById(menuId));
		return prefix + "/edit";
	}
	
    /**
     * 修改保存菜单
     */
    @RequiresPermissions("system:menu:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SmsMenu menu)
    {
        return toAjax(menuService.updateMenu(menu));
    }

	/**
	 * 选择菜单图标
	 */
	@GetMapping("/icon")
	public String icon() {
		return prefix + "/icon";
	}

	/**
	 * 校验菜单名称
	 */
	@PostMapping("/checkMenuNameUnique")
	@ResponseBody
	public Integer checkMenuNameUnique(SmsMenu menu) {
		return menuService.checkMenuNameUnique(menu);
	}

	/**
	 * 加载角色菜单列表树
	 */
	@GetMapping("/roleMenuTreeData")
	@ResponseBody
	public List<Ztree> roleMenuTreeData(Long roleId) {
		List<Ztree> ztrees = menuService.roleMenuTreeData(roleId);
		return ztrees;
	}

	/**
	 * 加载所有菜单列表树
	 */
	@GetMapping("/menuTreeData")
	@ResponseBody
	public List<Ztree> menuTreeData() {
		List<Ztree> ztrees = menuService.menuTreeData();
		return ztrees;
	}

	/**
	 * 选择菜单树
	 */
	@GetMapping("/selectMenuTree/{menuId}")
	public String selectMenuTree(@PathVariable("menuId") Long menuId, ModelMap mmap) {
		mmap.put("menu", menuService.selectMenuById(menuId));
		return prefix + "/tree";
	}
}