package com.second.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.second.R;
import com.second.adapter.ShopHeadAdaper;
import com.second.adapter.ShopItemAdapter;
import com.second.entity.item;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class ShopFragment extends BaseFragment {

	private ListView shop_lv;
	private ImageView shop_class;
	private ImageView shop_search;
	private LinearLayout loading_llyt;
	private int pageSize = 20;
	private int pageIndex = -1;

	private FrameLayout shop_head_view;

	private ViewPager shop_head_vp;

	// 是否为最后一行
	private boolean isLastRaw = false;

	// 是否还有更多数据
	private boolean isMore = true;

	// 是否正在加载数据
	private boolean isLoading = false;

	// 数据源
	private List<item> myDatas = new ArrayList<item>();
	
	private List<item> headList = new ArrayList<item>();

	private ShopItemAdapter adapter;
	
	private ShopHeadAdaper headAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_shop_main, container, false);
		shop_lv = (ListView) v.findViewById(R.id.shop_lv);
		shop_class = (ImageView) v.findViewById(R.id.shop_class);
		shop_search = (ImageView) v.findViewById(R.id.shop_search);
		initParams();
		return v;

	}

	// 加载头部数据
	private void loadHeadData()
	{
		BmobQuery<item> queryHead = new BmobQuery<item>();
		queryHead.addWhereEqualTo("itemCLASS", "head");
		queryHead.findObjects(getActivity(), new FindListener<item>() {

			@Override
			public void onSuccess(List<item> head) {
				if(!head.isEmpty())
				{
					headList.addAll(head);
					headAdapter.notifyDataSetChanged();
					// 初始选中项
					shop_head_vp.setCurrentItem(0);
					
				}
			}
			@Override
			public void onError(int arg0, String arg1) {
				
			}
			
		});
	}
	
	// 加载列表数据
	private void loadListData() {
		isLoading = true;
		pageIndex++;
		BmobQuery<item> queryList = new BmobQuery<item>();

		queryList.setLimit(pageSize);
		queryList.setSkip(pageIndex * pageSize);
		queryList.addWhereNotEqualTo("itemCLASS", "head");
		queryList.findObjects(getActivity(), new FindListener<item>() {

			@Override
			public void onSuccess(List<item> datas) {
				if (!datas.isEmpty()) {
//					myDatas.addAll(datas);
//					if (adapter != null) {
//						adapter.notifyDataSetChanged();
//					} else {
//						shop_lv.setVisibility(View.VISIBLE);
//						adapter = new ShopItemAdapter(context, myDatas);
//						if (myDatas.size() >= pageSize) {
//							shop_lv.addFooterView(loading_llyt);
//						}
//						shop_lv.setAdapter(adapter);
//					}
					if(pageIndex == 0)
					{
						// 移除底部加载布局
						if(datas.size()<pageSize)
						{
							shop_lv.removeFooterView(loading_llyt);
						}
					}
					myDatas.addAll(datas);
					adapter.notifyDataSetChanged();
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

		// 头部布局
		shop_head_view = (FrameLayout) getLayoutInflater(null).inflate(R.layout.shop_head_view, null);
		shop_head_vp = (ViewPager) shop_head_view.findViewById(R.id.shop_head_vp);
		
		// 初始化推荐图片
		headAdapter = new ShopHeadAdaper(context, headList);
		shop_head_vp.setAdapter(headAdapter);
		
		// 初始化列表
		adapter = new ShopItemAdapter(context, myDatas);
		// 增加头部布局
		shop_lv.addHeaderView(shop_head_view);
		// 增加底部布局
		shop_lv.addFooterView(loading_llyt);
		// 绑定适配器
		shop_lv.setAdapter(adapter);
		
		// 加载数据
		loadHeadData();
		loadListData();
		shop_lv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

				if (isLastRaw && scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					if (!isLoading && isMore) {
						loadListData();
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
