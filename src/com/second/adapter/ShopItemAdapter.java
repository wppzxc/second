package com.second.adapter;

import java.util.List;

import com.example.second.R;
import com.second.entity.item;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ShopItemAdapter extends SimpleBaseAdapter<item> {
	
	public ShopItemAdapter(Context c, List<item> datas) {
		super(c, datas);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		EntityHolder entityHolder = null;

		
		if (convertView == null) {
			entityHolder = new EntityHolder();

			convertView = layoutInflater.inflate(R.layout.shop_item, null);

			entityHolder.item_tv_title = (TextView) convertView.findViewById(R.id.item_tv_title);
			entityHolder.item_tv_time = (TextView)convertView.findViewById(R.id.item_tv_time);
			entityHolder.item_iv_img = (ImageView)convertView.findViewById(R.id.item_iv_img);
			convertView.setTag(entityHolder);
		} else {
			entityHolder = (EntityHolder) convertView.getTag();
		}
		// 显示标题
		entityHolder.item_tv_title.setText(datas.get(position).itemTITLE);
		
		// 显示创建时间
		entityHolder.item_tv_time.setText(datas.get(position).getCreatedAt().substring(0, 10));
		
		// 使用ImageLoader加载图片
		ImageLoader.getInstance().displayImage(datas.get(position).getItem_iv_img().getFileUrl(c), entityHolder.item_iv_img);
		
		return convertView;
	}

	private class EntityHolder {
		TextView item_tv_title;
		TextView item_tv_time;
		ImageView item_iv_img;
	}

}
