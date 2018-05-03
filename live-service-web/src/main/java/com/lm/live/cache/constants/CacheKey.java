package com.lm.live.cache.constants;

/**
 * 缓存key</br> 
 * 所有模块的缓存统一放在此处管理，避免分模块管理的重复问题</br>
 * 请按照模块分类，不要随意添加
 * @author shao.xiang
 * @Company lm
 * @data 2018年4月17日
 */
public class CacheKey {
	// im通讯
	/** im系统中存储token */
	public static final String IM_MC_SESSION_ = "mc_session_";
	
	
	// base模块
	/** 省份code缓存 */
	public static final String PROVINCE_CODE_CACHE = "province:code:";
	/** 每次缓存省市记录时间间隔key,24h */
	public static final String PROVINCE_TIME_CACHE = "province:time";
	/** 用户ip归属地缓存 */
	public static final String USER_IP_REGION_CACHE = "ip:region:";
	
	
	// 账户模块
	/** 账户基本信息缓存，只缓存等级等基本信息，升级后需要处理此缓存 */
	public static final String ACCOUNT_BASE_CACHE = "account:base:";
	
	
	// 勋章模块
	/** 用户所有有效勋章缓存，注意：每次增加勋章时，需要同步删除此缓存  */
	public static final String DECORATEPACKAGE_USER_CACHE = "decorate:pck:user:";
	/** 用户有效的勋章（查看他人时使用）注意：每次增加勋章时，需同步删除此缓存 */
	public static final String DECORATE_USER_CACHE = "decorate:user:";
	/** 主播房间勋章墙缓存，更新勋章时，删除此缓存 */
	public static final String DECORATE_ROOM_CACHE = "decorate:room:";
	
	
	// 守护模块
	/** 用户守护列表换缓存，购买守护后，需要同步删除此缓存，另，因为守护会存在过期问题，所以此缓存不应设置较长时间*/
	public static final String GUARD_USER_CACHE = "guard:user:";
	/** 房间内普通用户的守护墙缓存，房间守护有更新时，需删除此缓存:key + roomId */
	public static final String ROOM_GUARD_COMMON_CACHE = "guard:room:general";
	/** 房间内守护的缓存，以用户区分，当房间守护有更新时，需要删除所有的用户此缓存 : key + roomId + userId*/
	public static final String ROOM_GUARD_VIP_CACHE = "guard:room:vip:";
	
	
	// 首页
	/** 首页主播列表，一级缓存 */
	public static final String HOMEPAGE_ANCHOR_ONE_CACHE = "home:h1:one:";
	/** 首页主播列表，二级缓存 */
	public static final String HOMEPAGE_ANCHOR_TWO_CACHE = "home:h1:two:";
	/** 榜単列表，一级缓存 */
	public static final String HOMEPAGE_RANK_ONE_CACHE = "home:h3:one:";
	/** 榜単列表，二级缓存 */
	public static final String HOMEPAGE_RANK_TWO_CACHE = "home:h3:two:";
	
	
	// 登录模块
	/** 用户登录后，服务端派发给客户端用于数据加密token的key前缀 */
	public static final String MC_TOKEN_PREFIX = "mc_data_token_";
	public static final String MC_USERINFO_PREFIX = "mc_userinfo_";
	/** msgid消息资源映射关系的key前缀 */
	public static final String MC_MSGID_MAP_PREFIX = "mc_msgid_map_";
	/** 聊天消息中的url地址缓存的key前缀 */
	public static final String MC_MSG_URL_PREFIX = "mc_msg_url_";
	/** 系统帮未注册用户自动生成一个预登录帐号, 记录某ip对应的数量*/
	public static final String AUTO_REGIST_LIMIT_CACHE = "login:auto:ip:";
	/** 自动注册，每天的注册量限制 */
	public static final String AUTO_REGIST_LIMIT_DAY_CACHE = "login:auto:d:";
	/** 用户id库中生成的id缓存 */
	public static final String LOGIN_USERCODE_RONDOM_CACHE = "login:usercode";
	
	
	// 房间模块
	/** 房间在线成员缓存*/
	public static final String ROOM_USER_CACHE = "room:user:";
	/** 房间虚拟的在线人数  */ 
	public final static String ROOM_VM_NUM_CACHE = "room:vm:num:";
	/** 用户每天分享次数缓存，缓存时间为24H */
	public final static String ROOM_SHARE_CACHE = "room:share:num:";
	/** 房间及角色信息缓存 */
	public final static String ROOM_ROLE_CACHE = "room:role:";
	/** 房间茄子数据缓存，送礼时处理 */
	public final static String ROOM_EGGPLANT_CACHE = "room:eggplant:";
	
	
	// 用户信息
	/** 用户基本信息缓存，供service取基本信息使用，如用户名，头像，等级等，修改资料时需要处理此缓存 */
	public static final String USER_BASE_INFO_CACHE = "user:base:";
	/** 用户在房间的基本信息缓存，包含身份 ，修改身份时需要处理此缓存*/
	public static final String USER_ROOM_INFO_CACHE = "user:room:";
	/** 用户信息，供IM发消息时使用，缓存的信息由客户端决定 */
	public static final String USER_IM_CACHE = "user:im:";
	/** 用户详细信息缓存 */
	public static final String USER_INFO_CACHE = "user:info:";
	/** 用户缓存（db） */
	public static final String USER_INFODO_CACHE = "user:db:";
	/** 用户关注列表缓存 */
	public static final String USER_ATTENTION_CACHE = "user:attention:";
	/** 用户粉丝列表缓存*/
	public static final String USER_FANS_CACHE = "user:fans:";
	/** 主播基本信息缓存 */
	public static final String ANCHOR_ROOM_CACHE = "anchor:room:";
	/** 主播基本信息缓存(UserAnchor) */
	public static final String ANCHOR_BASE_CACHE = "anchor:base:";
	/** 主播粉丝总数缓存 */
	public static final String ANCHOR_FANSCOUNT_CACHE = "anchor:fans:t:";
	/** 游客昵称缓存 */
	public static final String USER_PESUDONAME_CACHE = "pesudo:name:";
	
	
	// 宠物模块
	/** 用户正在使用的宠物缓存 */
	public static final String PET_USEING_CACHE = "pet:useing:";
	/** 用户所有宠物信息缓存 */
	public static final String PET_ALL_CACHE = "pet:all:";
	
	
	// 道具模块
	/** 礼物缓存 */
	public static final String TOOL_GIFT_CACHE = "tool:gift:";
	/** 礼物列表缓存，返回给客户端展示的礼物栏列表 */
	public static final String TOOL_GIFT_ALL_CACHE = "tool:all:gift";
	/** 用户背包缓存 */
	public static final String TOOL_USER_BAG_CACHE = "tool:user:bag:";
	
	// others模块
	public static final String XINGE_PUSH_CACHE = "xinge_token_";
	
}
