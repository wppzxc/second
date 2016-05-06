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

	// �Ƿ�Ϊ���һ��
	private boolean isLastRaw = false;

	// �Ƿ��и�������
	private boolean isMore = true;

	// �Ƿ����ڼ�������
	private boolean isLoading = false;

	// ����Դ
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

	// ����ͷ������
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
					// ��ʼѡ����
					shop_head_vp.setCurrentItem(0);
					
				}
			}
			@Override
			public void onError(int arg0, String arg1) {
				
			}
			
		});
	}
	
	// �����б�����
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
						// �Ƴ��ײ����ز���
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
					Toast.makeText(getActivity(), "��û�и�������", Toast.LENGTH_SHORT).show();
				}
				isLoading = false;
			}

			@Override
			public void onError(int arg0, String arg1) {

				shop_lv.setVisibility(View.GONE);
				Toast.makeText(getActivity(), "����ʧ�ܣ��������������", Toast.LENGTH_SHORT).show();

			}
		});
	}

	@Override
	protected void initParams() {
		// �ײ�����
		loading_llyt = (LinearLayout) getLayoutInflater(null).inflate(R.layout.listview_loading_view, null);

		// ͷ������
		shop_head_view = (FrameLayout) getLayoutInflater(null).inflate(R.layout.shop_head_view, null);
		shop_head_vp = (ViewPager) shop_head_view.findViewById(R.id.shop_head_vp);
		
		// ��ʼ���Ƽ�ͼƬ
		headAdapter = new ShopHeadAdaper(context, headList);
		shop_head_vp.setAdapter(headAdapter);
		
		// ��ʼ���б�
		adapter = new ShopItemAdapter(context, myDatas);
		// ����ͷ������
		shop_lv.addHeaderView(shop_head_view);
		// ���ӵײ�����
		shop_lv.addFooterView(loading_llyt);
		// ��������
		shop_lv.setAdapter(adapter);
		
		// ��������
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
