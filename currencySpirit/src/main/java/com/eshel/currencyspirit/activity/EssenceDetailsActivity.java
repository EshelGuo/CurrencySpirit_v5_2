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
import com.eshel.model.EssenceModel;

import baseproject.base.WebActivity;
import baseproject.util.DensityUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guoshiwen on 2017/10/12.
 */

public class EssenceDetailsActivity extends WebActivity {
	public static String key = "essenceModel";
	private EssenceModel mEssenceModel;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showTitle();
		setTitleText(UIUtil.getString(R.string.item_essence));
		Intent intent = getIntent();
		if (intent != null) {
			mEssenceModel = (EssenceModel) intent.getSerializableExtra(key);
		}
		if (mEssenceModel.url != null) {
			loadUrl(mEssenceModel.url);
		} else {
			UIUtil.toast("加载失败");
		}
	}

	@Override
	public View initTitleView() {
		return null;
	}
}
