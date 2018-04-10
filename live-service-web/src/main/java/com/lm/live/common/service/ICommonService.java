package com.lm.live.common.service;

/**
 * service基类，各个service需要继承此类
 * @author shao.xiang
 * @date 2017-06-03
 */
public interface ICommonService<R> {

	public R getObjectById(Object id);

	public void insert(R vo);

	public void update(R vo);
	
	public void removeById(Object id);

}
