package com.accp.shiro.realm;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.accp.common.utils.ShiroUtils;
import com.accp.entity.SmsUser;
import com.accp.enums.RoleType;
import com.accp.service.SmsMenuService;
import com.accp.shiro.service.LoginService;

/**
 * 自定义Realm 处理登录 权限
 * 
 * @author Luyuan
 */
@Component
public class UserRealm extends AuthorizingRealm {
	private static final Logger log = LoggerFactory.getLogger(UserRealm.class);

	@Autowired
	private LoginService loginService;

	@Autowired
	private SmsMenuService menuService;

	/**
	 * 返回主体的授权信息
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		SmsUser user = ShiroUtils.getSysUser();
		// 角色列表
		Set<String> roles = new HashSet<>();
		// 功能列表
		Set<String> menus = new HashSet<String>();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		// 管理员拥有所有权限
		if (user.isAdmin()) {
			info.addRole(RoleType.ADMIN.getRoleType());
			info.addStringPermission("*:*:*");
		} else {
			roles.add(RoleType.USER.getRoleType());
			menus = menuService.selectPermsByRoleId(user.getRoleId());
			// 角色加入AuthorizationInfo认证对象
			info.setRoles(roles);
			// 权限加入AuthorizationInfo认证对象
			info.setStringPermissions(menus);
		}
		return info;
	}

	/**
	 * 身份认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		String username = upToken.getUsername();
		String password = "";
		if (upToken.getPassword() != null) {
			password = new String(upToken.getPassword());
		}

		SmsUser user = null;
		try {
			user = loginService.login(username, password);
		} catch (Exception e) {
			log.info("对用户[" + username + "]进行登录验证..验证未通过{}", e.getMessage());
			throw new AuthenticationException(e.getMessage(), e);
		}
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password, getName());
		return info;
	}

	/**
	 * 清理缓存权限
	 */
	public void clearCachedAuthorizationInfo() {
		this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
	}
	
	public static void main(String[] args) {
		String replace = UUID.randomUUID().toString().replace("-", "");
		System.out.println(replace);
	}
}
