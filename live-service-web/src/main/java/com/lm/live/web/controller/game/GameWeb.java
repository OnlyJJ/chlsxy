package com.lm.live.web.controller.game;



import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.lm.live.account.domain.UserAccount;
import com.lm.live.common.constant.LockKey;
import com.lm.live.common.controller.BaseController;
import com.lm.live.common.redis.RdLock;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.RequestUtil;
import com.lm.live.common.vo.RequestVo;
import com.lm.live.common.vo.Result;
import com.lm.live.game.enums.ErrorCode;
import com.lm.live.game.exception.GameBizException;
import com.lm.live.game.service.IGameService;
import com.lm.live.game.service.ISignService;
import com.lm.live.game.vo.GameVo;
import com.lm.live.game.vo.SignVo;
import com.lm.live.web.vo.DataRequest;

/**
 * 游戏入口
 * @author shao.xiang
 * @date 2017年8月7日
 *
 */
@Controller("GameWeb")
public class GameWeb extends BaseController {
	
	@Resource
	private IGameService gameService;
	
	@Resource
	private ISignService signService;
	
	
	/**
	 * G1
	 * 签到
	 * @param request
	 * @param response
	 * @param q
	 * @author shao.xiang
	 * @date 2018年3月14日
	 */
	@RequestMapping(value = {"G1/{q}"} , method= {RequestMethod.POST})
	public void sign(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(RequestVo.class.getSimpleName().toLowerCase())) {
				throw new GameBizException(ErrorCode.ERROR_101);
			}
			RequestVo req = new RequestVo();
			req.parseJson(data.getData().getJSONObject(req.getShortName()));
			String userId = req.getUserId();
			String lockname = LockKey.LOCK_USER_TOOL;
			try {
				RdLock.lock(lockname);
				SignVo vo = signService.sign(userId);
				if(vo != null) {
					jsonRes.put(vo.getShortName(), vo.buildJson());
				}
			} catch(Exception e) {
				throw e;
			} finally {
				RdLock.unlock(lockname);
			}
		} catch(GameBizException e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(e.getErrorCode().getResultCode());
			result.setResultDescr(e.getErrorCode().getResultDescr());
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(ErrorCode.ERROR_100.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_100.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		long time2 = System.currentTimeMillis();
		long spendTimes = time2 - time1;
		handleInfo(LogUtil.log, request, data.getRequestStr(), spendTimes, jsonRes.toString(), true);
		out(jsonRes, request, response, q);
	}
	
	/**
	 * G2
	 * 查询签到情况，返回奖励列表
	 *@param request
	 *@param response
	 *@param q
	 *@author shao.xiang
	 *@data 2018年5月25日
	 */
	@RequestMapping(value = {"G2/{q}"} , method= {RequestMethod.POST})
	public void getSignData(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(RequestVo.class.getSimpleName().toLowerCase())) {
				throw new GameBizException(ErrorCode.ERROR_101);
			}
			RequestVo req = new RequestVo();
			req.parseJson(data.getData().getJSONObject(req.getShortName()));
			String userId = req.getUserId();
			jsonRes = signService.listPrize(userId);
		} catch(GameBizException e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(e.getErrorCode().getResultCode());
			result.setResultDescr(e.getErrorCode().getResultDescr());
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(ErrorCode.ERROR_100.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_100.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		long time2 = System.currentTimeMillis();
		long spendTimes = time2 - time1;
		handleInfo(LogUtil.log, request, data.getRequestStr(), spendTimes, jsonRes.toString(), true);
		out(jsonRes, request, response, q);
	}
	
	
	/**
	 * G5
	 * 砸蛋
	 * @param data
	 * @return
	 * @author shao.xiang
	 * @date 2017年8月7日
	 */
	@RequestMapping(value = {"G5/{q}"} , method= {RequestMethod.POST})
	public void getUserInfo(HttpServletRequest request,HttpServletResponse response, @PathVariable String q){
		long time1 = System.currentTimeMillis();
		DataRequest data = (DataRequest) RequestUtil.getDataRequest(request, response);
		Result result = new Result(ErrorCode.SUCCESS_0.getResultCode(),ErrorCode.SUCCESS_0.getResultDescr());  
		JSONObject jsonRes = new JSONObject();
		try {
			if(data==null  
					|| !data.getData().containsKey(GameVo.class.getSimpleName().toLowerCase())
					|| !data.getData().containsKey(RequestVo.class.getSimpleName().toLowerCase())) {
				throw new GameBizException(ErrorCode.ERROR_101);
			}
			RequestVo req = new RequestVo();
			req.parseJson(data.getData().getJSONObject(req.getShortName()));
			String userId = req.getUserId();
			String roomId = req.getRoomId();
			GameVo game = new GameVo();
			game.parseJson(data.getData().getJSONObject(game.getShortName()));
			int gameType = game.getGameType();
			int series = game.getSeriesConf();
			String lockname = LockKey.LOCK_USER_ACCOUNT;
			try {
				RdLock.lock(lockname);
				jsonRes = gameService.openEggs(userId, roomId, gameType, series);
			} catch(Exception e) {
				throw e;
			} finally {
				RdLock.unlock(lockname);
			}
		} catch(GameBizException e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(e.getErrorCode().getResultCode());
			result.setResultDescr(e.getErrorCode().getResultDescr());
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
			result.setResultCode(ErrorCode.ERROR_100.getResultCode());
			result.setResultDescr(ErrorCode.ERROR_100.getResultDescr());
		}
		jsonRes.put(result.getShortName(),result.buildJson());
		long time2 = System.currentTimeMillis();
		long spendTimes = time2 - time1;
		handleInfo(LogUtil.log, request, data.getRequestStr(), spendTimes, jsonRes.toString(), true);
		out(jsonRes, request, response, q);
	}
	
}
