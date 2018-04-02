package com.yl.socket843;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import javax.annotation.PreDestroy;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.yl.common.utils.LogUtil;
import com.yl.common.utils.SpringContextListener;


/**
 * socket服务器 专用返回flash安全策略文件
 * @author hyr
 * @date 2016-3-2
 */
@Scope("singleton")
@Component
public class SocketServer843 extends Thread {
	
	private ServerSocket server;
	private final int port = 8843;//因为权限问题，监听 8843端口,服务器从843端口映射过来
	private final int timeout = Integer.valueOf(SpringContextListener.getContextProValue("socket.timeout", "120"));
	
	/**
	 * 实例化socket服务器
	 * @throws Exception
	 */
	public void startServer() throws Exception{
		LogUtil.log.info(String.format("socket.port=%s, socket.timeout=%s秒" , this.port, this.timeout));
		server = new ServerSocket(port);
		this.start();
	}
	
	@PreDestroy
	public void destory() {
		try {
			//关闭socket服务器
			if (null != server) {
				server.close();
			}
			
			//System.gc();
			LogUtil.log.info("spring容器销毁，socket服务器关闭");
		} catch (Exception e) {
			LogUtil.log.warn(e.getMessage(), e);
		}
	}

	public void run() {
		try {
			while (null != server && !server.isClosed()) {
				Socket client = server.accept();
				client.setTcpNoDelay(true);
				client.setSoTimeout(timeout * 1000); // 超过设置秒数后，无数据上来则超时中断socket连接
				client.setKeepAlive(true); //开启底层的存活检测机制
				LogUtil.log.info(port+"建立socket连接：" + client.getRemoteSocketAddress().toString());
				
				// 每接收到一个Socket就建立一个新的线程来处理它
				new SocketProc(client).start();
			}
		} catch (Exception e) {
			if(!"Socket closed".equals(e.getMessage())){
				LogUtil.log.error(e.getCause(), e);
			}
		}

	}
	
	
    /**
     * 处理线程
     */
	public class SocketProc extends Thread {
		private Socket client;
		private DataInputStream is;
		private DataOutputStream os;
		
		private SocketProc(){} //避免外部无参实例化，导致错误

		public SocketProc(Socket client ) throws Exception{
			this.client = client;
			//is = client.getInputStream();
			//os = client.getOutputStream();
			
			is=new DataInputStream(client.getInputStream());
			os=new DataOutputStream(client.getOutputStream());
		}

		/**
		 * 拆包
		 * @param is
		 * @param length
		 * @return
		 * @throws IOException
		 */
		private byte[] getData(DataInputStream is, int length) throws IOException {
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
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
		
		private byte[] readBytes(DataInputStream in, long length) throws IOException {
			   ByteArrayOutputStream bo = new ByteArrayOutputStream();
			   byte[] buffer = new byte[1024];
			   int read = 0;
			   while (read < length) {
			    int cur = in.read(buffer, 0, (int)Math.min(1024, length - read));
				//for(byte a:buffer)
				//	System.out.print(Integer.toHexString(a&0xff)+",");
				//System.out.println();
			    if (cur < 0) { break; }
			    read += cur;
			    bo.write(buffer, 0, cur);
			   }
			   return bo.toByteArray();
			}
		
		
		/**
		 * 推送数据包到客户端
		 * @param writer
		 * @param content
		 * @throws IOException 
		 */
		private void pushDataToClient_flash(DataOutputStream os, String content) throws IOException{
			LogUtil.log.info(String.format("flash策略文件：content=%s",content));
			byte[] data = content.getBytes();
			os.write(data);
			os.flush();
		}
		
	    /**
	     * 关闭物理层连接
	     */
	    private void close()
	    {
	        try
	        {
	            if (is != null)
	            	is.close();
	        }
	        catch (Exception e)
	        {}
	        try
	        {
	            if (os != null)
	                os.close();
	        }
	        catch (Exception e)
	        {}
	        try
	        {
	            if (client != null)
	            	client.close();
	        }
	        catch (Exception e)
	        {}
	    }
	    
		public void run() {
			try {

				// 获取数据
				//byte[] data = getData(is, 100);//flash取安全策略  <policy-file-request/>
				byte[] data = readBytes(is, 23);//flash取安全策略  <policy-file-request/>
				
				//flash取安全策略  <policy-file-request/>
				String policyrequest="<policy-file-request/>\0";
				String policyresponse = "<cross-domain-policy><site-control permitted-cross-domain-policies=\"all\"/><allow-access-from domain=\"*\" to-ports=\"*\"/></cross-domain-policy>\0";

				byte[] crossdomainpolicy = policyrequest.getBytes();//
				
				
				//for(byte a:data)
				//	System.out.print(Integer.toHexString(a&0xff)+",");
				//System.out.println();
				//for(byte a:crossdomainpolicy)
				//	System.out.print(Integer.toHexString(a&0xff)+",");
				//System.out.println();
				//System.out.println(Arrays.equals(data, crossdomainpolicy));
				
				
				if(Arrays.equals(data, crossdomainpolicy))
				{
					//Thread.currentThread().sleep(100);
					pushDataToClient_flash(os,policyresponse);
				}
				
				close();
			
			} catch (Exception e) {
				try {
					this.close();
				} catch (Exception e1) 
				{
					e1.printStackTrace();
					LogUtil.log.error(e1.getMessage(),e1);
				} 
				e.printStackTrace();
				LogUtil.log.error(e.getMessage(),e);
			} 
		}
	}
}
