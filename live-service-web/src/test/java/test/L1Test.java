package test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.redis.RedisUtil;
import com.lm.live.common.utils.HttpUtils;
import com.lm.live.common.utils.Md5CommonUtils;
import com.lm.live.login.dao.UuidBlackListMapper;
import com.lm.live.userbase.domain.UserAnchor;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:spring/spring-context.xml"})
public class L1Test {
	@Resource
	private UuidBlackListMapper uuidBlackListMapper;
	
	@Test
	public void test() {
		 String url=  "";
		 url=  "http://127.0.0.1:8088/service_8616/L1/0/";
		 JSONObject  json = new JSONObject();
		 JSONObject  code = new JSONObject();
		 JSONObject  deviceproperties = new JSONObject();
		 code.put("c", "293509968026258edc05db6c4596ebe9");
		 deviceproperties.put("a", "M5 Note");
		 deviceproperties.put("b", "F18A7B3C-7756-4E4B-B072-C6F3ABC0F034"); 
		 deviceproperties.put("i", 1);
		 json.put("code", code);
		 json.put("deviceproperties", deviceproperties);
		  
		  System.out.println("#####str:"+json.toString());
		 try {
			String strRes = HttpUtils.post(url, json.toString());
			System.out.println("#####strRes:"+strRes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		String[] a = {"a"};
		
		String k = JSON.toJSONString(a);
		String[] b = JSON.parseObject(k, String[].class);
		
		System.err.println(b[0]);
		
		
		String c = "F18A7B3C-7756-4E4B-B072-C6F3ABC0F034";
		String uuidServerMd5 = Md5CommonUtils.getMD5String(Md5CommonUtils
				.getMD5String(c) + c);
		System.err.println(uuidServerMd5);
				
	}
	
	@Test
	public void test1() {
		uuidBlackListMapper.getBlackListByUuid("sdsgsd");
	}
}
