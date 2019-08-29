package com.accp.controller;

import java.time.LocalDateTime;
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
import com.accp.entity.SmsApp;
import com.accp.entity.SmsUser;
import com.accp.freamwork.web.domain.AjaxResult;
import com.accp.freamwork.web.page.TableDataInfo;
import com.accp.service.SmsAppService;

/**
 * 短信应用管理
 * 
 * @author Luyuan
 *
 */
@Controller
@RequestMapping("system/app")
public class SmsAppController extends BaseController {

	private String prefix = "system/app";

	@Autowired
	private SmsAppService smsAppService;
	
	@RequiresPermissions("system:app:view")
	@GetMapping()
	public String app() {

		return prefix + "/app";
	}
	
	@RequiresPermissions("system:app:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo smsAppList(SmsApp app) {
		SmsUser user = getSysUser();
		
		List<SmsApp> list = smsAppService.selectSmsAppByUser(app,user);

		return getDataTable(list);
	}
	
	
	@GetMapping("/add")
	public String add(ModelMap mmap) {
		SmsUser user = getSysUser();
		mmap.put("userId", user.getId());
		mmap.put("appId", smsAppService.getAppId());
		return prefix + "/add";
	}
	
	@RequiresPermissions("system:app:add")
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(SmsApp app) {
		SmsUser user = getSysUser();
		app.setUserId(user.getId());
		app.setCreateTime(LocalDateTime.now());
		return toAjax(smsAppService.save(app));
	}
	
	@PostMapping("/checkAppNameUnique")
	@ResponseBody
	public Integer checkAppNameUnique(SmsApp app) {
		
		return smsAppService.checkAppNameUnique(app);
	}
	
	@RequiresPermissions("system:app:edit")
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap) {
		mmap.put("smsApp", smsAppService.getById(id));
		return prefix + "/edit";
	}
	
	@RequiresPermissions("system:app:edit")
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(SmsApp app) {
		SmsUser user = getSysUser();
		app.setUserId(user.getId());
		app.setUpdateTime(LocalDateTime.now());
		return toAjax(smsAppService.updateById(app));
	}
	
	/**
	 * 删除菜单
	 */
	@RequiresPermissions("system:app:remove")
	@PostMapping("/remove/{id}")
	@ResponseBody
	public AjaxResult remove(@PathVariable("id") Long id) {
		return toAjax(smsAppService.removeById(id));
	}
	
}
