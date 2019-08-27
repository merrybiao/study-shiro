package com.accp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accp.common.constant.UserConstants;
import com.accp.common.utils.StringUtils;
import com.accp.entity.SmsApp;
import com.accp.entity.SmsUser;
import com.accp.jedis.JedisClient;
import com.accp.mapper.SmsAppMapper;
import com.accp.service.SmsAppService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class SmsAppServiceImpl extends ServiceImpl<SmsAppMapper, SmsApp> implements SmsAppService {

	@Autowired
	private JedisClient client;

	@Override
	public List<SmsApp> selectSmsAppByUser(SmsApp app,SmsUser user) {

			QueryWrapper<SmsApp> query = new QueryWrapper<>();
			if(StringUtils.isNotEmpty(app.getAppName())) {
				query.lambda().likeRight(SmsApp::getAppName, app.getAppName());				
			}
			
			if(StringUtils.isNotEmpty(app.getMobileNo())) {
				query.lambda().likeRight(SmsApp::getMobileNo, app.getMobileNo());				
			}
			
			if(!user.isAdmin()) {
				query.lambda().eq(SmsApp::getUserId, user.getId());
			}
			return list(query);
		
	}

	
	@Override
	public String getAppId() {
		return "SMS" + client.incr("SMS:APP:ID");
	}

	@Override
	public Integer checkAppNameUnique(SmsApp app) {
		
		Long id = StringUtils.isNull(app.getId()) ? -1L : app.getId();
		
		QueryWrapper<SmsApp> query = new QueryWrapper<>();
		query.lambda().eq(SmsApp::getAppName, app.getAppName());
		SmsApp info = getOne(query);
		
		if(StringUtils.isNotNull(info) && info.getId().longValue() != id.longValue()) {
			return UserConstants.APPNAME_NAME_NOT_UNIQUE;
		}
		
		return UserConstants.APPNAME_NAME_UNIQUE;
	}
	
	

}
