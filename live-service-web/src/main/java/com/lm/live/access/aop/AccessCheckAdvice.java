//package com.lm.live.access.aop;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.Resource;
//
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//
//import com.lm.live.access.enums.ErrorCode;
//import com.lm.live.access.exception.AccessBizException;
//import com.lm.live.common.utils.JsonUtil;
//import com.lm.live.common.utils.LogUtil;
//import com.lm.live.common.utils.StrUtil;
//import com.lm.live.guard.service.IGuardService;
//import com.lm.live.room.enums.RoomEnum;
//import com.lm.live.room.service.IRoomService;
//import com.lm.live.room.vo.RoomBannedOperationVo;
//import com.lm.live.user.service.IUserCacheInfoService;
//
//
///**
// * 权限校验
// * 校验RoomBannedOperationServiceImpl下，禁言、解禁、踢人的权限
// * @author sx
// */
//@Component
//@Aspect
//public class AccessCheckAdvice {
//	
//	/** 禁言 */
//	public  final static int actionTypeForbidSpeak = Integer.parseInt(RoomEnum.ActionType.forbidSpeak.getValue());
//	
//	/** 踢人 */
//	public final  static int actionTypeFourceOut = Integer.parseInt(RoomEnum.ActionType.fourceOut.getValue());
//	
//	/** 解禁 */
//	public final  static int actionTypeUnForbidSpeak = Integer.parseInt(RoomEnum.ActionType.unForbidSpeak.getValue());
//	
//	/** 黄金守护的等级   */
//	public static int goldGuardLevel = 2;
//
//	@Resource
//	private IUserCacheInfoService userCacheInfoService;
//	
//	@Resource
//	private IRoomService roomService;
//	
//	@Resource
//	private IGuardService guardWorkService;
//	
//	/**
//	 * 禁言、踢人、解禁方法
//	 */
//	/*@Pointcut("execution(* com.jiujun.shows.room.service.impl.RoomBannedOperationServiceImpl.*(..))")
//	public void aspect() {
//	}*/
//	
//	
//	/**
//	 * 房间禁言、踢人、解禁权限检查
//	 */
//	@Pointcut("execution(* com.jiujun.shows.room.service.impl.RoomBannedOperationServiceImpl.*(..))")
//	public void aspectRoomAction() {
//		
//	}
//	
//	
//	/**
//	 * 房间,设置管理、取消管理
//	 */
//	@Pointcut("execution(* com.jiujun.shows.user.service.impl.UserRoomMembersServiceImpl.mgrUserRoomMembers(..))")
//	public void mgrUserRoomMembers() {
//	}
//	
//	
//	/**
//	 * 房间禁言、踢人、解禁权限检查逻辑
//	 * @param fromUserId
//	 * @param roomBannedOperationVo
//	 * @throws Exception
//	 */
//	@Before("aspectRoomAction() && args(fromUserId,roomBannedOperationVo)")
//	public void beforeAspectRoomAction(String fromUserId,
//			RoomBannedOperationVo roomBannedOperationVo) throws Exception {
//		LogUtil.log.info(String.format("#######begin-beforeAspectRoomAction,fromUserId:%s,roomBannedOperationVo:%s ", fromUserId,JsonUtil.beanToJsonString(roomBannedOperationVo)));
//		if(org.apache.commons.lang.StringUtils.isEmpty(fromUserId) || roomBannedOperationVo==null){
//			throw new AccessBizException(ErrorCode.ERROR_101);
//		}
//		
//		String toUserId = roomBannedOperationVo.getUserId();
//		String roomId = roomBannedOperationVo.getRoomid();
//		
//		if(StrUtil.isNullOrEmpty(toUserId) || StrUtil.isNullOrEmpty(roomId)){
//			throw new AccessBizException(ErrorCode.ERROR_101);
//		}
//		
//		int toDoActionType = roomBannedOperationVo.getType(); // 操作类型，0:禁言;1:踢出;2:解除禁言;
//		
//		switch (toDoActionType) {
//			case 0: // 禁言
//				// 检查是否有禁言权限
//				checkPermissionAboutForbidSpeak(fromUserId, toUserId,roomId);
//				break;
//			case 1: // 踢人
//				 // 检查是否有踢人权限
//				checkPermissionAboutForceOut(fromUserId, toUserId,roomId);
//				break;	
//			case 2: // 解禁 
//				// 检查是否有解禁权限
//				checkPermissionAboutUnForbidSpeak(fromUserId, toUserId,roomId);
//				break;
//			case 5: // 拉黑
//				// 暂时不处理权限逻辑
//				break;
//			default:
//				throw new AccessBizException(ErrorCode.ERROR_101);
//		}
//		
//		LogUtil.log.info(String.format("#######end-beforeAspectRoomAction,fromUserId:%s,roomBannedOperationVo:%s ", fromUserId,JsonUtil.beanToJsonString(roomBannedOperationVo)));
//	}
//
//
//	/**
//	 * 校验解禁权限
//	 * @param fromUserId
//	 * @param roomBannedOperationVo
//	 * @throws Exception
//	 */
//	private void checkPermissionAboutUnForbidSpeak(String fromUserId,String toUserId ,String roomId) throws Exception {
//		
//		// 当前禁言、踢人等操作者
//		UserInfoVo userInfoVo = userCacheInfoService.getInfoFromCache(fromUserId, roomId);
//		// 当前被禁言、被踢的人（普通用户、房管、主播、官方超级管理员，以及后期的守护者）
//		UserInfoVo toBeBannedUser = userCacheInfoService.getInfoFromCache(toUserId, roomId);
//		if (userInfoVo == null || toBeBannedUser == null) {
//			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3041);
//			LogUtil.log.error(e.getMessage(), e);
//			throw e;
//		} 
//		
//		// 当前操作用户和被操作用户在房间内的守护信息
//		boolean fromUserIsGuard = false;
//		boolean toUserIsGuard = false;
//		int fromUserGuardLevel = 0;
//		int toUserGuardLevel = 0;
//		String levelKey = "level";
//		List<Map> fromUserList = guardWorkService.getUserGuardInfoByRoomCache(fromUserId, roomId);
//		if(fromUserList != null && fromUserList.size() >0) {
//			Map fromUserMap = fromUserList.get(0);
//			if(fromUserMap.containsKey(levelKey) && fromUserMap.get(levelKey) != null) {
//				fromUserGuardLevel = Integer.parseInt(fromUserMap.get(levelKey).toString());
//				fromUserIsGuard =true;
//			}
//		}
//		List<Map> toUserList = guardWorkService.getUserGuardInfoByRoomCache(toUserId, roomId);
//		if(toUserList != null && toUserList.size() >0) {
//			Map toUserMap = toUserList.get(0);
//			if(toUserMap.containsKey(levelKey) && toUserMap.get(levelKey) != null) {
//				toUserGuardLevel = Integer.parseInt(toUserMap.get(levelKey).toString());
//				toUserIsGuard = true;
//			}
//		}
//		
//		// 判断操作者和被操作者身份
//		String fromUserRoleType = userInfoVo.getType(); // 操作者类型 1:主播，2:普通用户，3:房管 , 4:游客 , 5 官方人员
//		String toUserRoleType = toBeBannedUser.getType(); // 被操作者类型  1:主播，2:普通用户，3:房管 ,4:游客，5，官方人员
//		
//		LogUtil.log.info(String.format("###checkPermissionAboutUnForbidSpeak-beforeAspectRoomAction-roomId:%s,fromUser:%s,fromUserRoleType:%s,fromUserIsGuard:%s,fromUserGuardLevel:%s,toUser:%s,toUserRoleType:%s,toUserIsGuard:%s,toUserGuardLevel:%s",roomId,fromUserId,fromUserRoleType,fromUserIsGuard,fromUserGuardLevel,toUserId,toUserRoleType,toUserIsGuard,toUserGuardLevel));
//		
//		// 操作者是主播
//		if(UserInfoVoEnum.Type.Anchor.getValue().equals(fromUserRoleType)) {
//			
//		// 操作者是房管	
//		} else if(UserInfoVoEnum.Type.RoomMgr.getValue().equals(fromUserRoleType)) {
//			//房管不能解禁自己
//			if(fromUserId.equals(toUserId)){
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3126);
//				LogUtil.log.error(e.getMessage(), e);
//				throw e;
//			} 			
//		// 操作者是官方管理
//		} else if(UserInfoVoEnum.Type.OfficialUser.getValue().equals(fromUserRoleType)) {
//			
//			
//		//操作者是其他(普通用户、游客)
//		} else {
//			if(fromUserIsGuard && fromUserGuardLevel == goldGuardLevel) { // 黄金守护可以解禁普通用户和房管
//				if(toUserIsGuard) { // 不能解禁守护
//					Exception e = new SystemDefinitionException(ErrorCode.ERROR_3040);
//					LogUtil.log.error(e.getMessage(), e);
//					throw e;
//				} 			
//			} else {
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3040);
//				LogUtil.log.error(e.getMessage(), e);
//				throw e;
//			}
//		}
//		
//	}
//
//	/**
//	 *  校验踢人权限
//	 * @param fromUserId
//	 * @param roomBannedOperationVo
//	 * @throws Exception
//	 */
//	private void checkPermissionAboutForceOut(String fromUserId,String toUserId ,String roomId) throws Exception {
//		
//		// 当前禁言、踢人等操作者
//		UserInfoVo userInfoVo = userCacheInfoService.getInfoFromCache(fromUserId, roomId);
//		// 当前被禁言、被踢的人（普通用户、房管、主播、官方超级管理员，以及后期的守护者）
//		UserInfoVo toBeBannedUser = userCacheInfoService.getInfoFromCache(toUserId, roomId);
//		
//		if (userInfoVo == null || toBeBannedUser == null) {
//			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3041);
//			LogUtil.log.error(e.getMessage(), e);
//			throw e;
//		} 
//		
//		String userLevel = userInfoVo.getUserLevel(); // 操作者等级
//		String toUserLevel = toBeBannedUser.getUserLevel(); // 呗操作者等级
//		int uLevel = Integer.parseInt(userLevel.substring(1,userLevel.length()));
//		int toLevel = Integer.parseInt(toUserLevel.substring(1,toUserLevel.length()));
//		
//		LogUtil.log.info(String.format("###checkPermissionAboutForceOut-userLevel:%s,toUserLevel:%s,uLevel:%s,toLevel:%s",userLevel,toUserLevel,uLevel,toLevel));
//		
//		
//		// 用户防踢特权
//		int decorateId = DecorateTableEnum.Id.UserSpecialDer.getValue();
//		DecoratePackage decoratePackage = decoratePackageService.getDecoratePackageByUserIdAndDercoateId(toUserId, decorateId);
//		// 被操作的用户是否具有特权勋章
//		boolean isToUserHasSpecialDecorate = false;
//		Date nowDate = new Date();
//		if(decoratePackage != null) {
//			Date endTime = decoratePackage.getEndtime();
//			int isPeriod = decoratePackage.getIsperiod();
//			if((isPeriod == 0) || (isPeriod == 1 && endTime.after(nowDate))) {
//				isToUserHasSpecialDecorate = true;
//			}
//		}
//		// 当前操作用户和被操作用户在房间内的守护信息
//		boolean fromUserIsGuard = false;
//		boolean toUserIsGuard = false;
//		int fromUserGuardLevel = 0;
//		int toUserGuardLevel = 0;
//		String levelKey = "level";
//		List<Map> fromUserList = guardWorkService.getUserGuardInfoByRoomCache(fromUserId, roomId);
//		if(fromUserList != null && fromUserList.size() >0) {
//			Map fromUserMap = fromUserList.get(0);
//			if(fromUserMap.containsKey(levelKey) && fromUserMap.get(levelKey) != null) {
//				fromUserGuardLevel = Integer.parseInt(fromUserMap.get(levelKey).toString());
//				fromUserIsGuard =true;
//			}
//		}
//		List<Map> toUserList = guardWorkService.getUserGuardInfoByRoomCache(toUserId, roomId);
//		if(toUserList != null && toUserList.size() >0) {
//			Map toUserMap = toUserList.get(0);
//			if(toUserMap.containsKey(levelKey) && toUserMap.get(levelKey) != null) {
//				toUserGuardLevel = Integer.parseInt(toUserMap.get(levelKey).toString());
//				toUserIsGuard = true;
//			}
//		}
//		
//		// 判断操作者和被操作者身份
//		String fromUserRoleType = userInfoVo.getType(); // 操作者类型 1:主播，2:普通用户，3:房管 , 4:游客 , 5 官方人员
//		String toUserRoleType = toBeBannedUser.getType(); // 被操作者类型  1:主播，2:普通用户，3:房管 ,4:游客，5，官方人员
//		
//		LogUtil.log.info(String.format("###checkPermissionAboutForceOut-beforeAspectRoomAction-roomId:%s,fromUser:%s,fromUserRoleType:%s,fromUserIsGuard:%s,fromUserGuardLevel:%s,toUser:%s,toUserRoleType:%s,toUserIsGuard:%s,toUserGuardLevel:%s,isToUserHasSpecialDecorate:%s",roomId,fromUserId,fromUserRoleType,fromUserIsGuard,fromUserGuardLevel,toUserId,toUserRoleType,toUserIsGuard,toUserGuardLevel,isToUserHasSpecialDecorate));
//		
//		// 操作者是主播
//		if(UserInfoVoEnum.Type.Anchor.getValue().equals(fromUserRoleType)) {
//			// 国王及以上，只能被官方踢
//			if(toLevel >= 24) {
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3040);
//				LogUtil.log.error(e.getMessage(), e);
//				throw e;
//			}
//			//主播不能被踢
//			if(UserInfoVoEnum.Type.Anchor.getValue().equals(toUserRoleType)) {
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3040);
//				LogUtil.log.error(e.getMessage(), e);
//				throw e;
//			}
//			//官方不能被踢
//			if(toBeBannedUser.isIfOfficialUser()) {
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3040);
//				LogUtil.log.error(e.getMessage(), e);
//				throw e;
//			}
//			//特权不能被踢
//			if(isToUserHasSpecialDecorate) {
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3040);
//				LogUtil.log.error(e.getMessage(), e);
//				throw e;
//			}
//			
//			//守护不能被踢
//			if(toUserIsGuard) {
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3040);
//				LogUtil.log.error(e.getMessage(), e);
//				throw e;
//			}
//		// 操作者是房管	
//		} else if(UserInfoVoEnum.Type.RoomMgr.getValue().equals(fromUserRoleType)) {
//			//主播不能被踢
//			if(UserInfoVoEnum.Type.Anchor.getValue().equals(toUserRoleType)) {
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3040);
//				LogUtil.log.error(e.getMessage(), e);
//				throw e;
//			}
//			//官方不能被踢
//			if(toBeBannedUser.isIfOfficialUser()) {
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3040);
//				LogUtil.log.error(e.getMessage(), e);
//				throw e;
//			}
//			
//			//房管不能被踢
//			if(UserInfoVoEnum.Type.RoomMgr.getValue().equals(toUserRoleType)){
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3040);
//				LogUtil.log.error(e.getMessage(), e);
//				throw e;
//			}
//			
//			//特权不能被踢
//			if(isToUserHasSpecialDecorate) {
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3040);
//				LogUtil.log.error(e.getMessage(), e);
//				throw e;
//			}
//			
//			//守护不能被踢
//			if(toUserIsGuard) {
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3040);
//				LogUtil.log.error(e.getMessage(), e);
//				throw e;
//			}
//			// 国王以上，只能被官方踢
//			if(toLevel > 22) {
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3040);
//				LogUtil.log.error(e.getMessage(), e);
//				throw e;
//			}
//			// 亲王、王储，不能被低于自己等级，以及高于自己2级以内的管理踢
//			if(toLevel > 21 && toLevel <= 22) {
//				if(toLevel > uLevel || (uLevel - toLevel <= 2)) {
//					Exception e = new SystemDefinitionException(ErrorCode.ERROR_3040);
//					LogUtil.log.error(e.getMessage(), e);
//					throw e;
//				}
//			}
//			//　公。王等级的，不能被低于自己等级的管理踢
//			if(toLevel > 16 && toLevel <= 21) {
//				if(toLevel > uLevel) {
//					Exception e = new SystemDefinitionException(ErrorCode.ERROR_3040);
//					LogUtil.log.error(e.getMessage(), e);
//					throw e;
//				}
//			}
//			// 爵位等级的，不能被低于自己2级的管理踢
//			if(toLevel > 10 && toLevel <= 16) {
//				if((toLevel - uLevel) > 2) {
//					Exception e = new SystemDefinitionException(ErrorCode.ERROR_3040);
//					LogUtil.log.error(e.getMessage(), e);
//					throw e;
//				}
//			}
//		// 操作者是官方管理
//		} else if(UserInfoVoEnum.Type.OfficialUser.getValue().equals(fromUserRoleType)) {
//			//官方不能被踢
//			if(toBeBannedUser.isIfOfficialUser()) {
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3040);
//				LogUtil.log.error(e.getMessage(), e);
//				throw e;
//			}
//			
//		//操作者是其他(普通用户、有空)
//		} else {
//			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3040);
//			LogUtil.log.error(e.getMessage(), e);
//			throw e;
//		}
//		
//	}
//
//	/**
//	 *  校验禁言权限
//	 * @param fromUserId
//	 * @param roomBannedOperationVo
//	 * @throws Exception
//	 */
//	private void checkPermissionAboutForbidSpeak(String fromUserId,String toUserId ,String roomId) throws Exception {
//		LogUtil.log.info(String.format("#######begin-禁言权限校验,fromUserId:%s,toUserId:%s,roomId:%s",fromUserId,toUserId,roomId));
//		// 当前禁言、踢人等操作者
//		UserInfoVo userInfoVo = userCacheInfoService.getInfoFromCache(fromUserId, roomId);
//		// 当前被禁言、被踢的人（普通用户、房管、主播、官方超级管理员，以及后期的守护者）
//		UserInfoVo toBeBannedUser = userCacheInfoService.getInfoFromCache(toUserId, roomId);
//		if (userInfoVo == null || toBeBannedUser == null) {
//			Exception e = new SystemDefinitionException(ErrorCode.ERROR_3041);
//			LogUtil.log.error(e.getMessage(), e);
//			throw e;
//		} 
//		
//		// 用户防踢特权
////		int decorateId = DecorateTableEnum.Id.UserSpecialDer.getValue();
////		DecoratePackage decoratePackage = decoratePackageService.getDecoratePackageByUserIdAndDercoateId(toUserId, decorateId);
////		// 被操作的用户是否具有特权勋章
////		boolean isToUserHasSpecialDecorate = false;
////		Date nowDate = new Date();
////		if(decoratePackage != null) {
////			Date endTime = decoratePackage.getEndtime();
////			int isPeriod = decoratePackage.getIsperiod();
////			if((isPeriod == 0) || (isPeriod == 1 && endTime.after(nowDate))) {
////				isToUserHasSpecialDecorate = true;
////			}
////		}
//		// 当前操作用户和被操作用户在房间内的守护信息
//		boolean fromUserIsGuard = false;
//		boolean toUserIsGuard = false;
//		int fromUserGuardLevel = 0;
//		int toUserGuardLevel = 0;
//		String levelKey = "level";
//		List<Map> fromUserList = guardWorkService.getUserGuardInfoByRoomCache(fromUserId, roomId);
//		if(fromUserList != null && fromUserList.size() >0) {
//			Map fromUserMap = fromUserList.get(0);
//			if(fromUserMap.containsKey(levelKey) && fromUserMap.get(levelKey) != null) {
//				fromUserGuardLevel = Integer.parseInt(fromUserMap.get(levelKey).toString());
//				fromUserIsGuard =true;
//			}
//		}
//		List<Map> toUserList = guardWorkService.getUserGuardInfoByRoomCache(toUserId, roomId);
//		if(toUserList != null && toUserList.size() >0) {
//			Map toUserMap = toUserList.get(0);
//			if(toUserMap.containsKey(levelKey) && toUserMap.get(levelKey) != null) {
//				toUserGuardLevel = Integer.parseInt(toUserMap.get(levelKey).toString());
//				toUserIsGuard = true;
//			}
//		}
//		
//		// 判断操作者和被操作者身份
//		String fromUserRoleType = userInfoVo.getType(); // 操作者类型 1:主播，2:普通用户，3:房管 , 4:游客 , 5 官方人员
//		String toUserRoleType = toBeBannedUser.getType(); // 被操作者类型  1:主播，2:普通用户，3:房管 ,4:游客，5，官方人员
//		
//		String userLevel = userInfoVo.getUserLevel(); // 操作者等级
//		String toUserLevel = toBeBannedUser.getUserLevel(); // 呗操作者等级
//		int uLevel = Integer.parseInt(userLevel.substring(1,userLevel.length()));
//		int toLevel = Integer.parseInt(toUserLevel.substring(1,toUserLevel.length()));
//		
//		LogUtil.log.info(String.format("###checkPermissionAboutForbidSpeak-beforeAspectRoomAction-roomId:%s,fromUser:%s,fromUserRoleType:%s," +
//				"fromUserIsGuard:%s,fromUserGuardLevel:%s,toUser:%s,toUserRoleType:%s,toUserIsGuard:%s," +
//				"toUserGuardLevel:%s,userLevel:%s,toUserLevel:%s,uLevel:%s,toLevel:%s",roomId,fromUserId,fromUserRoleType,fromUserIsGuard,fromUserGuardLevel,
//				toUserId,toUserRoleType,toUserIsGuard,toUserGuardLevel,userLevel,toUserLevel,uLevel,toLevel));
//		// 操作者是主播
//		if(UserInfoVoEnum.Type.Anchor.getValue().equals(fromUserRoleType)) {
//			
//			//不能对主播操作禁言
//			if(UserInfoVoEnum.Type.Anchor.getValue().equals(toUserRoleType)) {
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3040);
//				LogUtil.log.error(e.getMessage(), e);
//				throw e;
//			} 
//			
//			//主播不能禁言官方
//			if(toBeBannedUser.isIfOfficialUser()) {
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3040);
//				LogUtil.log.error(e.getMessage(), e);
//				throw e;
//			}
//		// 操作者是房管	
//		} else if(UserInfoVoEnum.Type.RoomMgr.getValue().equals(fromUserRoleType)) {
//				// 房管不能对主播操作禁言
//				if(UserInfoVoEnum.Type.Anchor.getValue().equals(toUserRoleType)) {
//					Exception e = new SystemDefinitionException(ErrorCode.ERROR_3040);
//					LogUtil.log.error(e.getMessage(), e);
//					throw e;
//				// 房管不能对其他房管操作禁言
//				} else if(UserInfoVoEnum.Type.RoomMgr.getValue().equals(toUserRoleType)) {
//					// 是房管,并且是黄金守护
//					if(fromUserIsGuard && fromUserGuardLevel == goldGuardLevel ){
//						//房管+黄金守护不能禁言主播、官方、黄金守护、白银守护
//						if(UserInfoVoEnum.Type.Anchor.getValue().equals(toUserRoleType)
//								||UserInfoVoEnum.Type.OfficialUser.getValue().equals(toUserRoleType)
//								||(toUserIsGuard)) {
//							Exception e = new SystemDefinitionException(ErrorCode.ERROR_3040);
//							LogUtil.log.error(e.getMessage(), e);
//							throw e;
//						}
//					}else{
//						Exception e = new SystemDefinitionException(ErrorCode.ERROR_3040);
//						LogUtil.log.error(e.getMessage(), e);
//						throw e;
//					}
//					
//				// 房管不能对官方操作禁言
//				} else if(toBeBannedUser.isIfOfficialUser()) {
//					Exception e = new SystemDefinitionException(ErrorCode.ERROR_3040);
//					LogUtil.log.error(e.getMessage(), e);
//					throw e;
////					// 管理员不能对守护禁言
////				} else if(toUserIsGuard) {
////					Exception e = new SystemDefinitionException(ErrorCode.ERROR_3040);
////					LogUtil.log.error(e.getMessage(), e);
////					throw e;
//				}  else if(uLevel ==0 && toLevel > 10) {
//					Exception e = new SystemDefinitionException(ErrorCode.ERROR_3040);
//					LogUtil.log.error(e.getMessage(), e);
//					throw e;
//				}
//		// 操作者是官方管理
//		} else if(UserInfoVoEnum.Type.OfficialUser.getValue().equals(fromUserRoleType)) {
//			// 除了不能对官方禁言，其他都可以
//			if(toBeBannedUser.isIfOfficialUser()) {
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3040);
//				LogUtil.log.error(e.getMessage(), e);
//				throw e;
//			}
//		//操作者是其他(普通用户、游客)
//		} else {
//			// 操作者是守护
//			if(fromUserIsGuard ){
//				//if(fromUserGuardLevel == goldGuardLevel){  //操作者是黄金守护
//				{
//					//黄金守护不能禁言主播
//					if(UserInfoVoEnum.Type.Anchor.getValue().equals(toUserRoleType)) {
//						Exception e = new SystemDefinitionException(ErrorCode.ERROR_3040);
//						LogUtil.log.error(e.getMessage(), e);
//						throw e;
//					}
//					
//					//黄金守护不能禁言官方
//					if(UserInfoVoEnum.Type.OfficialUser.getValue().equals(toUserRoleType)) {
//						Exception e = new SystemDefinitionException(ErrorCode.ERROR_3040);
//						LogUtil.log.error(e.getMessage(), e);
//						throw e;
//					}
//					
//					//黄金守护不能禁言其他任何守护
//					if(toUserIsGuard) {
//						Exception e = new SystemDefinitionException(ErrorCode.ERROR_3040);
//						LogUtil.log.error(e.getMessage(), e);
//						throw e;
//					}
//				}
//				
//				// 是否拥有金守护四圣兽特权
//				boolean hasGoldGuard4seatPermision = false;
//				//守护每日能禁言本房间内其他非守护的管理员10次
//				if(UserInfoVoEnum.Type.RoomMgr.getValue().equals(toUserRoleType)) {
//					if(!toUserIsGuard){ // 非守护的管理员
//						hasGoldGuard4seatPermision = true;
//					}else{
//						Exception e = new SystemDefinitionException(ErrorCode.ERROR_3040);
//						throw e;
//					}
//				}else{ //普通用户
//					hasGoldGuard4seatPermision = true;
//				}
//				
//				if(hasGoldGuard4seatPermision){ //拥有金守护四圣兽特权
//					int dailyForbidSpeakTmes  =  this.roomBannedOperationService.qryForbidSpeakGuardDailyTimes(fromUserId,roomId);
//					LogUtil.log.info(String.format("#######禁言权限校验,fromUserId:%s,toUserId:%s,roomId:%s,拥有金守护四圣兽特权,特权已使用次数:%s",fromUserId,toUserId,roomId,dailyForbidSpeakTmes));
//					if(dailyForbidSpeakTmes >=10 ){ //判断次数是否用完
//						Exception e = new SystemDefinitionException(ErrorCode.ERROR_3182);
//						throw e;
//					}else{
//						// 次数++
//						roomBannedOperationService.recordForbidSpeakGuardDailyTimes(fromUserId, roomId);
//					}
//				}else{
//					Exception e = new SystemDefinitionException(ErrorCode.ERROR_3182);
//					throw e;
//				}
//			}else{
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3040);
//				LogUtil.log.error(e.getMessage(), e);
//				throw e;
//			}
//			
//		}
//		
//		LogUtil.log.info(String.format("#######end-禁言权限校验,fromUserId:%s,toUserId:%s,roomId:%s",fromUserId,toUserId,roomId));
//	}
//	
//	
//	
//	/**
//	 * 房间,设置管理、取消管理(权限判断)
//	 * @param userBaseInfo
//	 * @param roomBannedOperationVo
//	 * @throws Exception
//	 */
//	@Before("mgrUserRoomMembers() && args(userBaseInfo,roomBannedOperationVo)")
//	public void beforeMgrUserRoomMembers(UserBaseInfo userBaseInfo,RoomBannedOperationVo roomBannedOperationVo) throws Exception {
//		LogUtil.log.info("#######AccessCheckAdvice-beforeMgrUserRoomMembers 权限校验开始。。。 ");
//		if(userBaseInfo==null||roomBannedOperationVo==null){
//			Exception e = new SystemDefinitionException(ErrorCode.ERROR_2000);
//			LogUtil.log.error(e.getMessage(), e);
//			throw e;
//		}
//		
//		String fromUserId = userBaseInfo.getUserId();
//		
//		String roomId = roomBannedOperationVo.getRoomid();
//		LogUtil.log.info(String.format("#######设置、取消权限校验,fromUserId:%s,roomId:%s",fromUserId,roomId));
//		// 当前禁言、踢人等操作者
//		UserInfoVo userInfoVo = userCacheInfoService.getInfoFromCache(fromUserId, roomId);
//		
//		// 判断操作者和被操作者身份
//		String roleType = userInfoVo.getType(); // 操作者类型 1:主播，2:普通用户，3:房管 , 4:游客 , 5 官方人员
//		
//		
//	//	AnchorInfo  anchorInfo  = anchorInfoDao.getAnchorInfoByRoomId(roomId);
//		//if((anchorInfo==null||!anchorInfo.getUserId().equals(fromUserId))){
//		//如果既不是主播也不是官方
//		if(!UserInfoVoEnum.Type.Anchor.getValue().equals(roleType) && !UserInfoVoEnum.Type.OfficialUser.getValue().equals(roleType) ){
//			//3:设置房管;4:取消房管
//			int actionType = roomBannedOperationVo.getType();
//			// 四圣兽守护在一个有效期内可以设置管理员3次（一个有效期=7天）
//			boolean flagHasSpecialRole = false;
//			if(RoomBannedOperationEnum.ActionType.setRoomMgr.getValue().equals(actionType+"") ){ // 设置管理
//				List<GuardVo> listGuardVo = guardWorkService.getGuardWorkData(fromUserId, roomId);
//				LogUtil.log.info(String.format("###上管理listGuardVo:%s", JsonUtil.arrayToJsonString(listGuardVo)));
//				int times = 0;
//				String weekBeginDay = DateUntil.getLastWeekBeginDay();
//				String cacheKey = MCPrefix.WEEKLY_SET_ROOM_MGR_CACHE+weekBeginDay+"-"+roomId+"-"+fromUserId;
//				if(listGuardVo!=null && listGuardVo.size()>0){
//					for(GuardVo vo:listGuardVo){
//						if(!vo.getUserId().equals(fromUserId)){
//							continue;
//						}
//						int sortIndex = vo.getSortIndex();
//						if(sortIndex>=1 &&sortIndex<=4){ //是四圣兽
//							int cacheTime = MCTimeoutConstants.WEEKLY_SET_ROOM_MGR_CACHETIME;
//							Object cacheObj = MemcachedUtil.get(cacheKey);
//							if(cacheObj !=null){
//								times = Integer.parseInt(cacheObj.toString());
//								String infoMsg = String.format("#####从缓存查询到四圣兽守护设置管理次数,fromUserId:%s,roleType:%s,roomId:%s,总次数:%s", fromUserId,roleType,roomId,times);
//
//							}
//							if(times < 3){ //// 四圣兽守护在一个有效期内可以设置管理员3次（一个有效期=7天）
//								flagHasSpecialRole = true;
//								times ++;
//								MemcachedUtil.set(cacheKey, times, cacheTime) ;
//							}else{
//								flagHasSpecialRole = false;
//							}
//						}
//					}
//				}
//				String infoMsg = String.format("#####查询到四圣兽守护设置管理次数,fromUserId:%s,roleType:%s,roomId:%s,总次数:%s,cacheKey:%s", fromUserId,roleType,roomId,times,cacheKey);
//				LogUtil.log.info(infoMsg);
//				if(flagHasSpecialRole){
//					infoMsg = String.format("#####四圣兽守护设置管理,,fromUserId:%s,roleType:%s,roomId:%s,总次数:%s", fromUserId,roleType,roomId,times);
//					LogUtil.log.info(infoMsg);
//				}else{
//					String errorMsg = String.format("#####设置/取消管理-权限不足,fromUserId:%s,roleType:%s,roomId:%s", fromUserId,roleType,roomId);
//					Exception e = new SystemDefinitionException(ErrorCode.ERROR_3040);
//					LogUtil.log.error(e.getMessage(), e);
//					throw e;
//				}
//			}else{
//				String errorMsg = String.format("#####设置/取消管理-权限不足,fromUserId:%s,roleType:%s,roomId:%s", fromUserId,roleType,roomId);
//				Exception e = new SystemDefinitionException(ErrorCode.ERROR_3040);
//				LogUtil.log.error(e.getMessage(), e);
//				throw e;
//			}
//		}
//		LogUtil.log.info(String.format("#######end-设置、取消权限校验,fromUserId:%s,roomId:%s",fromUserId,roomId));
//	}
//
//}
