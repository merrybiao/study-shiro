package com.accp.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.accp.common.constant.UserConstants;
import com.accp.common.text.Convert;
import com.accp.common.utils.StringUtils;
import com.accp.entity.SmsUser;
import com.accp.exception.BusinessException;
import com.accp.mapper.SmsUserMapper;
import com.accp.service.SmsUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 用户 业务层处理
 * 
 * @author wubiao
 */
@Service
public class SmsUserServiceImpl extends ServiceImpl<SmsUserMapper, SmsUser> implements SmsUserService {

	/**
	 * 根据条件分页查询用户列表
	 * 
	 * @param user 用户信息
	 * @return 用户信息集合信息
	 */
	@Override
	public List<SmsUser> selectUserList(String loginName, String mobileNo, LocalDateTime beginTime,
			LocalDateTime endTime) {
		QueryWrapper<SmsUser> query = new QueryWrapper<>();
		// 根据登录名模糊匹配，手机号码，创建时间查询用户
		if(StringUtils.isNotEmpty(loginName)) {
			query.lambda().likeRight(SmsUser::getLoginName, loginName);
		}
		if(StringUtils.isNotEmpty(mobileNo)) {
			query.lambda().eq(SmsUser::getMobileNo, mobileNo);			
		}
		if(StringUtils.isNotNull(beginTime) && StringUtils.isNotNull(endTime)) {
			query.lambda().between(SmsUser::getCreateTime, beginTime, endTime);			
		} else if(StringUtils.isNotNull(beginTime) && StringUtils.isNull(endTime)) {
			query.lambda().ge(SmsUser::getCreateTime, beginTime);
		} else if(StringUtils.isNotNull(endTime) && StringUtils.isNull(beginTime)) {
			query.lambda().le(SmsUser::getCreateTime, endTime);
		}
		query.lambda().eq(SmsUser::getDelFlag, 0);
		// 过滤掉被删除的数据
		query.lambda().eq(SmsUser::getDelFlag, 0);

		return this.list(query);
	}

	/**
	 * 通过用户名查询用户
	 * 
	 * @param userName 用户名
	 * @return 用户对象信息
	 */
	@Override
	public SmsUser selectUserByLoginName(String loginName) {
		QueryWrapper<SmsUser> query = new QueryWrapper<>();
		query.lambda().eq(SmsUser::getLoginName, loginName);
		return this.getOne(query);
	}

	/**
	 * 通过手机号码查询用户
	 * 
	 * @param mobileNo 手机号码
	 * @return 用户对象信息
	 */
	@Override
	public List<SmsUser> selectUserByPhoneNumber(String mobileNo) {
		QueryWrapper<SmsUser> query = new QueryWrapper<>();
		query.lambda().eq(SmsUser::getMobileNo, mobileNo);
		return this.list(query);
	}

	/**
	 * 通过邮箱查询用户
	 * 
	 * @param email 邮箱
	 * @return 用户对象信息
	 */
	@Override
	public List<SmsUser> selectUserByEmail(String email) {
		QueryWrapper<SmsUser> query = new QueryWrapper<>();
		query.lambda().eq(SmsUser::getEmail, email);
		return this.list(query);
	}

	/**
	 * 通过用户ID查询用户
	 * 
	 * @param userId 用户ID
	 * @return 用户对象信息
	 */
	@Override
	public SmsUser selectUserById(Long userId) {
		SmsUser user = this.getById(userId);
		if (null == user || user.getDelFlag() == 1) {
			throw new RuntimeException("用户不存在或已删除");
		}
		return user;
	}

	/**
	 * 通过用户ID删除用户
	 * 
	 * @param userId 用户ID
	 * @return 结果
	 */
	@Override
	public boolean deleteUserById(Long userId) {
		SmsUser user = selectUserById(userId);
		if (null == user)
			return false;

		if (user.isAdmin())
			throw new RuntimeException("该用户是管理员，不可以删除~");

		if (user.getDelFlag() == 1)
			throw new RuntimeException("该用户不存在或已删除~");

		user.setDelFlag(1);// 删除
		user.setUpdateTime(LocalDateTime.now());
		return this.updateUser(user);
	}

