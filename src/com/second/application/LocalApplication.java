package com.second.application;

import com.example.second.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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
		instance = this;
		DisplayImageOptions options = new  DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.showImageOnLoading(R.drawable.empty_photo)
				.showImageForEmptyUri(R.drawable.empty_photo)
				.showImageOnFail(R.drawable.empty_photo).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
				.memoryCacheSize(2*1024*1024)
				.discCacheSize(50*1024*1024)
				.defaultDisplayImageOptions(options).build();
		ImageLoader.getInstance().init(config);
		
		// 得到屏幕的宽和高
		DisplayMetrics dm = getResources().getDisplayMetrics();
		screenH = dm.heightPixels;
		screenW = dm.widthPixels;
		super.onCreate();
	}

}
