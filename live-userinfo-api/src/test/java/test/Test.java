package test;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.vo.UserBaseInfo;
import com.lm.live.user.vo.UserInfo;

public class Test {
	
	public static void main(String[] args) {
		UserInfo info = new UserInfo();
		info.setUserId("1100011");
		info.setIcon("3294985723.jpg");
		info.setNickName("厉害的飞车党");
		info.setIsFirttimeLogin(0);
		JSONObject json = new JSONObject();
		json.put("b1", "10001");
		json.put("b3", "打坐");
		
		UserInfo u = new UserInfo();
		u.parseJson(json);
		
		System.err.println(u.getNickName());
		UserBaseInfo base = new UserBaseInfo();
		
		System.err.println(info.buildJson().toString());
	}
	
	public void test() {
		
	}
}
