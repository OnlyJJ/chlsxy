package com.yl.service;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.yl.common.constant.MessageStatus;
import com.yl.common.exception.ServiceException;
import com.yl.vo.SocketDataVo;

/**
 * 消息持久化服务
 * @author huangzp
 * @date 2015-5-11
 */
@Service
public class MessageDBService {
	
	/**
	 * 不存在则插入，存在则更新状态为0-未推送
	 * @param vo
	 * @param toUid
	 * @return
	 * @throws ServiceException
	 */
	public String getInsertOrUpdateSql(SocketDataVo vo, String toUid) throws ServiceException{
		JSONObject data = vo.getData();
		String msg = vo.toJsonString().replace("'", "\\'"); //消息需要进行特别字符转义，以免造成sql语句出问题
		String sql = null;
		
		sql = String.format("INSERT INTO im_msg.t_msg_base (fromUid, toUid, msgId, msg, time) VALUE('%s', '%s', '%s', '%s', '%s') ON DUPLICATE KEY UPDATE status=%s;", 
				data.getJSONObject("user").getString("uid"), toUid, data.getString("msgid"), msg, data.getString("datetime"), MessageStatus.STATUS_UNPUSHED.getStatus());
		
		return sql;
	}
	
	/**
	 * 更新消息为未签收状态，若已签收状态则不变动
	 * @param msgId
	 * @return
	 * @throws ServiceException
	 */
	public String getUpdateUnAckSql(String msgId) throws ServiceException{
		String sql = String.format("UPDATE im_msg.t_msg_base SET status=%s WHERE msgId='%s' AND status<>%s;", MessageStatus.STATUS_UNACK.getStatus(), msgId, MessageStatus.STATUS_ACK.getStatus());
		return sql;
	}
	
	/**
	 * 更新消息为已签收状态
	 * @param msgId
	 * @return
	 * @throws ServiceException
	 */
	public String getUpdateAckSql(String msgId) throws ServiceException{
		String sql = String.format("UPDATE im_msg.t_msg_base SET status=%s WHERE msgId='%s';", MessageStatus.STATUS_ACK.getStatus(), msgId);
		return sql;
	}
	
	/**
	 * 更新消息为回滚状态，若已签收状态则不变动
	 * @param msgId
	 * @return
	 * @throws ServiceException
	 */
	public String getBollackSql(String msgId) throws ServiceException{
		String sql = String.format("UPDATE im_msg.t_msg_base SET status=%s WHERE msgId='%s' AND status<>%s;", MessageStatus.STATUS_ROLLBACK.getStatus(), msgId, MessageStatus.STATUS_ACK.getStatus());
		return sql;
	}

}
