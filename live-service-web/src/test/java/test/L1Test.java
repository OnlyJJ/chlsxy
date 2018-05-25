package test;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.HttpUtils;
import com.lm.live.common.utils.Md5CommonUtils;
import com.lm.live.common.utils.RegexUtil;
import com.lm.live.login.dao.UuidBlackListMapper;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:spring/spring-context.xml"})
public class L1Test {
	@Resource
	private UuidBlackListMapper uuidBlackListMapper;
	
	@Test
	public void test() {
		 String url=  "";
		 url=  "http://192.168.1.70:8616/L7/0/";
		 JSONObject  json = new JSONObject();
		 JSONObject  code = new JSONObject();
		 JSONObject  deviceproperties = new JSONObject();
		 code.put("a", "100138");
		 code.put("c", "123456");
		 deviceproperties.put("a", "M5 Note");
		 deviceproperties.put("b", "F18A7B3C-7756-4E4B-B072-C6F3ABC0F034"); 
		 deviceproperties.put("i", 1);
		 json.put("code", code);
//		 json.put("deviceproperties", deviceproperties);
		  
		  System.out.println("#####str:"+json.toString());
		 try {
			String strRes = HttpUtils.post(url, json.toString());
			System.out.println("#####strRes:"+strRes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
//		String[] a = {"a"};
//		
//		String k = JSON.toJSONString(a);
//		String[] b = JSON.parseObject(k, String[].class);
//		
//		System.err.println(b[0]);
//		
//		
//		String c = "F18A7B3C-7756-4E4B-B072-C6F3ABC0F034";
//		String uuidServerMd5 = Md5CommonUtils.getMD5String(Md5CommonUtils
//				.getMD5String(c) + c);
//		System.err.println(uuidServerMd5);
//		
		List<String> list = new ArrayList<String>();
		String code = "102678";
		// AABB
		String a1 = "\\d*(\\d)\\1{1,}(\\d)\\2{1,}\\d*";
		list.add(a1);
		
		System.err.println(RegexUtil.contains(code, list));
				
	}
	
	@Test
	public void test1() {
		 String url=  "";
		 url=  "http://127.0.0.1:8088/service_8616/H1/0/";
		 JSONObject  json = new JSONObject();
		 JSONObject  kind = new JSONObject();
		 JSONObject page = new JSONObject();
		 JSONObject  deviceproperties = new JSONObject();
		 kind.put("a", 1);
		 deviceproperties.put("a", "M5 Note");
		 deviceproperties.put("b", "F18A7B3C-7756-4E4B-B072-C6F3ABC0F034"); 
		 deviceproperties.put("i", 1);
		 page.put("a", 0);
		 page.put("b", 1);
		 page.put("c", 36);
		 json.put("page", page);
		 json.put("kind", kind);
		 json.put("deviceproperties", deviceproperties);
		 
		  
		  System.out.println("#####str:"+json.toString());
		 try {
			String strRes = HttpUtils.post(url, json.toString());
			System.out.println("#####strRes:"+strRes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test2() {
		 String url=  "";
		 url=  "http://127.0.0.1:8088/service_8616/G2/0/";
		 JSONObject  json = new JSONObject();
		 JSONObject  userinfo = new JSONObject();
		 JSONObject  requestvo = new JSONObject();
		 JSONObject  deviceproperties = new JSONObject();
		 requestvo.put("a", "100482");
		 userinfo.put("b3", "cococ2");
		 userinfo.put("b6", "w");
		 userinfo.put("g", "甘霖娘aaaaaa");
		 
		 deviceproperties.put("a", "M5 Note");
		 deviceproperties.put("b", "F18A7B3C-7756-4E4B-B072-C6F3ABC0F034"); 
		 deviceproperties.put("i", 1);
//		 json.put("deviceproperties", deviceproperties);
//		 json.put("userinfo", userinfo);
		 json.put("requestvo", requestvo);
		  System.out.println("#####str:"+json.toString());
		 try {
			String strRes = HttpUtils.post(url, json.toString());
			System.out.println("#####strRes:"+strRes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
