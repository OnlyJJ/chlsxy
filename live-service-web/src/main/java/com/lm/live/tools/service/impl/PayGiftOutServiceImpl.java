package com.lm.live.tools.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.tools.dao.PayGiftOutMapper;
import com.lm.live.tools.domain.PayGiftOut;
import com.lm.live.tools.service.IPayGiftOutService;

@Service("payGiftOutService")
public class PayGiftOutServiceImpl extends CommonServiceImpl<PayGiftOutMapper, PayGiftOut>
	implements IPayGiftOutService{

	@Resource
	public void setDao(PayGiftOutMapper dao) {
		this.dao = dao;
	}

}
