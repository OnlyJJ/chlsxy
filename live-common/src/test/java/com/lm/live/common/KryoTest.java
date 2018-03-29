package com.lm.live.common;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lm.live.common.redis.RedisUtil;
import com.lm.live.common.utils.KryoUtil;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:spring/spring-context.xml"})
public class KryoTest {
	
	@Test
	public void test1() {
		try {
			String userId = "153706";
			RedisUtil.set("test", userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws ClassNotFoundException {
		UserInfo user = new UserInfo();
		user.setId(123847575);
		user.setUserId("12345677 12345677 12345677 12345677 12345677 12345677 12345677 12345677");
		user.setName("12345677 12345677 12345677 12345677 12345677 12345677 12345677 12345677");
		user.setCarid(123847575);
		user.setAge(123847575);
		
		String key = "user";
//		RedisUtil.set(key, user);
//		
//		RedisUtil.getObj(key);
		String seriz = KryoUtil.serializeToString(user);
		System.err.println("seriz=" + seriz);
		
		
	}
	
	
}
