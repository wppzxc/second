package fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

	@SuppressWarnings("unused")
	private Context context;

	@SuppressWarnings("deprecation")
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(getLayoutId(), container, false);
		initParams();
		return view;
	}

	protected abstract void initParams();

	protected abstract int getLayoutId();

}
