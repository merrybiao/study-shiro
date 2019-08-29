package com.accp;

import java.io.File;

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
	
	@Test
	public void contextLoads2() {
	
	}
	
	public static void main(String[] args) {
		File file = new File("/E:/sts_workspace/study_shiro/src/main/resources/templates/test.html");
		boolean exists = file.exists();
		System.out.println(exists);
	
	}

}