	/**
	 * 批量删除用户信息
	 * 
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 */
	@Override
	public int deleteUserByIds(String ids) throws BusinessException {
		Long[] userIds = Convert.toLongArray(ids);
		for (Long userId : userIds) {
			deleteUserById(userId);
		}
		return userIds.length;
	}

	/**
	 * 新增保存用户信息
	 * 
	 * @param user 用户信息
	 * @return 结果
	 */
	@Override
	@Transactional
	public boolean insertUser(SmsUser user) {
		user.setRoleId(2);// 角色 普通 
		user.setCreateTime(LocalDateTime.now());
		return save(user);
	}

	/**
	 * 修改保存用户信息
	 * 
	 * @param user 用户信息
	 * @return 结果
	 */
	@Override
	@Transactional
	public boolean updateUser(SmsUser user) {
		user.setUpdateTime(LocalDateTime.now());
		return updateById(user);
	}

	/**
	 * 修改用户个人详细信息
	 * 
	 * @param user 用户信息
	 * @return 结果
	 */
	@Override
	public boolean updateUserInfo(SmsUser user) {
		user.setUpdateTime(LocalDateTime.now());
		return updateById(user);
	}

	/**
	 * 修改用户密码
	 * 
	 * @param user 用户信息
	 * @return 结果
	 */
	@Override
	public boolean resetUserPwd(SmsUser user) {
		user.setUpdateTime(LocalDateTime.now());
		return updateById(user);

	}

	/**
	 * 校验用户名称是否唯一
	 * 
	 * @param loginName 用户名
	 * @return
	 */
	@Override
	public Integer checkLoginNameUnique(String loginName) {
		QueryWrapper<SmsUser> query = new QueryWrapper<>();
		query.lambda().eq(SmsUser::getLoginName, loginName);
		int count = count(query);
		if (count > 0) {
			return UserConstants.USER_NAME_NOT_UNIQUE;
		}
		return UserConstants.USER_NAME_UNIQUE;
	}

	/**
	 * 校验用户名称是否唯一
	 *
	 * @param user 用户信息
	 * @return
	 */
	@Override
	public Integer checkPhoneUnique(SmsUser user) {

		QueryWrapper<SmsUser> query = new QueryWrapper<>();
		query.lambda().eq(SmsUser::getMobileNo, user.getMobileNo());
		int count = count(query);
		if (count > 0) {
			return UserConstants.USER_PHONE_NOT_UNIQUE;
		}
		return UserConstants.USER_PHONE_UNIQUE;
	}

	/**
	 * 校验email是否唯一
	 *
	 * @param user 用户信息
	 * @return
	 */
	@Override
	public Integer checkEmailUnique(SmsUser user) {
		QueryWrapper<SmsUser> query = new QueryWrapper<>();
		query.lambda().eq(SmsUser::getEmail, user.getEmail());
		int count = count(query);
		if (count > 0) {
			return UserConstants.USER_EMAIL_NOT_UNIQUE;
		}
		return UserConstants.USER_EMAIL_UNIQUE;
	}

	/**
	 * 用户状态修改
	 * 
	 * @param user 用户信息
	 * @return 结果
	 */
	@Override
	public boolean changeStatus(SmsUser user) {
		if (SmsUser.isAdmin(user.getRoleId())) {
			throw new BusinessException("不允许修改超级管理员用户");
		}

		UpdateWrapper<SmsUser> update = new UpdateWrapper<>();
		update.lambda().set(SmsUser::getStatus, user.getStatus()).set(SmsUser::getLastLoginTime, LocalDateTime.now())
				.eq(SmsUser::getId, user.getId());

		return update(update);
	}

}
