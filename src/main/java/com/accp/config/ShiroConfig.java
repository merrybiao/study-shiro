package com.accp.config;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.accp.shiro.realm.UserRealm;
import com.accp.shiro.web.filter.CaptchaValidateFilter;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;

/**
 * 权限配置加载
 * 
 * @author Luyuan
 */
@Configuration
public class ShiroConfig {
	public static final String PREMISSION_STRING = "perms[\"{0}\"]";

	// Session超时时间，单位为毫秒（默认30分钟）
	@Value("${shiro.session.expireTime}")
	private int expireTime;

	// 相隔多久检查一次session的有效性，单位毫秒，默认就是10分钟
	@Value("${shiro.session.validationInterval}")
	private int validationInterval;

	// 验证码开关
	@Value("${shiro.user.captchaEnabled}")
	private boolean captchaEnabled;

	// 验证码类型
	@Value("${shiro.user.captchaType}")
	private String captchaType;

	// 设置Cookie的域名
	@Value("${shiro.cookie.domain}")
	private String domain;

	// 设置cookie的有效访问路径
	@Value("${shiro.cookie.path}")
	private String path;

	// 设置HttpOnly属性
	@Value("${shiro.cookie.httpOnly}")
	private boolean httpOnly;

	// 设置Cookie的过期时间，秒为单位
	@Value("${shiro.cookie.maxAge}")
	private int maxAge;

	// 登录地址
	@Value("${shiro.user.loginUrl}")
	private String loginUrl;

	// 权限认证失败地址
	@Value("${shiro.user.unauthorizedUrl}")
	private String unauthorizedUrl;

	/**
	 * 退出过滤器
	 */
	/*
	 * public LogoutFilter logoutFilter() { LogoutFilter logoutFilter = new
	 * LogoutFilter(); logoutFilter.setLoginUrl(loginUrl); return logoutFilter; }
	 */

	/**
	 * 安全管理器
	 */
	@Bean
	public SecurityManager securityManager(UserRealm userRealm) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 设置realm.
		securityManager.setRealm(userRealm);
		// 记住我
		securityManager.setRememberMeManager(rememberMeManager());
//        // 注入缓存管理器;
//        securityManager.setCacheManager(getEhCacheManager());
//        // session管理器
//        securityManager.setSessionManager(sessionManager());
		return securityManager;
	}

	/**
	 * Shiro过滤器配置
	 */
	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		// Shiro的核心安全接口,这个属性是必须的
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		// 身份认证失败，则跳转到登录页面的配置
		shiroFilterFactoryBean.setLoginUrl(loginUrl);
		// 权限认证失败，则跳转到指定页面
		shiroFilterFactoryBean.setUnauthorizedUrl(unauthorizedUrl);
		// Shiro连接约束配置，即过滤链的定义
		LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
		// 对静态资源设置匿名访问
		filterChainDefinitionMap.put("/favicon.ico**", "anon");
		filterChainDefinitionMap.put("/ruoyi.png**", "anon");
		filterChainDefinitionMap.put("/css/**", "anon");
		filterChainDefinitionMap.put("/docs/**", "anon");
		filterChainDefinitionMap.put("/fonts/**", "anon");
		filterChainDefinitionMap.put("/img/**", "anon");
		filterChainDefinitionMap.put("/ajax/**", "anon");
		filterChainDefinitionMap.put("/js/**", "anon");
		filterChainDefinitionMap.put("/ruoyi/**", "anon");
		filterChainDefinitionMap.put("/druid/**", "anon");
		filterChainDefinitionMap.put("/captcha/captchaImage**", "anon");
		// 退出 logout地址，shiro去清除session
		filterChainDefinitionMap.put("/logout", "logout");
		// 不需要拦截的访问
		filterChainDefinitionMap.put("/login", "anon,captchaValidate");//启用名字为captchaValidate的拦截器。
		filterChainDefinitionMap.put("/register", "anon,captchaValidate");
		filterChainDefinitionMap.put("/register_success", "anon");
		// 系统权限列表
		// filterChainDefinitionMap.putAll(SpringUtils.getBean(MenuService.class).selectPermsAll());

		Map<String, Filter> filters = new LinkedHashMap<>();
		filters.put("captchaValidate", captchaValidateFilter());//将自己 实现的验证码的拦截器是实例加入到shiro的拦截器列表中。
		// 注销成功，则跳转到指定页面
		//filters.put("logout", logoutFilter());
		shiroFilterFactoryBean.setFilters(filters);

		// 所有请求需要认证
		filterChainDefinitionMap.put("/**", "user");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

		return shiroFilterFactoryBean;
	}

	/**
	 * 自定义验证码过滤器
	 */
	@Bean
	public CaptchaValidateFilter captchaValidateFilter() {
		CaptchaValidateFilter captchaValidateFilter = new CaptchaValidateFilter();
		captchaValidateFilter.setCaptchaEnabled(captchaEnabled);
		captchaValidateFilter.setCaptchaType(captchaType);
		return captchaValidateFilter;
	}


	/**
	 * cookie 属性设置
	 */
	public SimpleCookie rememberMeCookie() {
		SimpleCookie cookie = new SimpleCookie("rememberMe");
		cookie.setDomain(domain);
		cookie.setPath(path);
		cookie.setHttpOnly(httpOnly);
		cookie.setMaxAge(maxAge * 24 * 60 * 60);
		return cookie;
	}

	/**
	 * 记住我
	 */
	public CookieRememberMeManager rememberMeManager() {
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(rememberMeCookie());
		cookieRememberMeManager.setCipherKey(Base64.decode("fCq+/xW488hMTCD+cmJ3aQ=="));
		return cookieRememberMeManager;
	}

	/**
	 * thymeleaf模板引擎和shiro框架的整合
	 */
	@Bean
	public ShiroDialect shiroDialect() {
		return new ShiroDialect();
	}

	/**
	 * 开启Shiro注解通知器
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
			@Qualifier("securityManager") SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}
}
