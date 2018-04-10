package com.lm.live.userbase.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.lm.live.common.service.impl.CommonServiceImpl;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.RegexUtil;
import com.lm.live.userbase.dao.CodeRandomMapper;
import com.lm.live.userbase.domain.CodeRandom;
import com.lm.live.userbase.enums.ErrorCode;
import com.lm.live.userbase.exception.UserBaseBizException;
import com.lm.live.userbase.service.ICodeRandomService;

@Service("codeRandomService")
public class CodeRandomServiceImpl extends
		CommonServiceImpl<CodeRandomMapper, CodeRandom> implements
		ICodeRandomService {

	@Resource
	public void setDao(CodeRandomMapper dao) {
		this.dao = dao;
	}

	// 最少可用值
	private static final int minUseEnable = 35000;

	// 最大的code值
	private final int maxUserId = 99999999;

	@Override
	public boolean checkIfLiangHao(String code) throws Exception {
		if (StringUtils.isEmpty(code)) {
			throw new UserBaseBizException(ErrorCode.ERROR_101);
		}
		// 过滤的正则表达式集合
		List patternStringList = new ArrayList();
		// --------------- 3位靓号
		// 　3位以上的重复数字
		patternStringList.add("\\d*(\\d)\\1{2,}\\d*");
		// 　3位爱情靓号
		patternStringList.add("\\d*(520|530|521|230|921){1,}\\d*");
		// 　财富号
		patternStringList.add("\\d*(168|668|918|998|518){1,}\\d*");
		// 　4位爱情靓号
		patternStringList
				.add("\\d*(1314|5120|5230|8013|9421|2013|3344|1420){1,}\\d*");
		// 3位以上顺增或顺降
		patternStringList
				.add("\\d*(?:(?:0(?=1)|1(?=2)|2(?=3)|3(?=4)|4(?=5)|5(?=6)|6(?=7)|7(?=8)|8(?=9)){2}|(?:9(?=8)|8(?=7)|7(?=6)|6(?=5)|5(?=4)|4(?=3)|3(?=2)|2(?=1)|1(?=0)){2})\\d*");

		// 至少有两组重复2次以上的数
		patternStringList.add("\\d*(\\d)\\1{1,}\\d*(\\d)\\2{1,}\\d*");
		// 二连号 ABABAB...
		patternStringList.add("\\d*(\\d{2})\\1{1,}\\d*");

		return RegexUtil.contains(code, patternStringList);
	}

	@Override
	public void batchGenerateNewCode(int batchSize) throws Exception {
		LogUtil.log.info("--------batchGenerateNewCode,batchSize:" + batchSize);
		// db目前的最大code
		String dbMaxCode = queryDbMaxCode();
		// 批量插入
		batchInsert(dbMaxCode, batchSize);
	}

	/**
	 * 查询db中目前最大的code
	 * 
	 * @return
	 */
	private String queryDbMaxCode() {
		return dao.getMaxCode();
	}

	/**
	 * 执行批量插入
	 * 
	 * @param dbMaxCode
	 *            db现最大的code
	 * @param batchSize
	 *            此次要插入的条数
	 * @throws SQLException
	 */
	private void batchInsert(String dbMaxCode, int batchSize) throws Exception {
		LogUtil.log.info("--------batchGenerateNewCode,dbMaxCode:" + dbMaxCode
				+ ",size:" + batchSize);
		// 可用的用户号
		int countUserUseEnable = dao.getUserCount();
		// 可用的房间号
		int countRoomUseEnable = dao.getRoomCount();
		// 可用的数量大于最少可用值，则不执行批量插入
		if (countUserUseEnable > minUseEnable
				&& countRoomUseEnable > minUseEnable) {
			LogUtil.log
					.info("--------don't batchInsert t_code_random,conf-minUseEnable:"
							+ minUseEnable
							+ ",countUserUseEnable:"
							+ countUserUseEnable
							+ ",countRoomUseEnable:"
							+ countRoomUseEnable);
			return;
		}
		if (!StringUtils.isEmpty(dbMaxCode)) {
			// 此次插入的起点
			int generateStartCode = Integer.parseInt(dbMaxCode);
			LogUtil.log.info("--------begin-add-t_random_code dbMaxCode:"
					+ dbMaxCode + ",size:" + batchSize);
			// 成功插入的条数
			int insertSuccessSize = 0;
			for (; insertSuccessSize < batchSize; insertSuccessSize++) {
				// 新号+1
				generateStartCode++;
				if (generateStartCode > maxUserId) {
					// 超过最大的code值
					LogUtil.log.info("--------break,over maxUserId:"
							+ maxUserId);
					break;
				}

				// 跳过7位id，直接生成8位
				if (generateStartCode > 999999 && generateStartCode < 10000000) {
					generateStartCode = 10000000;
					insertSuccessSize--;
					continue;
				}

				// 检测是否靓号
				if (checkIfLiangHao(generateStartCode + "")) {
					// 碰到靓号时,i--,保证每次都能插入固定的量,不然会有可能因为靓号而卡在这里,一直跳不过去(比如:210XXX)
					insertSuccessSize--;
					// 靓号则跳过
					LogUtil.log.info("--------continue,code is lianghao:"
							+ generateStartCode);
					continue;
				} else {
					// 检测是否已存在此号
					CodeRandom code = dao.getCodeRandom(String
							.valueOf(generateStartCode));
					if (code != null) {
						LogUtil.log
								.info("--------continue,code is exists in db:"
										+ generateStartCode);
						continue;
					} else {
						code = new CodeRandom();
						code.setIsuseruse(0);
						code.setIsroomuse(0);
						code.setCode(String.valueOf(generateStartCode));
						dao.insert(code);
					}
				}
			}
			LogUtil.log.info("--------end-add-t_random_code endCode:"
					+ generateStartCode + ",size:" + batchSize
					+ ",insertSuccessSize:" + insertSuccessSize);
		} else {
			LogUtil.log
					.info("----------batchInsert param:dbMaxCode is null,don't batchInsert");
		}
	}

	@Override
	public List<CodeRandom> listCodeRandom() {
		return dao.listCodeRandom();
	}

	@Override
	public void updateStatus(String code) {
		dao.updateStatus(code);
	}

}
