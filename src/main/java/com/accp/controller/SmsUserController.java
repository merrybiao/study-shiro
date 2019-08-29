package com.accp.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
import com.accp.common.utils.StringUtils;
import com.accp.entity.SmsUser;
import com.accp.freamwork.web.domain.AjaxResult;
import com.accp.freamwork.web.page.TableDataInfo;
import com.accp.service.SmsUserService;

/**
 * 用户信息
 * 
 * @author Luyuan
 */
@Controller
@RequestMapping("/system/user")
public class SmsUserController extends BaseController {
	private String prefix = "system/user";

	@Autowired
	private SmsUserService userService;

	@RequiresPermissions("system:user:view")
	@GetMapping()
	public String user() {
		return prefix + "/user";
	}

	@RequiresPermissions("system:user:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(String loginName, String mobileNo, String beginTime,
			String endTime) {
		startPage();
		LocalDateTime beginDate = null;
		if(StringUtils.isNotEmpty(beginTime)) {
			beginDate = LocalDateTime.of(LocalDate.parse(beginTime, DateTimeFormatter.ofPattern("yyyy-MM-dd")), LocalTime.MIN);
		}
		LocalDateTime endDate = null;
		if(StringUtils.isNotEmpty(endTime)) {
			endDate = LocalDateTime.of(LocalDate.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd")),LocalTime.MIN);
		}
		List<SmsUser> list = userService.selectUserList(loginName,mobileNo,beginDate,endDate);
		return getDataTable(list);
	}

	/**
	 * 新增用户
	 */
	@GetMapping("/add")
	public String add(ModelMap mmap) {
		mmap.put("roles", null);
		mmap.put("posts", null);
		return prefix + "/add";
	}

	/**
	 * 新增保存用户
	 */
	@RequiresPermissions("system:user:add")
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(SmsUser user) {
		if (StringUtils.isNotNull(user.getId()) && SmsUser.isAdmin(user.getRoleId())) {
			return error("不允许修改超级管理员用户");
		}
		return toAjax(userService.insertUser(user));
	}

	/**
	 * 修改用户
	 */
	@GetMapping("/edit/{userId}")
	public String edit(@PathVariable("userId") Long userId, ModelMap mmap) {
		mmap.put("user", userService.selectUserById(userId));
		return prefix + "/edit";
	}

	/**
	 * 修改保存用户
	 */
	@RequiresPermissions("system:user:edit")
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(SmsUser vo) {
		if (null == vo.getId()) {
			return error("提交用户数据缺失[id]");
		}
		SmsUser user = null;
		try {
			user = userService.getById(vo.getId());
		} catch (RuntimeException e) {
			return error(e.getMessage());
		}

		user.setUserName(vo.getUserName());
		user.setMobileNo(vo.getMobileNo());
		user.setStatus(vo.getStatus());
		user.setSex(vo.getSex());
		user.setEmail(vo.getEmail());
		user.setRemark(vo.getRemark());
		return toAjax(userService.updateUser(user));
	}

	@RequiresPermissions("system:user:resetPwd")
	@GetMapping("/resetPwd/{userId}")
	public String resetPwd(@PathVariable("userId") Long userId, ModelMap mmap) {
		mmap.put("initPassword", "123456");
		mmap.put("user", userService.selectUserById(userId));
		return prefix + "/resetPwd";
	}

	@RequiresPermissions("system:user:resetPwd")
	@PostMapping("/resetPwd")
	@ResponseBody
	public AjaxResult resetPwd(SmsUser vo) {

		if (null == vo.getId()) {
			return error("提交用户数据缺失[id]");
		}
		SmsUser user = null;
		try {
			user = userService.getById(vo.getId());
		} catch (RuntimeException e) {
			return error(e.getMessage());
		}
		user.setPassword(vo.getPassword());
		return toAjax(userService.resetUserPwd(user));
	}

	@RequiresPermissions("system:user:remove")
	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(String ids) {

		try {
			return toAjax(userService.deleteUserByIds(ids));
		} catch (Exception e) {
			return error(e.getMessage());
		}
	}

	/**
	 * 校验用户名
	 */
	@PostMapping("/checkLoginNameUnique")
	@ResponseBody
	public Integer checkLoginNameUnique(SmsUser user) {
		return userService.checkLoginNameUnique(user.getLoginName());
	}

	/**
	 * 校验手机号码
	 */
	@PostMapping("/checkPhoneUnique")
	@ResponseBody
	public Integer checkPhoneUnique(SmsUser user) {
		return userService.checkPhoneUnique(user);
	}

	/**
	 * 校验email邮箱
	 */
	@PostMapping("/checkEmailUnique")
	@ResponseBody
	public Integer checkEmailUnique(SmsUser user) {
		return userService.checkEmailUnique(user);
	}

	/**
	 * 用户状态修改
	 */
	@RequiresPermissions("system:user:edit")
	@PostMapping("/changeStatus")
	@ResponseBody
	public AjaxResult changeStatus(SmsUser user) {
		return toAjax(userService.changeStatus(user));
	}
}