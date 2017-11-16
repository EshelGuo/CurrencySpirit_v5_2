package com.eshel.viewmodel;

import com.eshel.currencyspirit.CurrencySpiritApp;
import com.eshel.model.InformationModel;
import com.eshel.net.RetrofitUtil;
import com.eshel.net.api.StringCallback;
import com.eshel.viewmodel.BaseViewModel.Mode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;

import baseproject.util.Log;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by guoshiwen on 2017/10/10.
 */

public class InformationViewModel {
	private static int start = 0;
	private static int count = 20;
	private static long refreshTime = 2000;

	public static void getInformationData(final Mode mode ){
		Call<ResponseBody> information = RetrofitUtil.createApi().information(start, count);
		RetrofitUtil.enqueueCall(information, new StringCallback() {
			@Override
			public void onSuccess(String result, long time) {
				Gson gson = new Gson();
				final ArrayList<InformationModel> data = gson.fromJson(result, new TypeToken<ArrayList<InformationModel>>() {}.getType());
				start += count;
				long refreshTime = 0;
				if(mode == Mode.REFRESH){
					refreshTime = InformationViewModel.refreshTime - time;
					if(refreshTime < 0)
						refreshTime = 0;
				}
				CurrencySpiritApp.postDelayed(new Runnable() {
					@Override
					public void run() {
						if(mode == Mode.REFRESH || mode == Mode.NORMAL)
							InformationModel.informationData.clear();
						else {
							start += count;
						}
						InformationModel.loadDataCount = data.size();
						InformationModel.informationData.addAll(data);
						InformationModel.notifyView(mode,true);
					}
				},refreshTime);
			}

			@Override
			public void onFailed(String errMsg, long time) {
				InformationModel.notifyView(mode, false);
				Log.e(InformationViewModel.class,errMsg);
			}
		});
	}
	public static void refreshData(){
		start = 0;
		getInformationData(Mode.REFRESH);
	}
}
