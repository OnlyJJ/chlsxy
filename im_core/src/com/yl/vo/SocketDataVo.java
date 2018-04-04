package com.yl.vo;

import java.io.Serializable;
import java.util.Map;

import net.sf.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yl.common.constant.ErrorCode;
import com.yl.common.constant.MessageFunID;
import com.yl.common.exception.ServiceException;
import com.yl.common.utils.JsonUtil;

/**
 * socket通信数据包bean
 * @author huangzp
 * @date 2015-4-7
 */
public class SocketDataVo implements Serializable {

	@JsonIgnore
	private static final long serialVersionUID = -2948982136529721262L;

	private int funID; //消息功能号
	
	private int seqID; //内容长度
	 
	private JSONObject data; //内容主体
	
	@JsonIgnore
	private static int i=1;
	
	@JsonIgnore
	private SocketDataVo() {} //禁止无参构造

	@JsonIgnore
	public SocketDataVo(String msg) throws ServiceException {
		
		//LogUtil.log.info("接收：msg＝"+ msg);

		//msg=msg.replace ("用户:", "用户");
		//LogUtil.log.info("接收：msg＝"+ msg);
		JSONObject json = JsonUtil.strToJsonObject(msg);
		//LogUtil.log.info("接收：json＝"+ json.toString());
		if(null != json){
			this.funID = (null==json.get("funID")) ? MessageFunID.FUNID_11001.getFunID() : json.getInt("funID");
			this.seqID = (null==json.get("seqID")) ? 1 : json.getInt("seqID");
			
			this.data = (null==json.get("data")) ? null : json.getJSONObject("data");
			//this.data = (null==json.get("data")) ? json : json.getJSONObject("data");
			
			//if(-1==length || null==data){
			//	throw new ServiceException(ErrorCode.ERROR_5003);
			//}
		}else{
			throw new ServiceException(ErrorCode.ERROR_5003);
		}
		
	}
	
	@JsonIgnore
	public SocketDataVo(MessageFunID funID, Map<String, Object> data) throws ServiceException{
		this.funID = funID.getFunID();
		this.data = JsonUtil.beanToJsonObject(data);
		
		this.data.put("datetime", System.currentTimeMillis());
		
		setSeqID();
	}
	
	@JsonIgnore
	public String toJsonString() throws ServiceException{
		return JsonUtil.beanToJsonString(this);
	}

	public int getFunID() {
		return funID;
	}

	public int getSeqID()
	{
		return seqID;
	}

	public void setSeqID(int seqID)
	{
        if (seqID >= 0x7fffffff)
        	seqID = 1;
        this.seqID = seqID;
	}
	
	@JsonIgnore
    public void setSeqID()
    {
    	seqID = i;
        i++;
        if (i == 0x7fffffff)
            i = 1;
    }

    @JsonIgnore
    public static void initSeqID(int num)
    {
        i = num;
    }

	public JSONObject getData() {
		return data;
	}

	public void setFunID(int funID)
	{
		this.funID = funID;
	}

	
}
