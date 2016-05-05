package com.second.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.second.R;
import com.second.adapter.ShopItemAdapter;
import com.second.entity.item;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;

public class ShopFragment extends BaseFragment {

	private ListView shop_lv;
	private ImageView shop_class;
	private ImageView shop_search;
	private LinearLayout loading_llyt;
	private int pageSize = 20;
	private int pageIndex = -1;

	// 是否为最后一行
	private boolean isLastRaw = false;

	// 是否还有更多数据
	private boolean isMore = true;

	// 是否正在加载数据
	private boolean isLoading = false;

	// 数据源
	private List<item> myDatas = new ArrayList();

	private ShopItemAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_shop_main, container, false);
		shop_lv = (ListView) v.findViewById(R.id.shop_lv);
		shop_class = (ImageView) v.findViewById(R.id.shop_class);
		shop_search = (ImageView) v.findViewById(R.id.shop_search);
		initParams();
		return v;

	}

	private void loadData() {
		isLoading = true;
		pageIndex++;
		BmobQuery<item> query = new BmobQuery<item>();
			
		query.setLimit(pageSize);
		query.setSkip(pageIndex * pageSize);
		query.findObjects(getActivity(), new FindListener<item>() {

			@Override
			public void onSuccess(List<item> datas) {
				if (!datas.isEmpty()) {
					myDatas.addAll(datas);
					if (adapter != null) {
						adapter.notifyDataSetChanged();
					} else {
						shop_lv.setVisibility(View.VISIBLE);
						adapter = new ShopItemAdapter(context, myDatas);
						if (myDatas.size() >= pageSize) {
							shop_lv.addFooterView(loading_llyt);
						}
						shop_lv.setAdapter(adapter);
					}
				} else {
					isMore = false;
					shop_lv.removeFooterView(loading_llyt);
					Toast.makeText(getActivity(), "已没有更多数据", Toast.LENGTH_SHORT).show();
				}
				isLoading = false;
			}

			@Override
			public void onError(int arg0, String arg1) {

				shop_lv.setVisibility(View.GONE);
				Toast.makeText(getActivity(), "请求失败，请检查网络后重试", Toast.LENGTH_SHORT).show();

			}
		});
	}

	@Override
	protected void initParams() {
		// 底部布局
		loading_llyt = (LinearLayout) getLayoutInflater(null).inflate(R.layout.listview_loading_view, null);

		loadData();
		shop_lv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

				if (isLastRaw && scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					if (!isLoading && isMore) {
						loadData();
					}
					isLastRaw = false;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

				if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0) {
					isLastRaw = true;
				}
			}
		});
	}

	@Override
	protected int getLayoutId() {

		return R.layout.fragment_shop_main;
	}

}
