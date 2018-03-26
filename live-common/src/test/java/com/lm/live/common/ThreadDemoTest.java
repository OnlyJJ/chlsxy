package com.lm.live.common;

import com.lm.live.common.thread.ThreadManager;

/**
 * 线程池管理类测试
 * @author shao.xiang
 * @Company lm
 * @data 2018年3月26日
 */
public class ThreadDemoTest {

	public static void main(String[] args) {
		ThreadDemo t = new ThreadDemo();
		t.setArg0("arg0");
		t.setArg1("arg1");
		ThreadManager.getInstance().execute(t);
	}
}
