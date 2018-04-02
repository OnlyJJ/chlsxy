package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.HttpUtils;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:spring/spring-context.xml"})
public class TestDemo {

	@Test
	public void test() {
		 String url=  "";
		 url=  "http://127.0.0.1:8080/live-service/HomeWeb/H1/0/";
		 JSONObject  json = new JSONObject();
		 JSONObject  kind = new JSONObject();
		 JSONObject  page = new JSONObject();
		 kind.put("a", 1);
		 page.put("b", 1);
		 page.put("c", 36);
		 json.put("kind", kind);
		 json.put("page", page);
		  
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
	}
}
