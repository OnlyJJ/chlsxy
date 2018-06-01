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
import com.lm.live.appclient.service.IAppStartupPageService;
import com.lm.live.base.service.IImagePackageConfigService;
import com.lm.live.base.service.IThirdpartyConfService;
import com.lm.live.base.service.IUserAccusationInfoService;
import com.lm.live.common.redis.RedisUtil;
import com.lm.live.common.utils.MD5Util;
import com.lm.live.common.utils.StrUtil;
import com.lm.live.common.vo.Page;
import com.lm.live.decorate.service.IDecoratePackageService;
import com.lm.live.game.service.IGameService;
import com.lm.live.game.service.ISignService;
import com.lm.live.guard.service.IGuardService;
import com.lm.live.home.service.IGwFilesService;
import com.lm.live.login.service.ILoginService;
import com.lm.live.room.service.IRoomService;
import com.lm.live.tools.service.IGiftService;
import com.lm.live.tools.service.IUserPackageService;
import com.lm.live.user.service.IUserCacheInfoService;
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
	
	@Resource
	private IRoomService roomService;
	
	@Resource
	private IUserCacheInfoService userCacheInfoService;
	
	@Resource
	private IUserAccusationInfoService userAccusationInfoService;
	
	@Resource
	private IGiftService giftService;
	
	@Resource
	private IUserPackageService userPackageService;
	
	@Resource
	private IGwFilesService gwFilesService;
	
	@Resource
	private IAppStartupPageService appStartupPageService;
	
	@Resource
	private IThirdpartyConfService thirdpartyConfService;
	
	@Resource
	private IGameService gameService;
	
	@Resource
	private ISignService signService;
	
	@Resource
	private IImagePackageConfigService imagePackageConfigService;
	
	@Resource
	private ILoginService loginService;

	@Test
	public void test() {
		String userId = "100526";
		String roomId = "100310";
		String anchorId = "102029";
		String toUserId = "102029";
		try {
//			UserInfo vo = userInfoService.getUserInfo(userId);
			Page page = new Page();
			page.setCount(0);
			page.setPageNum(1);
			page.setPagelimit(36);
//			UserInfo vo = new UserInfo();
//			vo.setSex("m");
//			vo.setNickName("渣渣辉");
//			vo.setRemark("你是个渣渣呀");
//			vo.setUserId(userId);
//			UserInfo info = new UserInfo();
//			info.setNickName("异次元诸葛亮100");
//			info.setUserId(userId);
//			info.setRemark("暂无愧疚");
//			userInfoService.modifyUserBase(userId, info);
//			userInfoService.modifyUserBase(vo);
//			userInfoService.userAttention(userId, "102029", 0);
//			JSONObject jsonRes = userInfoService.listFans(userId, page);
//			decoratePackageService.updateStatus(userId, 1, 1);;
//			String cacheKey = MCPrefix.DECORATEPACKAGE_USER_CACHE + userId;
//			String key = CacheKey.GUARD_USER_CACHE + userId;
//			RedisUtil.del(key);
//			JSONObject jsonRes = decoratePackageService.getUserDecorateData(userId);
//			JSONObject jsonRes =  guardService.getGuardData(userId, page);
//			JSONObject jsonRes = decoratePackageService.getRoomDecorateData(anchorId);
//			roomService.recordRoomOnlineMember(userId, roomId, 1);
//			String key = CacheKey.USER_ROOM_INFO_CACHE + userId + Constants.SEPARATOR_COLON + roomId;
//			RedisUtil.del(key);
//			roomService.recordRoomOnlineMember(userId, roomId, 1);
//			roomService.shareApp(userId, roomId, 1, "192.168.1.70");
//			roomService.buyGuard(userId, anchorId, roomId, 1, 1, 1);
//			String key = CacheKey.TOOL_GIFT_ALL_CACHE;
//			RedisUtil.del(key);
//			roomService.sendGift(userId, roomId, anchorId, 1, 1, 0);
//			AppStartupPageVo avo =  appStartupPageService.getAppStartupPage();
//			AccusationVo av = new  AccusationVo();
//			av.setAccusationType(1);
//			av.setAccusationDesc("不开车，没意思");
//			userAccusationInfoService.recordAccusationInfo(userId, toUserId, av);
//			JSONObject jsonRes = roomService.getRoomOnlineData(roomId, page);
//			JSONObject jsonRes = guardService.getRoomGuardData(userId, roomId);
//			JSONObject jsonRes = imagePackageConfigService.findNewestUseableList(null);
//			JSONObject jsonRes = giftService.qryGiftData();
//			JSONObject jsonRes =  userPackageService.listUserBagData(userId);
//			if(avo != null) {
//				System.err.println(avo.buildJson().toString());
//			} else {
//				System.err.println("null ....");
//			}
//			BannerVo vo1 = new BannerVo();
//			vo1.setShowPage(0);
//			vo1.setBannerType(1);
//			gwFilesService.getIndexPageBanner(vo1);
//			UserCache info = userCacheInfoService.getUserByChe(userId);
//			UserCache info = userCacheInfoService.getUserInRoomChe(userId, roomId);
//			UserInfoVo info = userCacheInfoService.getUserFromCache(userId, roomId);
//			String name = userCacheInfoService.getAndSetPesudoUserName(userId, null);
//			if(info != null) {
//				System.err.println(JSON.toJSON(info));
//			} else {
//				System.err.println("null ....");
//			}
//			System.err.println("name =" + name);
//			ServiceResult<ThirdpartyConf> srt = thirdpartyConfService.getThirdpartyConf(0, "com.lm.live", 0);
			int gameType = 1;
			int series = 1;
//			JSONObject jsonRes = gameService.openEggs(userId, roomId, gameType, series);
//			Date now = new Date();
//			String nowStr = DateUntil.format2Str(now, Constants.DATEFORMAT_YMD_1);
//			String key = CacheKey.SIGN_DAY_CACHE + userId + Constants.SEPARATOR_COLON + nowStr;
//			RedisUtil.del(key);
//			SignVo jsonRes = signService.sign(userId);
//			JSONObject jsonRes = loginService.register("牛逼ddd人2223", "123456");
			String pwd =  MD5Util.md5("100282" + MD5Util.md5(StrUtil.replaceBlank("123456")));
			System.err.println(pwd);
//			JSONObject jsonRes = loginService.verifyLogin("100282", pwd);
//			JSONObject jsonRes = signService.listPrize(userId);
//			if(jsonRes != null) {
//				System.err.println(jsonRes);
//			} else {
//				System.err.println("null ....");
//			}
//			JSONObject data = new JSONObject();
//			data.put("userId", "100655");
//			data.put("icon", "http://192.168.1.70/upload/default.png");
//			data.put("userLevel", 2);
//			data.put("sort", 100000);
//			RedisUtil.lpush("list2", data.toString());
//			List list = RedisUtil.lget("list2", 0, -1);
//			if(list != null && list.size() >0) {
//				for(int i=0;i<list.size(); i++) {
//					Object obj = list.get(i);
//					System.err.println("obj=" + JSON.toJSON(obj));
//				}
//			}
			// 加1000个userId
			for(int i=0; i< 1000;i++) {
				String key = "test_set";
				double score = 1.0+i;
				String member = "1000" + i;
				RedisUtil.zadd(key, score, member);
				
				// 插入每个用户的信息
				UserInfo vo = new UserInfo();
				vo.setSex("m");
				vo.setNickName("渣渣辉:" + i);
				vo.setRemark("你是个渣渣呀~" + i);
				vo.setUserId(member);
				RedisUtil.set(member, JSON.toJSONString(vo));
			}
			int num = 1;
			int limit = 36;
			int index = num > 1? (num -1) * limit : 0;
			List<UserInfo> list = new ArrayList<UserInfo>();
			for(int i=0;i<limit;i++) {
				if(list.size() > index) {
					break;
				}
				String member = "1000" + i;
				UserInfo info = RedisUtil.getJavaBean(member, UserInfo.class);
				list.add(info);
				index++;
			}
			if(list.size() >0) {
				System.err.println("list=" + list.size());
				for(UserInfo v : list) {
					System.err.println("userId = " + v.getUserId());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
