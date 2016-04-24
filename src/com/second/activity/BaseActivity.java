package com.second.activity;

import android.support.v4.app.FragmentActivity;
import android.view.View.OnClickListener;
import android.app.Dialog;
import android.os.Bundle;;

public abstract class BaseActivity extends FragmentActivity implements OnClickListener {
	protected Dialog dialog;
	private boolean isCreate = false;

	@Override
	protected void onCreate(Bundle saveInstanceState) {
		super.onCreate(saveInstanceState);
		setContentView(getLayoutId());
		isCreate = true;

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isCreate) {
			isCreate = false;
			initParams();

		}
	}

	protected abstract int getLayoutId();

	protected abstract void initParams();

}
