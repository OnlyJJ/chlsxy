package com.lm.live.pet.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lm.live.base.service.ISendMsgService;
import com.lm.live.cache.constants.CacheKey;
import com.lm.live.cache.constants.CacheTimeout;
import com.lm.live.common.redis.RedisUtil;
import com.lm.live.common.utils.StrUtil;
import com.lm.live.pet.constant.Constants;
import com.lm.live.pet.dao.PetConfMapper;
import com.lm.live.pet.dao.PetLevelMapper;
import com.lm.live.pet.dao.PetNatureMapper;
import com.lm.live.pet.dao.PetNatureRelationMapper;
import com.lm.live.pet.dao.UserPetMapper;
import com.lm.live.pet.domain.PetConf;
import com.lm.live.pet.domain.PetLevel;
import com.lm.live.pet.domain.PetNature;
import com.lm.live.pet.domain.PetNatureRelation;
import com.lm.live.pet.domain.UserPet;
import com.lm.live.pet.enums.ErrorCode;
import com.lm.live.pet.enums.PetEnums.StatusEnum;
import com.lm.live.pet.exception.PetBizException;
import com.lm.live.pet.service.IUserPetService;
import com.lm.live.pet.vo.PetVo;

/**
 * 宠物座驾服务
 * @author shao.xiang
 * @Company lm
 * @data 2018年4月13日
 */
@Service("userPetService")
public class UserPetServiceImpl implements IUserPetService {

	@Resource
	private UserPetMapper userPetMapper;
	
	@Resource
	private PetLevelMapper petLevelMapper;
	
	@Resource
	private PetConfMapper petConfMapper;
	
	@Resource
	private PetNatureRelationMapper petNatureRelationMapper;
	
	@Resource
	private PetNatureMapper petNatureMapper;
	
	@Resource
	private ISendMsgService sendMsgService;
	
	@Override
	public PetVo getUsePet(String userId) throws Exception {
		if(StrUtil.isNullOrEmpty(userId)) {
			throw new PetBizException(ErrorCode.ERROR_101);
		}
		// 从缓存中取
		String key = CacheKey.PET_USEING_CACHE + userId;
		PetVo vo = RedisUtil.getJavaBean(key, PetVo.class);
		if(vo != null) {
			return vo;
		}
		vo = new PetVo();
		UserPet up = userPetMapper.getUserPet(userId);
		if(up != null) {
			vo = getPetVo(up);
		}
		RedisUtil.set(key, vo, CacheTimeout.DEFAULT_TIMEOUT_24H);
		return vo;
	}

	@Override
	public List<PetVo> listUserPet(String userId) throws Exception {
		if(StrUtil.isNullOrEmpty(userId)) {
			throw new PetBizException(ErrorCode.ERROR_101);
		}
		String key = CacheKey.PET_ALL_CACHE + userId;
		List<PetVo> list = RedisUtil.getList(key, PetVo.class);
		if(list != null) {
			return list;
		} 
		list = new ArrayList<PetVo>();
		List<UserPet> upList = this.userPetMapper.listUserPet(userId);
		if(upList != null && upList.size() > 0) {
			for(UserPet up : upList) {
				PetVo vo = getPetVo(up);
				list.add(vo);
			}
		}
		RedisUtil.set(key, list, CacheTimeout.DEFAULT_TIMEOUT_24H);
		return list;
	}

	@Override
	public void addPetPoint(String userId, int petId, long petPoint)
			throws Exception {
		if(StrUtil.isNullOrEmpty(userId)) {
			throw new PetBizException(ErrorCode.ERROR_101);
		}
		UserPet up = userPetMapper.getUserPetByCondition(userId, petId);
		if(up == null) {
			throw new PetBizException(ErrorCode.ERROR_11002);
		}
		int nextLevel = up.getLevel() + 1;
		long pointTotal = up.getPetpoint() + petPoint;
		// 加经验
		userPetMapper.addPoint(userId, petId, petPoint);
		// 处理升级
		// 查询下一级
		PetLevel pl = petLevelMapper.getPetLevel(petId, nextLevel);
		if(pl == null) { // 如果没有下一级配置，则说明是最高级了
			return;
		}
		long nextPoint = pl.getPoint();
		if(pointTotal >= nextPoint) {
			userPetMapper.addLevel(userId, petId);
			// 删除用户宠物相关缓存
			String key = CacheKey.PET_USEING_CACHE + userId;
			RedisUtil.del(key);
			String allkey = CacheKey.PET_ALL_CACHE + userId;
			RedisUtil.del(allkey);
			String userkey = CacheKey.USER_IM_CACHE + userId;
			RedisUtil.del(userkey);
			// 推送升级消息
//			sendMsgService.sendMsg(userId,null, targetid, imType, content);
		}
	}

	@Override
	public List<PetNatureRelation> listPetNatureRelation(int petId, int level)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void buyPet(String userId, int petId) throws Exception {
		// TODO Auto-generated method stub
		
	}

	private PetVo getPetVo(UserPet up) throws Exception {
		PetVo vo = new PetVo();
		// 通过petId和level获取等级配置
		int petId = up.getId();
		// 宠物基本信息
		PetConf pc = petConfMapper.getObjectById(petId);
		if(pc == null) {
			throw new PetBizException(ErrorCode.ERROR_11002);
		}
		int level = up.getLevel();
		PetLevel pl = petLevelMapper.getPetLevel(petId, level);
		if(pl == null) {
			throw new PetBizException(ErrorCode.ERROR_11001);
		}
		// 下一等级
		long nextPoint = 0;
		PetLevel pl2 = petLevelMapper.getPetLevel(petId, level+1);
		if(pl2 != null) {
			nextPoint = pl2.getPoint();
		}
		// 宠物属性
		List<PetNatureRelation> list = petNatureRelationMapper.listPetNatureRelation(petId, level);
		if(list != null && list.size() > 0) {
			StringBuilder info = new StringBuilder();
			int index = 0;
			for(PetNatureRelation pnr : list) {
				index++;
				PetNature pn = petNatureMapper.getObjectById(pnr.getNatureId());
				if(pn == null) {
					continue;
				}
				info.append(index).append(Constants.SEPARATOR_COLON).append(pn.getInfo());
			}
			vo.setNatureInfo(info.toString());
		}
		String image = pl.getImage();
		vo.setPetId(petId);
		vo.setPetName(up.getPetname());
		vo.setPetLevel(level);
		vo.setImage(image);
		vo.setPetPoint(pl.getPoint());
		vo.setNextLevelPoint(nextPoint);
		vo.setStatus(StatusEnum.USEING.getValue());
		vo.setType(pc.getType());
		vo.setBuyAble(pc.getBuyAble());
		vo.setGold(pc.getGold());
		vo.setComment(pc.getComment());
		return vo;
	}
	
}
