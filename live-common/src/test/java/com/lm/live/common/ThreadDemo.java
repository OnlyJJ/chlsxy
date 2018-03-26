package com.lm.live.common;



public class ThreadDemo implements Runnable {
	
	private String arg0;
	private String arg1;

	public ThreadDemo() {
		
	}

	@Override
	public void run() {
		// do biz...
	}

	public String getArg0() {
		return arg0;
	}

	public void setArg0(String arg0) {
		this.arg0 = arg0;
	}

	public String getArg1() {
		return arg1;
	}

	public void setArg1(String arg1) {
		this.arg1 = arg1;
	}

	
}
