package com.yl.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.stereotype.Component;

/**
 * 获取properties配置
 * @author huangzp
 * @date 2015-4-7
 */
@Component
public final class SpringContextListener extends PropertyPlaceholderConfigurer {

	private static Properties context = new Properties();

	/**
	 * 此方法加载多个properties 文件
	 */
	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
		InputStream in = null;
		try {
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream("conf.properties");
			if(in != null ) {
				Properties p = new Properties();
				p.load(in);
				for (Object key : p.keySet()) {
					String keyStr = key.toString().trim();
					String value = p.getProperty(keyStr).trim();
					props.put(keyStr, value);
				}
			}
		} catch (Exception e) {
			LogUtil.log.error(e.getMessage(), e);
		} finally {
			try { 
				if(in != null){
					in.close();
				}
			} catch (IOException e) {}
		}
		
		super.processProperties(beanFactoryToProcess, props);
		this.context = props;
	}

	/**
	 * 获取配置文件的属性值
	 * @param key
	 * @param defaultValue 默认值
	 * @return
	 */
	public static String getContextProValue(String key, String defaultValue) {
		String value = defaultValue;
		if(null != context && context.size() > 0 ){
			value = context.getProperty(key, defaultValue);
		}
		return value;
	}

}
