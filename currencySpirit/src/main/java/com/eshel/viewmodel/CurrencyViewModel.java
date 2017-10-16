package com.eshel.viewmodel;

import com.eshel.currencyspirit.CurrencySpiritApp;
import com.eshel.currencyspirit.fragment.currency.AOIFragment;
import com.eshel.currencyspirit.fragment.currency.MarketValueFragment;
import com.eshel.model.CurrencyModel;
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
 * createTime: 2017/10/15 20:59
 * desc: TODO
 */

public class CurrencyViewModel {
	static BaseViewModel marketValue = new BaseViewModel() {
		@Override
		public void getData(Mode mode) {
			CurrencyViewModel.getData(marketValue,mode,"volume",true,CurrencyModel.martetValueModel, MarketValueFragment.class);
		}
	};
	static BaseViewModel aoi = new BaseViewModel() {
		@Override
		public void getData(Mode mode) {
			CurrencyViewModel.getData(aoi,mode,"percent",true,CurrencyModel.aoiModel, AOIFragment.class);
		}
	};
	public static BaseViewModel aoi2 = new BaseViewModel() {
		@Override
		public void getData(Mode mode) {
			CurrencyViewModel.getData(aoi,mode,"percent",false,CurrencyModel.aoiModel2, AOIFragment.class);
		}
	};
	public static void getAOIData(Mode mode){
		aoi.getData(mode);
	}
	public static void refreshAOIData(){
		aoi.refreshData();
	}
	public static void getAOI2Data(Mode mode){
		aoi2.getData(mode);
	}
	public static void refreshAOI2Data(){
		aoi2.refreshData();
	}
	/**
	 * 请求市值数据
	 * Call<ResponseBody> currency = newListApi.coinInfo(marketValue.start, marketValue.count,,true,"USD");
	 */
	public static void getMarketValueData(final Mode mode){
		marketValue.getData(mode);
	}
	public static void refreshMarketValueData(){
		marketValue.refreshData();
	}

	public static void getData(final BaseViewModel base , final Mode mode, String sort, boolean desc, final CurrencyModel.BaseModel baseModel, final Class fragmentClass){
		final long ago = System.currentTimeMillis();
		NewListApi newListApi = RetrofitFactory.getRetrofit().create(NewListApi.class);
		Call<ResponseBody> currency = newListApi.coinInfo(base.start, base.count,sort,desc,"USD");
		currency.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				if(response.isSuccessful()){
					try {
						String json = response.body().string();
						refreshView(json, mode, ago,base,baseModel,fragmentClass);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else {
					String errMsg = "";
					try {
						errMsg = response.errorBody().string();
					} catch (Throwable e) {
						e.printStackTrace();
					}
					baseModel.notifyView(mode,false,fragmentClass);
					Log.e(CurrencyViewModel.class, errMsg);
				}
			}
			@Override
			public void onFailure(Call<ResponseBody> call, Throwable t) {
				baseModel.notifyView(mode,false,fragmentClass);
				t.printStackTrace();
			}
		});
	}
	private static void refreshView(String json, final Mode mode, long ago, final BaseViewModel base, final CurrencyModel.BaseModel baseModel, final Class fragmentClass) {
		Gson gson = new Gson();
		final ArrayList<CurrencyModel> data = gson.fromJson(json, new TypeToken<ArrayList<CurrencyModel>>() {}.getType());
		CurrencySpiritApp.getApp().getHandler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if(mode == Mode.REFRESH)
					baseModel.data.clear();
				else {
					base.start += base.count;
				}
				baseModel.loadDataCount = data.size();
				baseModel.data.addAll(data);
				baseModel.notifyView(mode,true,fragmentClass);
			}
		},base.getRefreshTime(mode,ago));
	}
}
