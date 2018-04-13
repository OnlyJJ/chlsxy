package com.lm.live.pet.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.pet.dao.UserPetMapper;
import com.lm.live.pet.domain.UserPet;
import com.lm.live.pet.service.IUserPetService;
import com.lm.live.pet.vo.PetVo;

/**
 * 宠物座驾服务
 * @author shao.xiang
 * @Company lm
 * @data 2018年4月13日
 */
@Service("userPetService")
public class UserPetServiceImpl extends CommonServiceImpl<UserPetMapper, UserPet> implements IUserPetService {

	@Resource
	public void setDao(UserPetMapper dao) {
		this.dao = dao;
	}
	
	@Override
	public PetVo getUsePet(String userId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PetVo> listUserPet(String userId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	
}
