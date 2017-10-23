package com.eshel.currencyspirit.activity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.eshel.currencyspirit.R;
import com.eshel.currencyspirit.util.UIUtil;
import com.eshel.currencyspirit.widget.SearchView;

import baseproject.base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guoshiwen on 2017/10/19.
 */

public class SearchCurrencyActivity extends BaseActivity {

	@BindView(R.id.tv_title)
	TextView mTvTitle;
	@BindView(R.id.btn_search)
	Button mBtnSearch;
	@BindView(R.id.searchView)
	SearchView mSearchView;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		getWindow().setBackgroundDrawable(new ColorDrawable(UIUtil.getColor(android.R.color.transparent)));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_currency);
		ButterKnife.bind(this);
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
	}
	private void search(View v){
		//让mPasswordEdit获取输入焦点
		mSearchView.getFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
}
