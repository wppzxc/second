package com.second.adapter;

import java.util.List;

import com.example.second.R;
import com.second.entity.item;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ShopHeadAdaper extends PagerAdapter {

	private List<item> datas;

	private Context context;
	private LayoutInflater layoutInflater;

	public ShopHeadAdaper(Context context, List<item> datas) {
		this.datas = datas;
		this.context = context;
		layoutInflater = LayoutInflater.from(context);

	}

	public void refreshDatas(List<item> datas) {
		this.datas = datas;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		View layout = layoutInflater.inflate(R.layout.shop_head_item, null);
		ImageView viewpager_item_img = (ImageView) layout.findViewById(R.id.viewpager_item_img);

		ImageLoader.getInstance().displayImage(datas.get(position).getItem_iv_img().getFileUrl(context),
				viewpager_item_img);
		((ViewPager) container).addView(layout);
		return layout;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
}
