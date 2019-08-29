package com.accp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.accp.base.BaseController;
import com.accp.dto.SmsMenuDto;
import com.accp.entity.SmsUser;
import com.accp.service.SmsMenuService;

/**
 * 首页 业务处理
 * 
 * @author Luyuan
 */
@Controller
public class IndexController extends BaseController {
	@Autowired
	private SmsMenuService menuService;

	// 系统首页
	@GetMapping("/index")
	public String index(ModelMap mmap) {
		// 取身份信息
		SmsUser user = getSysUser();
		
		if(null == user) {
			return "redirect:login";
		}
		
		// 根据用户id取出菜单
		List<SmsMenuDto> menus = menuService.selectMenusByUser(user);
		mmap.put("menus", menus);
		mmap.put("user", user);
		mmap.put("copyrightYear", "2020");
		return "index";
	}

	// 系统介绍
	@GetMapping("/system/main")
	public String main(ModelMap mmap) {
		mmap.put("version", "sms1.0");
		return "main";
	}
}
