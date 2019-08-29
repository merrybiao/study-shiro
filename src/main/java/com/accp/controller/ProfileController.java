package com.accp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.accp.base.BaseController;
import com.accp.entity.SmsUser;
import com.accp.freamwork.web.domain.AjaxResult;
import com.accp.service.SmsUserService;

/**
 * 个人信息 业务处理
 * 
 * @author Luyuan
 */
@Controller
@RequestMapping("/system/user/profile")
public class ProfileController extends BaseController {

	private String prefix = "system/user/profile";

	@Autowired
	private SmsUserService userService;

	/**
	 * 个人信息
	 */
	@GetMapping()
	public String profile(ModelMap mmap) {
		SmsUser user = getSysUser();
		mmap.put("user", user);
		mmap.put("roleGroup", null);
		mmap.put("postGroup", null);
		return prefix + "/profile";
	}

	@GetMapping("/checkPassword")
	@ResponseBody
	public boolean checkPassword(String password) {
		SmsUser user = getSysUser();
		if (password.equals(user.getPassword())) {
			return true;
		}
		return false;
	}

	@GetMapping("/resetPwd")
	public String resetPwd(ModelMap mmap) {
		SmsUser user = getSysUser();
		mmap.put("user", userService.selectUserById(user.getId()));
		return prefix + "/resetPwd";
	}

	// @Log(title = "重置密码", businessType = BusinessType.UPDATE)
	@PostMapping("/resetPwd")
	@ResponseBody
	public AjaxResult resetPwd(SmsUser vo) {
		SmsUser user = getSysUser();
		user.setPassword(vo.getPassword());
		if (userService.resetUserPwd(user)) {
			setSysUser(userService.selectUserById(user.getId()));
			return success();
		}
		return error();
		

	}

	/**
	 * 修改用户
	 */
	@GetMapping("/edit")
	public String edit(ModelMap mmap) {
		SmsUser user = getSysUser();
		mmap.put("user", userService.selectUserById(user.getId()));
		return prefix + "/edit";
	}

	/**
	 * 修改头像
	 */
	@GetMapping("/avatar")
	public String avatar(ModelMap mmap) {
		SmsUser user = getSysUser();
		mmap.put("user", userService.selectUserById(user.getId()));
		return prefix + "/avatar";
	}

	/**
	 * 修改用户
	 */
	// @Log(title = "个人信息", businessType = BusinessType.UPDATE)
	@PostMapping("/update")
	@ResponseBody
	public AjaxResult update(SmsUser user) {
		SmsUser currentUser = getSysUser();
		currentUser.setUserName(user.getUserName());
		currentUser.setEmail(user.getEmail());
		currentUser.setMobileNo(user.getMobileNo());
		currentUser.setSex(user.getSex());
		if (userService.updateUserInfo(currentUser)) {
			setSysUser(userService.selectUserById(currentUser.getId()));
			return success();
		}
		return error();
	}
//
//    /**
//     * 保存头像
//     */
//    @PostMapping("/updateAvatar")
//    @ResponseBody
//    public AjaxResult updateAvatar(@RequestParam("avatarfile") MultipartFile file)
//    {
//        SmsUser currentUser = getSysUser();
//        try
//        {
//            if (!file.isEmpty())
//            {
//                String avatar = FileUploadUtils.upload(RuoyiConfig.getAvatarPath(), file);
//                currentUser.setAvatar(avatar);
//                if (userService.updateUserInfo(currentUser) > 0)
//                {
//                    setSysUser(userService.selectUserById(currentUser.getId()));
//                    return success();
//                }
//            }
//            return error();
//        }
//        catch (Exception e)
//        {
//            log.error("修改头像失败！", e);
//            return error(e.getMessage());
//        }
//    }
}
