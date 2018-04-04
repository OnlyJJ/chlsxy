package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.lm.live.common.redis.RedisUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:spring/spring-context.xml"})
public class CarTest {
	
//	@Resource
//	private IUserCarPortService userCarPortService;
//	
	@Test
	public void test1() {
		try {
			// 字符串转javaBean
			String key1 = "javabean";
			UserInfo u1 = new UserInfo();
			u1.setName("mmmm");
//			RedisUtil.set(key1, u1);
			UserInfo u2 = RedisUtil.getJavaBean(key1, UserInfo.class);
			System.err.println(u2.getName());
			
			// 转List<javaBean>
			String key2 = "listbean";
			List<UserInfo> l1 = new ArrayList<UserInfo>();
			l1.add(u1);
//			RedisUtil.set(key2, l1);
			List<UserInfo> listPerson = RedisUtil.getList(key2, UserInfo.class);
			System.err.println(listPerson.get(0).getName());
			
			// 转List<String>
			String key3 = "liststring";
			List<String> l2 = new ArrayList<String>();
			l2.add("dashabi");
			l2.add("cmmnn");
//			RedisUtil.set(key3, l2);
			List<String> listString = RedisUtil.getList(key3, String.class);
			System.err.println(listString.get(1));
			
			// 转 List<Map<String,Object>>
			String key4 = "listmap";
			List<Map<String, Object>> l3 = new ArrayList<Map<String, Object>>();
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("a1", "xxxxx");
			m.put("a2", "kkkkkk");
			m.put("a3", u1);
			l3.add(m);
//			RedisUtil.set(key4, l3);
			List<Map<String, Object>> listMap = RedisUtil.getListMap(key4);
			System.err.println(listMap.get(0).get("a3").toString());
			
			Map<String, Object> hs = new HashMap<String, Object>();
			hs.put("a1", "aa");
			RedisUtil.hset("hset1", "a1", u1, 30);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		Map<String, Object> m = new HashMap<String, Object>(1);
		m.put("a", new String['1']);
		String mm = JSON.toJSONString(m);
		Map<String, Object> m2 = JSON.parseObject(mm, Map.class);
		String[] s = (String[]) m2.get("a");
		System.err.println(s[0]);
		
		
		// 字符串转javaBean
//		UserInfo u1 = new UserInfo();
//		u1.setName("mmmm");
//		String s1 = JSON.toJSONString(u1); // 对象转字符串
//		UserInfo u2 = JSON.parseObject(s1, UserInfo.class); 
//		System.err.println(u2.getName());
//		
//		// 转List<javaBean>
//		List<UserInfo> l1 = new ArrayList<UserInfo>();
//		l1.add(u1);
//		String s2 = JSON.toJSONString(l1); // 对象转字符串
//		List<UserInfo> listPerson =JSON.parseArray(s2, UserInfo.class); 
//		System.err.println(listPerson.get(0).getName());
//		
//		// 转List<String>
//		List<String> l2 = new ArrayList<String>();
//		l2.add("dashabi");
//		l2.add("cmmnn");
//		String s3 = JSON.toJSONString(l2); // 对象转字符串
//		List<String> listString = JSON.parseArray(s3, String.class); 
//		System.err.println(listString.get(1));
//		
//		// 转 List<Map<String,Object>>
//		List<Map<String, Object>> l3 = new ArrayList<Map<String, Object>>();
//		Map<String, Object> m = new HashMap<String, Object>();
//		m.put("a1", "xxxxx");
//		m.put("a2", "kkkkkk");
//		m.put("a3", u1);
//		l3.add(m);
//		String s4  = JSON.toJSONString(l3); // 对象转字符串
//		List<Map<String, Object>> listMap = 
//				JSON.parseObject(s4, new TypeReference<List<Map<String,Object>>>(){});
//		
//		System.err.println(listMap.get(0).get("a3").toString());
	}
}
