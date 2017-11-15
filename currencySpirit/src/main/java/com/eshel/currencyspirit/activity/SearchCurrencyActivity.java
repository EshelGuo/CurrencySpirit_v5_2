package com.eshel.currencyspirit.activity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eshel.currencyspirit.R;
import com.eshel.currencyspirit.fragment.currency.CurrencyBaseFragment;
import com.eshel.currencyspirit.util.UIUtil;
import com.eshel.currencyspirit.widget.RecycleViewDivider;
import com.eshel.currencyspirit.widget.SearchView;
import com.eshel.currencyspirit.widget.util.Config;
import com.eshel.model.CurrencyModel;
import com.eshel.viewmodel.SearchViewModel;

import java.util.ArrayList;

import baseproject.base.BaseActivity;
import baseproject.util.DensityUtil;
import baseproject.util.NetUtils;
import baseproject.util.StringUtils;
import baseproject.util.ViewUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guoshiwen on 2017/10/19.
 */

public class SearchCurrencyActivity extends BaseActivity {

	@BindView(R.id.btn_search)
	Button mBtnSearch;
	@BindView(R.id.searchView)
	SearchView mSearchView;
	@BindView(R.id.content)
	FrameLayout mContent;
	@BindView(R.id.top_search)
	LinearLayout mTopSearch;
	@BindView(R.id.recycler_view)
	RecyclerView mRecyclerView;
	private TextView mLoadFailedView;
	private View mLoadingView;



	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		getWindow().setBackgroundDrawable(new ColorDrawable(UIUtil.getColor(android.R.color.transparent)));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_currency);
		ButterKnife.bind(this, getContentView());
		showTitle();
		showBack();
		setTitleText(UIUtil.getString(R.string.search));
		mSearchView.setBackgroundResource(R.drawable.search_drawable);
		mBtnSearch.setBackgroundResource(R.drawable.btn_selector);
		mBtnSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				search(v);
			}
		});
		mSearchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH ||
						(event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
					search(v);
					return true;
				}
				return false;
			}
		});
		setSwipeBackEnable(true);
		initRecycleView();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		SearchViewModel.onDestroy();
	}

	private void search(View v) {
		String keyword = mSearchView.getText().toString().trim();
		if (StringUtils.isEmpty(keyword)) {
			UIUtil.toastShort(getString(R.string.search_empty_msg));
			return;
		}
		//让mPasswordEdit获取输入焦点
		mSearchView.getFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		if (!NetUtils.hasNetwork(getApplicationContext())) {
			loadFailed(UIUtil.getString(R.string.no_net));
			return;
		}
		loading();
		SearchViewModel.search(keyword);
	}

	public void loadFailed(String msg) {
		mRecyclerView.setVisibility(View.GONE);
		mContent.setVisibility(View.VISIBLE);
		mLoadFailedView = ViewUtil.getLoadFailedView(this, mLoadFailedView, msg);
		mContent.removeAllViews();
		mContent.addView(mLoadFailedView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
	}

	public void loading() {
		mRecyclerView.setVisibility(View.GONE);
		mContent.setVisibility(View.VISIBLE);
		if (mLoadFailedView == null)
			mLoadingView = ViewUtil.getLoadingView(this);
		mContent.removeAllViews();
		mContent.addView(mLoadingView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
	}

	public void loadSuccess(ArrayList<CurrencyModel> data) {
		mContent.removeAllViews();
		mContent.setVisibility(View.GONE);
		mRecyclerView.setVisibility(View.VISIBLE);
		RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
		if (adapter != null)
			adapter.notifyDataSetChanged();
	}

	private RecyclerView initRecycleView() {
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
		mRecyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL,
				DensityUtil.dp2px(Config.dividerHeight), UIUtil.getColor(R.color.dividerColor),
				DensityUtil.dp2px(10), DensityUtil.dp2px(10)));
		mRecyclerView.setAdapter(new SearchAdapter());
		mRecyclerView.setVisibility(View.GONE);
		return mRecyclerView;
	}

	private static class SearchAdapter extends RecyclerView.Adapter<CurrencyBaseFragment.BaseViewHolder> {

		@Override
		public CurrencyBaseFragment.BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			if (BaseActivity.getTopActivity() != null)
				return new CurrencyBaseFragment.BaseViewHolder(BaseActivity.getTopActivity());
			return null;
		}

		@Override
		public void onBindViewHolder(CurrencyBaseFragment.BaseViewHolder holder, int position) {
			holder.bindDataToView(SearchViewModel.data.get(position));
		}

		@Override
		public int getItemCount() {
			if (SearchViewModel.data != null)
				return SearchViewModel.data.size();
			return 0;
		}
	}
}
