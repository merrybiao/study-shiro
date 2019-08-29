package com.accp.config;

import javax.servlet.ServletContext;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.ServletContextResource;

public class ServletContextResourceConfig extends ServletContextResource{

	public ServletContextResourceConfig(ServletContext servletContext, String path) {
		super(servletContext, path);
	}


}
