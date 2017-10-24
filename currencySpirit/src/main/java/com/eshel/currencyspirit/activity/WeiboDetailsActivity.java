package com.eshel.currencyspirit.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eshel.currencyspirit.R;
import com.eshel.currencyspirit.util.UIUtil;
import com.eshel.model.InformationModel;
import com.eshel.net.Url;

import baseproject.base.WebActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guoshiwen on 2017/10/12.
 */

public class WeiboDetailsActivity extends WebActivity {
	public static String key = "informationModel";
	private InformationModel mInformationModel;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showTitle();
		setTitleText(UIUtil.getString(R.string.item_information));
		Intent intent = getIntent();
		if (intent != null) {
			mInformationModel = (InformationModel) intent.getSerializableExtra(key);
			loadUrl(Url.getWeiboUrl(mInformationModel.wbid));
		}
	}

	@Override
	public View initTitleView() {
		return null;
	}
}
