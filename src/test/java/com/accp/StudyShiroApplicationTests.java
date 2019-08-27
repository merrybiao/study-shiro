package com.accp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.accp.entity.SmsUser;
import com.accp.service.SmsUserService;
import com.alibaba.fastjson.JSON;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StudyShiroApplicationTests {
	
	@Autowired
	SmsUserService sms;

	@Test
	public void contextLoads() {
		SmsUser selectUserById = sms.selectUserById(1L);
		System.out.println(JSON.toJSONString(selectUserById));
	}

}
