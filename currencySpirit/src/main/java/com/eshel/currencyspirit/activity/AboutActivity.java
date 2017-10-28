package com.eshel.currencyspirit.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eshel.currencyspirit.R;
import com.eshel.currencyspirit.util.UIUtil;

import baseproject.base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * createBy Eshel
 * createTime: 2017/10/14 23:56
 * desc: TODO
 */

public class AboutActivity extends BaseActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		setSwipeBackEnable(true);
		showTitle();
		setTitleText(UIUtil.getString(R.string.item_about));
//		System.runFinalization();
	}
}
