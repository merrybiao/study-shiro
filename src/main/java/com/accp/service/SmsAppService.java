package com.accp.service;

import java.util.List;

import com.accp.entity.SmsApp;
import com.accp.entity.SmsUser;
import com.baomidou.mybatisplus.extension.service.IService;

public interface SmsAppService extends IService<SmsApp> {

	List<SmsApp> selectSmsAppByUser(SmsApp app,SmsUser user);

	String getAppId();

	Integer checkAppNameUnique(SmsApp app);

}
