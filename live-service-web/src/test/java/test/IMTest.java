package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.IMutils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:spring/spring-context.xml"})
public class IMTest {

	public static void main(String[] args) throws Exception {
		JSONObject content = new JSONObject();
		content.put("msg", "this is test msg!");
		JSONObject imData = new JSONObject();
		imData.put("msgtype", 2); 
		imData.put("targetid", "666999");
		imData.put("type", 1);
		imData.put("content", content);
		
		IMutils.sendMsg2IM(11000, 1, imData, "153706");
	}
	
	@Test
	public void test() {
		JSONObject content = new JSONObject();
		content.put("msg", "this is test msg!");
		JSONObject imData = new JSONObject();
		imData.put("msgtype", 2); 
		imData.put("targetid", "666999");
		imData.put("type", 1);
		imData.put("content", content);
		
		try {
			IMutils.sendMsg2IM(11000, 1, imData, "153706");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
