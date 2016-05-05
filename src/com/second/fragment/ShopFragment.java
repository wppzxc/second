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

	// �Ƿ�Ϊ���һ��
	private boolean isLastRaw = false;

	// �Ƿ��и�������
	private boolean isMore = true;

	// �Ƿ����ڼ�������
	private boolean isLoading = false;

	// ����Դ
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
