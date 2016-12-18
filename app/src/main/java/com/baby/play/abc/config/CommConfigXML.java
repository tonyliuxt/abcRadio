package com.baby.play.abc.config;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * xml配置文件读写类
 * @author Administrator
 *
 */
public class CommConfigXML extends Application
{
	private static final String xmlFileName  = "ieltsls";
	private static CommConfigXML self = null;
	private static Context context = null;
	private static SharedPreferences config = null;
	private SharedPreferences.Editor editor = null;
	
	public static CommConfigXML getInstance(Context ctx)
	{
		if(self == null)
		{
			self = new CommConfigXML();
			context = ctx;
			config = context.getSharedPreferences(xmlFileName,0);
		}
		return self;
	}
	
	/**
	 * 向配置文件写配置内容
	 * @param name
	 * @param value
	 */
	public void setValueByName(String name, String value)
	{
		editor = config.edit();
		editor.putString(name,value);
		editor.commit();
	}
	
	/**
	 * 根据名称返回单个值
	 * @param name
	 * @return
	 */
	public String getValueByName(String name, String defaultValue)
	{
		return config.getString(name, defaultValue);
	}
}
