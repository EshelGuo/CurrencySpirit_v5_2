package baseproject.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.eshel.currencyspirit.R;
import com.eshel.currencyspirit.util.UIUtil;
import com.eshel.viewmodel.BaseViewModel;

import static com.eshel.currencyspirit.R.id.tv_failed;

/**
 * 项目名称: BaseProject
 * 创建人: Eshel
 * 创建时间:2017/10/2 14时42分
 * 描述: TODO
 */

public abstract class BaseFragment extends Fragment{
	protected boolean onViewCreated = false;
	private FrameLayout mControl;
	private LoadState mState;
	private LoadState stateChanged;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if(mControl == null) {
			mControl = new FrameLayout(getActivity());
			mControl.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//			setInitView();
			changeState(LoadState.StateLoading);
		}
		onViewCreated = true;
		if(stateChanged != null){
			LoadState tempState = stateChanged;
			stateChanged = null;
			changeState(tempState);
		}
		if(mState != null)
			changeState(mState);
		return mControl;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		onViewCreated = false;
		super.onDestroyView();
	}

	private void setInitView(){
		TextView tv = new TextView(getActivity());
		tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		tv.setGravity(Gravity.CENTER);
		tv.setText(getTitle());
		mControl.removeAllViews();
		mControl.addView(tv);
	}
	private void setLoadingView(){
		View loadingView = View.inflate(getActivity(), R.layout.fragment_loading, null);
		TextView tvLoading = (TextView) loadingView.findViewById(R.id.tv_loading);
		tvLoading.setTextColor(UIUtil.getColor(R.color.black));
		final View view = loadingView.findViewById(R.id.spin_kit);
		ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
		int width = UIUtil.getScreenWidth() / 10;
		if(layoutParams != null){
			layoutParams.width = width;
			layoutParams.height = width;
		} else {
			layoutParams = new ViewGroup.LayoutParams(width,width);
		}
		view.setLayoutParams(layoutParams);
		mControl.removeAllViews();
		mControl.addView(loadingView);
	}
	private void setLoadFailedView(){
		View loadFailedView = View.inflate(getActivity(),R.layout.fragment_load_failed,null);
		TextView tv_failed = (TextView) loadFailedView.findViewById(R.id.tv_failed);
		tv_failed.setTextColor(UIUtil.getColor(R.color.black));
		tv_failed.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				changeState(LoadState.StateLoading);
				reloadData();
			}
		});
		mControl.removeAllViews();
		mControl.addView(loadFailedView);
	}

	protected abstract void reloadData();

	private String getTitle(){
		return getClass().getSimpleName();
	}
	public void changeState(LoadState state){
		if(!onViewCreated) {
			stateChanged = state;
			return;
		}
		mState = state;
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				switch (mState){
					case StateNoLoad:
						setInitView();
						break;
					case StateLoading:
						setLoadingView();
						break;
					case StateLoadFailed:
						setLoadFailedView();
						break;
					case StateLoadSuccess:
						setLoadSuccessView();
						break;
				}
			}
		});
	}
	private void setLoadSuccessView(){
		mControl.removeAllViews();
		mControl.addView(getLoadSuccessView());
	}
	public abstract View getLoadSuccessView();
	public void refreshFailed() {
		UIUtil.toast("刷新失败");
	}
	public void loadModeFailed(){
		UIUtil.toast("加载失败");
	}


	public enum LoadState{
		StateLoading,StateLoadFailed,StateLoadSuccess,StateNoLoad
	}
	public LoadState getCurrState(){
		return mState;
	}
	public abstract void notifyView(BaseViewModel.Mode mode);
}
