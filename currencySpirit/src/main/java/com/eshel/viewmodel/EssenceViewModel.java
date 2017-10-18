package com.eshel.viewmodel;

import com.eshel.currencyspirit.CurrencySpiritApp;
import com.eshel.database.dao.EssenceHistoryDao;
import com.eshel.database.table.EssenceHistory;
import com.eshel.model.EssenceModel;
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
 * createBy Eshel
 * createTime: 2017/10/5 03:29
 * desc: TODO
 */

public class EssenceViewModel {
	static int start = 0;
	static int count = 20;

	static long refreshTime = 2000;
	public static void getEssenceDataFromHistory(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				EssenceModel.transitionData(EssenceHistoryDao.queryAll());
				CurrencySpiritApp.getApp().getHandler().post(new Runnable() {
					@Override
					public void run() {
						EssenceModel.notifyHistoryActivity();
					}
				});
			}
		}).start();
	}

	public static void getEssenceData(final Mode mode ){
		final long ago = System.currentTimeMillis();
		NewListApi newListApi = RetrofitFactory.getRetrofit().create(NewListApi.class);
		Call<ResponseBody> essence = newListApi.essence(start, count);
		essence.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				if(response.isSuccessful()){
					try {
						String json = response.body().string();
//						DiskCache.cacheToDisk(CacheType.Type_Essence,start,json);
						refreshView(json, mode, ago);
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
//					String json = DiskCache.getJsonFromDiskCache(CacheType.Type_Essence, start);
//					Log.i(json);
					/*if(!StringUtils.isEmpty(json)) {
						refreshView(json,mode,ago);
					}else {*/
					EssenceModel.notifyView(mode,false);
					Log.e(EssenceViewModel.class, errMsg);
				}
			}

			@Override
			public void onFailure(Call<ResponseBody> call, Throwable t) {
//				String json = DiskCache.getJsonFromDiskCache(CacheType.Type_Essence, start);
//				Log.i(EssenceViewModel.class,json);
				/*if(!StringUtils.isEmpty(json)) {
					refreshView(json,mode,ago);
				}else {*/
				EssenceModel.notifyView(mode,false);
//				Log.i(call.toString());
				t.printStackTrace();
			}
		});
	}

	private static void refreshView(String json, final Mode mode, long ago) {
		Gson gson = new Gson();
		final ArrayList<EssenceModel> data = gson.fromJson(json, new TypeToken<ArrayList<EssenceModel>>() {
		}.getType());
		start += count;
		long refreshTime;
		if(mode == Mode.REFRESH){
			refreshTime = EssenceViewModel.refreshTime - BaseViewModel.getTimeDifference(ago);
			if(refreshTime < 0)
				refreshTime = 0;
		}else {
			refreshTime = 0;
		}
		CurrencySpiritApp.getApp().getHandler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if(mode == Mode.REFRESH || mode == Mode.NORMAL)
					EssenceModel.essenceData.clear();
				else {
					start += count;
				}
				EssenceModel.loadDataCount = data.size();
				EssenceModel.essenceData.addAll(data);
				EssenceModel.notifyView(mode,true);
			}
		},refreshTime);
	}

	public static void refreshData(){
		start = 0;
		getEssenceData(Mode.REFRESH);
	}
	public static void addDataToHistory(EssenceModel model){
		EssenceHistory history = EssenceHistoryDao.queryByUrl(model.url);
		if(history == null)
			EssenceHistoryDao.add(model);
		else {
			EssenceHistoryDao.update(model);
		}
	}
}
