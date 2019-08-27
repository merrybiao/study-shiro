package com.accp.service;

import java.time.LocalDateTime;
import java.util.List;

import com.accp.entity.SmsUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户 业务层
 * 
 * @author Luyuan
 */
public interface SmsUserService extends IService<SmsUser> {
	/**
	 * 根据条件分页查询用户列表
	 * 
	 * @param user 用户信息
	 * @return 用户信息集合信息
	 */
	public List<SmsUser> selectUserList(String loginName, String mobileNo, LocalDateTime beginTime,
			LocalDateTime endTime);

	/**
	 * 通过用户名查询用户
	 * 
	 * @param userName 用户名
	 * @return 用户对象信息
	 */
	public SmsUser selectUserByLoginName(String userName);

	/**
	 * 通过手机号码查询用户
	 * 
	 * @param phoneNumber 手机号码
	 * @return 用户对象信息
	 */
	public List<SmsUser> selectUserByPhoneNumber(String phoneNumber);

	/**
	 * 通过邮箱查询用户
	 * 
	 * @param email 邮箱
	 * @return 用户对象信息
	 */
	public List<SmsUser> selectUserByEmail(String email);

	/**
	 * 通过用户ID查询用户
	 * 
	 * @param userId 用户ID
	 * @return 用户对象信息
	 */
	public SmsUser selectUserById(Long userId);

	/**
	 * 通过用户ID删除用户
	 * 
	 * @param userId 用户ID
	 * @return 结果
	 */
	public boolean deleteUserById(Long userId);

	/**
	 * 批量删除用户信息
	 * 
	 * @param ids 需要删除的数据ID
	 * @return 结果
	 * @throws Exception 异常
	 */
	public int deleteUserByIds(String ids) throws Exception;

	/**
	 * 保存用户信息
	 * 
	 * @param user 用户信息
	 * @return 结果
	 */
	public boolean insertUser(SmsUser user);

	/**
	 * 保存用户信息
	 * 
	 * @param user 用户信息
	 * @return 结果
	 */
	public boolean updateUser(SmsUser user);

	/**
	 * 修改用户详细信息
	 * 
	 * @param user 用户信息
	 * @return 结果
	 */
	public boolean updateUserInfo(SmsUser user);

	/**
	 * 修改用户密码信息
	 * 
	 * @param user 用户信息
	 * @return 结果
	 */
	public boolean resetUserPwd(SmsUser user);

	/**
	 * 校验用户名称是否唯一
	 * 
	 * @param loginName 登录名称
	 * @return 结果
	 */
	public Integer checkLoginNameUnique(String loginName);

	/**
	 * 校验手机号码是否唯一
	 *
	 * @param user 用户信息
	 * @return 结果
	 */
	public Integer checkPhoneUnique(SmsUser user);

	/**
	 * 校验email是否唯一
	 *
	 * @param user 用户信息
	 * @return 结果
	 */
	Integer checkEmailUnique(SmsUser user);

	/**
	 * 用户状态修改
	 * 
	 * @param user 用户信息
	 * @return 结果
	 */
	public boolean changeStatus(SmsUser user);
}
