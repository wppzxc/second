package com.second.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.example.second.R;
import com.second.adapter.ShopHeadAdaper;
import com.second.adapter.ShopItemAdapter;
import com.second.entity.item;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
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

	private ImageView shop_tip1;
	private ImageView shop_tip2;
	private ImageView shop_tip3;
	private List<ImageView> shopTipList = new ArrayList<ImageView>();

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
	
	private ScheduledExecutorService scheduledExecutorService;
	private int currentItem = 0;

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
	private void loadHeadData() {
		BmobQuery<item> queryHead = new BmobQuery<item>();
		queryHead.addWhereEqualTo("itemCLASS", "head");
		queryHead.findObjects(getActivity(), new FindListener<item>() {

			@Override
			public void onSuccess(List<item> head) {
				if (!head.isEmpty()) {
					headList.add(head.get(2));
					headList.addAll(head);
					headList.add(head.get(0));
					headAdapter.refreshDatas(headList);
					shop_head_vp.setCurrentItem(1);
					shopTipList.get(0).setSelected(true);
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
					if (pageIndex == 0) {
						// 移除底部加载布局
						if (datas.size() < pageSize) {
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
		shop_tip1 = (ImageView) shop_head_view.findViewById(R.id.shop_tip1);
		shop_tip2 = (ImageView) shop_head_view.findViewById(R.id.shop_tip2);
		shop_tip3 = (ImageView) shop_head_view.findViewById(R.id.shop_tip3);
		shopTipList.add(shop_tip1);
		shopTipList.add(shop_tip2);
		shopTipList.add(shop_tip3);

		// 初始化推荐图片
		headAdapter = new ShopHeadAdaper(context, headList);
		shop_head_vp.setAdapter(headAdapter);
		shop_head_vp.setOnPageChangeListener(new shopOnPageChangeListener());

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

	public class shopOnPageChangeListener implements OnPageChangeListener {

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
				shopTipList.get(0).setSelected(true);
				shopTipList.get(1).setSelected(false);
				shopTipList.get(2).setSelected(false);
				shop_head_vp.setCurrentItem(3, false);
				currentItem = 3;
				break;
			case 1:
				shopTipList.get(0).setSelected(true);
				shopTipList.get(1).setSelected(false);
				shopTipList.get(2).setSelected(false);
				currentItem = arg0;
				break;
			case 2:
				shopTipList.get(0).setSelected(false);
				shopTipList.get(1).setSelected(true);
				shopTipList.get(2).setSelected(false);
				currentItem = arg0;
				break;
			case 3:
				shopTipList.get(0).setSelected(false);
				shopTipList.get(1).setSelected(false);
				shopTipList.get(2).setSelected(true);
				currentItem = arg0;
				break;
			case 4:
				shopTipList.get(0).setSelected(false);
				shopTipList.get(1).setSelected(false);
				shopTipList.get(2).setSelected(true);
				shop_head_vp.setCurrentItem(1, false);
				currentItem = 1;
				break;

			}
		}

	}

	@Override
	public void onStart() {
		super.onStart();
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 5, 5, TimeUnit.SECONDS);
	}

	@Override
	public void onStop() {
		super.onStop();
		if(scheduledExecutorService!=null)
		{
			scheduledExecutorService.shutdown();
		}
	}
	
	private class ScrollTask implements Runnable
	{
		public void run()
		{
			synchronized (shop_head_vp) {
				currentItem ++;
				if(currentItem == 4)
				{
					currentItem = 1;
				}
				handler.sendEmptyMessage(1);
			}
		}
	}
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if(!headList.isEmpty())
				{
					shop_head_vp.setCurrentItem(currentItem);
				}
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected int getLayoutId() {

		return R.layout.fragment_shop_main;
	}

}
