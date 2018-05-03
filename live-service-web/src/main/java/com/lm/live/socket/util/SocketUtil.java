package com.lm.live.socket.util;


import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import com.lm.live.common.thread.ThreadManager;
import com.lm.live.common.utils.ByteUtil;
import com.lm.live.common.utils.GZipUtil;
import com.lm.live.common.utils.LogUtil;
import com.lm.live.common.utils.StrUtil;
import com.lm.live.socket.SocketClient;
import com.lm.live.socket.SocketRestartThread;


public class SocketUtil {
	public static int SEQID;
	
	/** 消息序列号 */
	public static int getSeqId() {
		return SEQID++;
	}
	
	private SocketUtil() {}
	
	private static Socket getSocket() {
		return SocketClient.getInstance();
	}
	
	
	// 向IM服务端程序发送数据(采用指定的压缩方法)
	public static void sendToIm(String msg)  throws Exception{
		DataOutputStream os = null;
		try {
			if(!StrUtil.isNullOrEmpty(msg)) {
				msg=msg.replaceAll("\n|\r|\t|\b|\f", "");
				byte[] body = GZipUtil.compressToByte(msg);
				byte[] head = ByteUtil.toByteArray(body.length, 4);
				byte[] data = new byte[body.length+head.length];

				System.arraycopy(head, 0, data, 0, head.length);
				System.arraycopy(body, 0, data, head.length, body.length);
				os = new DataOutputStream(getSocket().getOutputStream());
				os.write(data);
				os.flush();
			}
		} catch(Exception e) {
			if(os != null) {
				os.close();
			}
			// 启动一个线程去重连
			synchronized(SocketUtil.class) {
				SocketRestartThread task = new SocketRestartThread();
				ThreadManager.getInstance().execute(task);
			}
			throw e;
		}
	}
	/**
	 * 接受IM
	 * @return
	 */
	public static void recieve() throws Exception {
		DataInputStream is = null;
		try {
			is = new DataInputStream(getSocket().getInputStream());
			String msg = getDataBody(is); // 暂时不处理
		} catch (Exception e) {
			if(is != null) {
				is.close();
			}
			LogUtil.log.error(e.getMessage() ,e);
			throw e;
		}
	}
	
	/**
	 * 解socket数据包体
	 * @param is
	 * @return
	 * @throws IOException 
	 */
	public static String getDataBody(InputStream is) throws IOException {
		String dataBody = null;
		// 获取头部
		byte[] head = getData(is, 4);
		int dataLength = ByteUtil.toInt(head);
		// 获取数据
		byte[] data = getData(is, dataLength);
		dataBody = GZipUtil.uncompressToString(data);
		return dataBody;
	}
	
	/**
	 * 拆包
	 * @param is
	 * @param length
	 * @return
	 * @throws IOException
	 */
	private static byte[] getData(InputStream is, int length) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[5120];
		int nIdx = 0; //累计读取了多少位
		int nReadLen = 0; //一次读取了多少位
		while (nIdx < length) { //循环读取足够长度的数据
			if(length - nIdx >= buffer.length){ //剩余数据大于缓存，则全部读取
				nReadLen = is.read(buffer);
			}else{ //剩余数据小于缓存，则注意拆分其他包，只取当前包剩余数据
				nReadLen = is.read(buffer, 0, length - nIdx);
			}
			if (nReadLen > 0) {
				baos.write(buffer, 0, nReadLen);
				nIdx = nIdx + nReadLen;
			} else {
				break;
			}
		}
		return baos.toByteArray();
	}
	
	public static void close()  throws Exception{
		 try {
			 if(getSocket()!=null && getSocket().isConnected()){
				 getSocket().close();
			 }
		} catch (IOException e) {
			LogUtil.log.error(e.getMessage() ,e);
			throw e;
		}
	}
	
}
