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
import com.lm.live.common.vo.Page;
import com.lm.live.decorate.contants.MCPrefix;
import com.lm.live.decorate.service.IDecoratePackageService;
import com.lm.live.guard.service.IGuardService;
import com.lm.live.user.service.IUserInfoService;
import com.lm.live.user.vo.UserInfo;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:spring/spring-context.xml"})
public class ServiceTest {
	
	@Resource
	private IUserInfoService userInfoService;
	
	@Resource
	private IDecoratePackageService decoratePackageService;
	
	@Resource
	private IGuardService guardService;

	@Test
	public void test() {
		String userId = "100357";
		try {
//			UserInfo vo = userInfoService.getUserInfo(userId);
			Page page = new Page();
			page.setCount(0);
			page.setPageNum(1);
			page.setPagelimit(36);
//			userInfoService.userAttention(userId, "102029", 0);
//			JSONObject jsonRes = userInfoService.listFans(userId, page);
//			decoratePackageService.updateStatus(userId, 1, 1);;
//			String cacheKey = MCPrefix.DECORATEPACKAGE_USER_CACHE + userId;
//			RedisUtil.del(cacheKey);
//			JSONObject jsonRes = decoratePackageService.getUserDecorateData(userId);
			JSONObject jsonRes =  guardService.getGuardData(userId, page);
			if(jsonRes != null) {
				System.err.println(jsonRes.toString());
			} else {
				System.err.println("null ....");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
