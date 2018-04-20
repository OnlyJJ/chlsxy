package test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.base.service.IUserAccusationInfoService;
import com.lm.live.cache.constants.CacheKey;
import com.lm.live.common.redis.RedisUtil;
import com.lm.live.common.vo.Page;
import com.lm.live.decorate.service.IDecoratePackageService;
import com.lm.live.guard.service.IGuardService;
import com.lm.live.home.service.IGwFilesService;
import com.lm.live.home.vo.BannerVo;
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

	@Test
	public void test() {
		String userId = "100357";
		String roomId = "102692";
		String anchorId = "102029";
		String toUserId = "102029";
		try {
//			UserInfo vo = userInfoService.getUserInfo(userId);
			Page page = new Page();
			page.setCount(0);
			page.setPageNum(1);
			page.setPagelimit(36);
			UserInfo vo = new UserInfo();
			vo.setSex("m");
			vo.setNickName("渣渣辉");
			vo.setRemark("你是个渣渣呀");
			vo.setUserId(userId);
//			userInfoService.modifyUserBase(vo);
//			userInfoService.userAttention(userId, "102029", 0);
//			JSONObject jsonRes = userInfoService.listFans(userId, page);
//			decoratePackageService.updateStatus(userId, 1, 1);;
//			String cacheKey = MCPrefix.DECORATEPACKAGE_USER_CACHE + userId;
//			RedisUtil.del(cacheKey);
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
			JSONObject jsonRes =  giftService.qryGiftData();
//			AccusationVo av = new  AccusationVo();
//			av.setAccusationType(1);
//			av.setAccusationDesc("不开车，没意思");
//			userAccusationInfoService.recordAccusationInfo(userId, toUserId, av);
//			JSONObject jsonRes = roomService.getRoomOnlineData(roomId, page);
//			JSONObject jsonRes = guardService.getRoomGuardData(userId, roomId);
//			JSONObject jsonRes = giftService.qryGiftData();
//			JSONObject jsonRes =  userPackageService.listUserBagData(userId);
			if(jsonRes != null) {
				System.err.println(jsonRes.toString());
			} else {
				System.err.println("null ....");
			}
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
