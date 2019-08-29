package com.accp.shiro.web.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.AccessControlFilter;

import com.accp.common.constant.ShiroConstants;
import com.accp.common.utils.ShiroUtils;
import com.accp.common.utils.StringUtils;
import com.google.code.kaptcha.Constants;

/**
 * 验证码过滤器
 * 
 * @author wubiao
 */
public class CaptchaValidateFilter extends AccessControlFilter {
	/**
	 * 是否开启验证码
	 */
	private boolean captchaEnabled = true;

	/**
	 * 验证码类型
	 */
	private String captchaType = "char";

	public void setCaptchaEnabled(boolean captchaEnabled) {
		this.captchaEnabled = captchaEnabled;
	}

	public void setCaptchaType(String captchaType) {
		this.captchaType = captchaType;
	}

	@Override
	public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		request.setAttribute(ShiroConstants.CURRENT_ENABLED, captchaEnabled);
		request.setAttribute(ShiroConstants.CURRENT_TYPE, captchaType);
		return super.onPreHandle(request, response, mappedValue);
	}

	/**
     * 表示是否允许访问 ，如果允许访问返回true，否则false；
     * @param servletRequest
     * @param servletResponse
     * @param mappedValue 表示写在拦截器中括号里面的字符串 mappedValue 就是 [urls] 配置中拦截器参数部分
     * @return
     * @throws Exception
     */
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		// 验证码禁用 或不是表单提交 允许访问
		  if (captchaEnabled == false || !"post".equals(httpServletRequest.getMethod().toLowerCase())) { 
			  return true;
		  }
		
		//验证页面输入的验证码是否与session存储的验证码一致。
		return validateResponse(httpServletRequest,httpServletRequest.getParameter(ShiroConstants.CURRENT_VALIDATECODE));
	}

	public boolean validateResponse(HttpServletRequest request, String validateCode) {
		Object obj = ShiroUtils.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
		String code = String.valueOf(obj != null ? obj : "");
		if (StringUtils.isEmpty(validateCode) || !validateCode.equalsIgnoreCase(code)) {
			return false;
		}
		return true;
	}

	
	 /**
     * onAccessDenied：表示当访问拒绝时是否已经处理了；如果返回 true 表示需要继续处理；如果返回 false 表示该拦截器实例已经处理了，将直接返回即可。

     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		request.setAttribute(ShiroConstants.CURRENT_CAPTCHA, ShiroConstants.CAPTCHA_ERROR);
		return true;
	}
}
