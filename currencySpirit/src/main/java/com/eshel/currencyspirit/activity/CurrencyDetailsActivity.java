package com.eshel.currencyspirit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.eshel.currencyspirit.R;
import com.eshel.model.CurrencyModel;

import java.io.Serializable;

import baseproject.base.WebActivity;

/**
 * Created by guoshiwen on 2017/10/16.
 */

public class CurrencyDetailsActivity extends WebActivity{
	public static String key = "currency";
	private CurrencyModel mCurrencyModel;

	@Override
	public View initTitleView() {
		View title = View.inflate(this, R.layout.title_currency_details,null);
		return title;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if(intent != null){
			mCurrencyModel = (CurrencyModel) intent.getSerializableExtra(key);
			loadUrl(mCurrencyModel.url);
		}
	}
}
