package com.second.application;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;;;

public abstract class BaseApplication extends Application
{
	public static final String TAG = "Application";
	
	public static Context applicationContext;
	
	//以键值对的形式存储账号和密码
	public SharedPreferences sharedpreferences;
	@Override
	public void onCreate()
	{
		super.onCreate();
		//初始化键值对存储
		
		sharedpreferences = getSharedPreferences("local_kv", MODE_PRIVATE);
		
	}
}
