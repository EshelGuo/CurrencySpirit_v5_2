package com.eshel.viewmodel;

import com.bumptech.glide.load.Encoder;
import com.eshel.currencyspirit.R;
import com.eshel.currencyspirit.activity.SearchCurrencyActivity;
import com.eshel.currencyspirit.util.UIUtil;
import com.eshel.model.CurrencyModel;
import com.eshel.net.api.ApiUtil;
import com.eshel.net.api.StringCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.URLEncoder;
import java.util.ArrayList;

import baseproject.base.BaseActivity;

/**
 * Created by guoshiwen on 2017/11/15.
 */

public class SearchViewModel {
	public static ArrayList<CurrencyModel> data;
	public static void search(String keyword){
		ApiUtil.search(keyword, new StringCallback() {
			@Override
			public void onSuccess(String result) {
				Gson gson = new Gson();
				data = gson.fromJson(result,new TypeToken<ArrayList<CurrencyModel>>(){}.getType());
				//失败
				if(BaseActivity.getTopActivity() != null && BaseActivity.getTopActivity() instanceof SearchCurrencyActivity){
					SearchCurrencyActivity activity = (SearchCurrencyActivity) BaseActivity.getTopActivity();
					if(data == null || data.size() == 0)
						activity.loadFailed(UIUtil.getString(R.string.did_not_find));
					else {
						activity.loadSuccess(data);
					}
				}
			}

			@Override
			public void onFailed(String errMsg) {
				if(BaseActivity.getTopActivity() != null && BaseActivity.getTopActivity() instanceof SearchCurrencyActivity){
					SearchCurrencyActivity activity = (SearchCurrencyActivity) BaseActivity.getTopActivity();
					activity.loadFailed(UIUtil.getString(R.string.unknown_err));
				}
			}
		});
	}
	public static void onDestroy(){
		data = null;
	}
}
