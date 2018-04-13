package test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lm.live.common.utils.HttpUtils;
import com.lm.live.user.service.IUserInfoService;
import com.lm.live.user.vo.UserInfo;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:spring/spring-context.xml"})
public class ServiceTest {
	
	@Resource
	private IUserInfoService userInfoService;

	@Test
	public void test() {
		String userId = "100315";
		try {
			UserInfo vo = userInfoService.getUserInfo(userId);
			if(vo != null) {
				System.err.println(JSON.toJSON(vo).toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
