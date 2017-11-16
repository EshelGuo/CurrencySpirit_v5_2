package com.eshel.viewmodel;

import com.eshel.currencyspirit.CurrencySpiritApp;
import com.eshel.database.dao.EssenceHistoryDao;
import com.eshel.database.table.EssenceHistory;
import com.eshel.model.EssenceModel;
import com.eshel.net.RetrofitUtil;
import com.eshel.net.api.ApiUtil;
import com.eshel.net.api.NewListApi;
import com.eshel.net.api.StringCallback;
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
	public static void getEssenceDataFromHistory(final Mode mode){
		final long ago = System.currentTimeMillis();
		new Thread(new Runnable() {
			@Override
			public void run() {
				EssenceModel.transitionData(EssenceHistoryDao.queryAll());
				long now = System.currentTimeMillis();
				long time = now - ago;
				if(time > refreshTime)
					time = refreshTime;
				if(mode != Mode.REFRESH){
					time = 0;
				}
				CurrencySpiritApp.postDelayed(new Runnable() {
					@Override
					public void run() {
						EssenceModel.notifyHistoryActivity(mode);
					}
				},refreshTime - time);
			}
		}).start();
	}

	public static void getEssenceData(final Mode mode ){
		Call<ResponseBody> essence = RetrofitUtil.createApi().essence(start,count);
		RetrofitUtil.enqueueCall(essence, new StringCallback() {
			@Override
			public void onSuccess(String result,long time) {
				try {
//					DiskCache.cacheToDisk(CacheType.Type_Essence,start,json);
					refreshView(result, mode, time);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onFailed(String errMsg,long time) {
				EssenceModel.notifyView(mode,false);
				Log.e(EssenceViewModel.class, "Essence 精华数据加载失败, msg: "+errMsg);
			}
		});
	}

	private static void refreshView(String json, final Mode mode, long time) {
		Gson gson = new Gson();
		final ArrayList<EssenceModel> data = gson.fromJson(json, new TypeToken<ArrayList<EssenceModel>>() {}.getType());
		start += count;
		long refreshTime = 0;
		if(mode == Mode.REFRESH){
			// 计算等待时间
			refreshTime = EssenceViewModel.refreshTime - time;
			if(refreshTime < 0)
				refreshTime = 0;
		}
		CurrencySpiritApp.postDelayed(new Runnable() {
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
