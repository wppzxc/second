package com.second.activity;

import com.example.second.R;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import fragment.FriendFragment;
import fragment.SelfFragment;
import fragment.ShopFragment;

public class MainActivity extends BaseActivity {

	LinearLayout second_llyt_shop;
	LinearLayout second_llyt_friend;
	LinearLayout second_llyt_self;
	ImageView second_iv_shop;
	ImageView second_iv_friend;
	ImageView second_iv_self;

	private int chooseIndex = -1;
	private long firstTime = 0;

	private FragmentTransaction ft;

	// 控件绑定
	private void initViews() {
		second_llyt_shop = (LinearLayout) findViewById(R.id.second_llyt_shop);
		second_llyt_friend = (LinearLayout) findViewById(R.id.second_llyt_friend);
		second_llyt_self = (LinearLayout) findViewById(R.id.second_llyt_self);
		second_iv_shop = (ImageView) findViewById(R.id.second_iv_shop);
		second_iv_friend = (ImageView) findViewById(R.id.second_iv_friend);
		second_iv_self = (ImageView) findViewById(R.id.second_iv_self);

	}

	@Override
	protected void onCreate(Bundle saveInstanceState) {
		super.onCreate(saveInstanceState);
		initViews();
		initListeners();
		if (saveInstanceState == null) {
			onClick(second_llyt_shop);
		}

	}

	// 监听器绑定
	private void initListeners() {

		second_llyt_shop.setOnClickListener(this);
		second_llyt_friend.setOnClickListener(this);
		second_llyt_self.setOnClickListener(this);
	}

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

	private void tabBgChange(int index) {
		switch (index) {
		case 0:
			second_iv_shop.setSelected(true);
			second_iv_friend.setSelected(false);
			second_iv_self.setSelected(false);

			break;
		case 1:
			second_iv_shop.setSelected(false);
			second_iv_friend.setSelected(true);
			second_iv_self.setSelected(false);

			break;

		case 2:
			second_iv_shop.setSelected(false);
			second_iv_friend.setSelected(false);
			second_iv_self.setSelected(true);

			break;

		}

	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_main;
	}

	@Override
	protected void initParams() {
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
