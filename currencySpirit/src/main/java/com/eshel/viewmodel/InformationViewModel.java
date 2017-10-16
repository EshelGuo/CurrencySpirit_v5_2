package com.eshel.viewmodel;

import com.eshel.currencyspirit.CurrencySpiritApp;
import com.eshel.model.InformationModel;
import com.eshel.net.api.NewListApi;
import com.eshel.net.factory.RetrofitFactory;
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
	static int start = 0;
	static int count = 20;

	static long refreshTime = 2000;
	public static void getInformationData(final Mode mode ){
		final long ago = System.currentTimeMillis();
		NewListApi newListApi = RetrofitFactory.getRetrofit().create(NewListApi.class);
		Call<ResponseBody> information = newListApi.information(start, count);
		information.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				if(response.isSuccessful()){
					try {
						String json = response.body().string();
						Gson gson = new Gson();
						final ArrayList<InformationModel> data =
								gson.fromJson(json, new TypeToken<ArrayList<InformationModel>>() {}.getType());
						start += count;
						long refreshTime;
						if(mode == Mode.REFRESH){
							refreshTime = InformationViewModel.refreshTime - getTimeDifference(ago);
							if(refreshTime < 0)
								refreshTime = 0;
						}else {
							refreshTime = 0;
						}
						CurrencySpiritApp.getApp().getHandler().postDelayed(new Runnable() {
							@Override
							public void run() {
								if(mode == Mode.REFRESH)
									InformationModel.informationData.clear();
								else {
									start += count;
								}
								InformationModel.loadDataCount = data.size();
								InformationModel.informationData.addAll(data);
								InformationModel.notifyView(true);
							}
						},refreshTime);

					} catch (IOException e) {
						e.printStackTrace();
					}
				}else {
					String errMsg = "";
					try {
						errMsg = response.errorBody().string();
					} catch (IOException e) {
						e.printStackTrace();
					}
					InformationModel.notifyView(false);
					Log.e(InformationViewModel.class,errMsg);
				}
			}

			@Override
			public void onFailure(Call<ResponseBody> call, Throwable t) {
				InformationModel.notifyView(false);
				Log.i(call.toString());
				t.printStackTrace();
			}
		});
	}
	static long getTimeDifference(long ago){
		long afterTime = System.currentTimeMillis();
		return afterTime - ago;
	}
	public static void refreshData(){
		start = 0;
		getInformationData(Mode.REFRESH);
	}
}
