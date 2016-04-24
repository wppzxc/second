package com.second.application;

import android.util.DisplayMetrics;

public class LocalApplication extends BaseApplication {

	private static LocalApplication instance;
	public int screenW = 0;
	public int screenH = 0;

	public static LocalApplication getInstance() {
		if (instance == null) {
			instance = new LocalApplication();
		}
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		instance = this;

		// 得到屏幕的宽和高
		DisplayMetrics dm = getResources().getDisplayMetrics();
		screenH = dm.heightPixels;
		screenW = dm.widthPixels;
	}

}
