package com.second.activity;

import cn.bmob.v3.Bmob;
import android.app.Activity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

import com.example.second.R;
import com.second.fragment.FriendFragment;
import com.second.fragment.SelfFragment;
import com.second.fragment.ShopFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

	LinearLayout second_llyt_shop;
	LinearLayout second_llyt_friend;
	LinearLayout second_llyt_self;
	ImageView second_iv_shop;
	ImageView second_iv_friend;
	ImageView second_iv_self;
	ViewPager main_vp;

	private int chooseIndex = -1;
	private long firstTime = 0;

	private FragmentTransaction ft;
	private ShopFragment shopFragment;
	private FriendFragment friendFragment;
	private SelfFragment selfFragment;
	private List<Fragment> fragmentsList = new ArrayList<Fragment>();

	@Override
	protected void onCreate(Bundle saveInstanceState) {
		super.onCreate(saveInstanceState);
		Bmob.initialize(this, "efabecb6ea93f7cda3da00b7cbab75a9");
		initViews();
		initListeners();
		if (saveInstanceState == null) {
			onClick(second_llyt_shop);
		}
	}

	// 控件绑定
	private void initViews() {
		second_llyt_shop = (LinearLayout) findViewById(R.id.second_llyt_shop);
		second_llyt_friend = (LinearLayout) findViewById(R.id.second_llyt_friend);
		second_llyt_self = (LinearLayout) findViewById(R.id.second_llyt_self);
		second_iv_shop = (ImageView) findViewById(R.id.second_iv_shop);
		second_iv_friend = (ImageView) findViewById(R.id.second_iv_friend);
		second_iv_self = (ImageView) findViewById(R.id.second_iv_self);
		main_vp = (ViewPager) findViewById(R.id.main_vp);

	}

	// 监听器绑定
	private void initListeners() {

		second_llyt_shop.setOnClickListener(this);
		second_llyt_friend.setOnClickListener(this);
		second_llyt_self.setOnClickListener(this);
	}

	// 控件点击事件
	@Override
	public void onClick(View v) {

		ft = getSupportFragmentManager().beginTransaction();

		switch (v.getId()) {
		case R.id.second_llyt_shop:

			if (chooseIndex != 0) {
				chooseIndex = 0;
				tabBgChange(chooseIndex);
				ft.replace(R.id.second_flyt_content,
						ShopFragment.instantiate(MainActivity.this, ShopFragment.class.getName(), null),
						"shopfragment");
				ft.commit();
			}
			break;

		case R.id.second_llyt_friend:

			if (chooseIndex != 1) {
				chooseIndex = 1;
				tabBgChange(chooseIndex);
				ft.replace(R.id.second_flyt_content,
						FriendFragment.instantiate(MainActivity.this, FriendFragment.class.getName(), null),
						"friendfragment");
				ft.commit();
			}
			break;

		case R.id.second_llyt_self:

			if (chooseIndex != 2) {
				chooseIndex = 2;
				tabBgChange(chooseIndex);
				ft.replace(R.id.second_flyt_content,
						SelfFragment.instantiate(MainActivity.this, SelfFragment.class.getName(), null),
						"selffragment");
				ft.commit();
			}
			break;

		}

	}

	public class DefineOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				tabBgChange(0);
				break;
			case 1:
				tabBgChange(1);
				break;
			case 2:
				tabBgChange(2);
				break;

			}

		}

	}

	private void tabBgChange(int index) {
		switch (index) {
		case 0:
			second_iv_shop.setSelected(true);
			second_iv_friend.setSelected(false);
			second_iv_self.setSelected(false);
			main_vp.setCurrentItem(0);

			break;
		case 1:
			second_iv_shop.setSelected(false);
			second_iv_friend.setSelected(true);
			second_iv_self.setSelected(false);
			main_vp.setCurrentItem(1);

			break;

		case 2:
			second_iv_shop.setSelected(false);
			second_iv_friend.setSelected(false);
			second_iv_self.setSelected(true);
			main_vp.setCurrentItem(2);

			break;

		}

	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_main;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void initParams() {
		shopFragment = new ShopFragment();
		friendFragment = new FriendFragment();
		selfFragment = new SelfFragment();

		fragmentsList.add(shopFragment);
		fragmentsList.add(friendFragment);
		fragmentsList.add(selfFragment);

		main_vp.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
			@Override
			public int getCount() {
				return fragmentsList.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				return fragmentsList.get(arg0);
			}
		});

		main_vp.setCurrentItem(0);
		main_vp.setOnPageChangeListener(new DefineOnPageChangeListener());
	}

	@Override
	public void onBackPressed() {
		long secondTime = System.currentTimeMillis();

		if (secondTime - firstTime > 1000) {
			Toast.makeText(this, "再按一次退出客户端", Toast.LENGTH_SHORT).show();
			firstTime = secondTime;
		} else {
			finish();
		}

	}

}
