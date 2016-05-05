package com.second.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

public abstract class SimpleBaseAdapter<T> extends BaseAdapter {

	protected Context c = null;
	protected LayoutInflater layoutInflater = null;
	protected List<T> datas = null;

	public SimpleBaseAdapter(Context c, List<T> datas) {
		this.c = c;
		this.datas = datas;
		layoutInflater = LayoutInflater.from(c);

	}

	public void refreshDatas(List<T> datas) {
		this.datas = datas;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {

		return datas != null ? datas.size() : 0;

	}

	@Override
	public Object getItem(int position) {

		return datas != null ? datas.get(position) : null;

	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}
