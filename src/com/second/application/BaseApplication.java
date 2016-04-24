package com.second.application;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;;;

public abstract class BaseApplication extends Application
{
	public static final String TAG = "Application";
	
	public static Context applicationContext;
	
	//�Լ�ֵ�Ե���ʽ�洢�˺ź�����
	public SharedPreferences sharedpreferences;
	@Override
	public void onCreate()
	{
		super.onCreate();
		//��ʼ����ֵ�Դ洢
		
		sharedpreferences = getSharedPreferences("local_kv", MODE_PRIVATE);
		
	}
}
