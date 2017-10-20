package com.eshel.viewmodel;

import com.eshel.config.AppConfig;
import com.eshel.config.AppConstant;
import com.eshel.currencyspirit.CurrencySpiritApp;
import com.eshel.currencyspirit.R;
import com.eshel.currencyspirit.fragment.currency.AOIFragment;
import com.eshel.currencyspirit.fragment.currency.MarketValueFragment;
import com.eshel.currencyspirit.fragment.currency.SelfSelectFragment;
import com.eshel.currencyspirit.util.UIUtil;
import com.eshel.database.dao.CurrencyDao;
import com.eshel.database.table.CurrencyTable;
import com.eshel.model.CurrencyModel;
import com.eshel.net.api.NewListApi;
import com.eshel.net.factory.RetrofitFactory;
import com.eshel.viewmodel.BaseViewModel.Mode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import baseproject.util.Log;
import baseproject.util.NetUtils;
import baseproject.util.StringUtils;
import baseproject.util.shape.ShapeUtil;
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
			CurrencyViewModel.getData(aoi2,mode,"percent",false,CurrencyModel.aoiModel2, AOIFragment.class);
		}
	};
	public static BaseViewModel selfSelect = new BaseViewModel() {
		@Override
		public void getData(final Mode mode) {
			final long ago = System.currentTimeMillis();
			if(mode == Mode.REFRESH)
				selfSelect.start = 0;
			final List<CurrencyTable> currencyTables = CurrencyDao.queryByCount(selfSelect.start,count);
			if((currencyTables == null || currencyTables.size() == 0)){
				selfSelect.start = 0;
				CurrencyModel.selfSelectModel.data.clear();
				CurrencyModel.selfSelectModel.notifyView(mode,true, SelfSelectFragment.class);
				return;
			}
			if(mode == Mode.REFRESH || mode == Mode.NORMAL) {
				CurrencyModel.selfSelectModel.data.clear();
			}
			if(!NetUtils.hasNetwork(UIUtil.getContext())) {
				for (CurrencyTable currencyTable : currencyTables) {
					// TODO: 2017/10/19
					CurrencyModel.selfSelectModel.data.add(currencyTable.get(null));
				}
				CurrencySpiritApp.getApp().getHandler().postDelayed(new Runnable() {
					@Override
					public void run() {
						CurrencyModel.selfSelectModel.loadDataCount = currencyTables.size();
						CurrencyModel.selfSelectModel.notifyView(mode, true, SelfSelectFragment.class);
					}
				}, getRefreshTime(mode, ago));
			}else {
				NewListApi api = RetrofitFactory.getRetrofit().create(NewListApi.class);
				List<String> ids = new ArrayList<>();
				for (CurrencyTable currencyTable : currencyTables) {
					ids.add(currencyTable.coin_id);
				}
				Call<ResponseBody> select = api.select("USD", ids);
				select.enqueue(new Callback<ResponseBody>() {
					@Override
					public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
						if(response.isSuccessful()){
							try {
								String json = response.body().string();
								refreshView(json,mode,ago,selfSelect,CurrencyModel.selfSelectModel,SelfSelectFragment.class);

							} catch (IOException e) {
								e.printStackTrace();
							}
						}else {
						}
					}

					@Override
					public void onFailure(Call<ResponseBody> call, Throwable t) {
						Log.w(CurrencyViewModel.class,call.toString());
						t.printStackTrace();
					}
				});
			}


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
		if(SelfSelectFragment.class == fragmentClass)
			for (CurrencyModel currencyModel : data) {
				CurrencyDao.update(currencyModel);
			}
		CurrencySpiritApp.getApp().getHandler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if(mode == Mode.REFRESH || mode == Mode.NORMAL)
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

	public static void attention(final CurrencyModel currencyModel){
		if(!checkAttention(true)){
			return;
		}
		NewListApi newListApi = RetrofitFactory.getRetrofit().create(NewListApi.class);
		Call<ResponseBody> currency = newListApi.addtag(
				AppConfig.token,AppConfig.deviceId,"",currencyModel.coin_id,AppConfig.deviceType);
		currency.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				if(response.isSuccessful()){
					CurrencyDao.add(currencyModel);
					CurrencyModel.attentionSuccess(true);
				}else {
					CurrencyModel.attentionFailed(true,UIUtil.getString(R.string.attention_failed_net));
				}
			}
			@Override
			public void onFailure(Call<ResponseBody> call, Throwable t) {
				CurrencyModel.attentionFailed(true,UIUtil.getString(R.string.attention_failed_net));
			}
		});
	}
	public static void unAttention(final CurrencyModel currencyModel){
		if(!checkAttention(false)){
			return;
		}
		NewListApi newListApi = RetrofitFactory.getRetrofit().create(NewListApi.class);
		Call<ResponseBody> currency = newListApi.deltag(
				AppConfig.token,AppConfig.deviceId,"",currencyModel.coin_id,AppConfig.deviceType);
		currency.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				if(response.isSuccessful()){
					CurrencyDao.del(currencyModel.coin_id);
					CurrencyModel.attentionSuccess(false);
				}else {
					CurrencyModel.attentionFailed(false,UIUtil.getString(R.string.unattention_failed_net));
				}
			}
			@Override
			public void onFailure(Call<ResponseBody> call, Throwable t) {
				CurrencyModel.attentionFailed(false,UIUtil.getString(R.string.unattention_failed_net));
			}
		});
	}
	private static boolean checkAttention(boolean isAttention){
		if(!NetUtils.hasNetwork(CurrencySpiritApp.getContext())){
			CurrencyModel.attentionFailed(isAttention,UIUtil.getString(isAttention ? R.string.attention_failed_net : R.string.unattention_failed_net));
			return false;
		}
		if(StringUtils.isEmpty(AppConfig.token)){
			CurrencyModel.attentionFailed(isAttention,UIUtil.getString(isAttention ? R.string.attention_failed_token : R.string.unattention_failed_token));
			return false;
		}
		if(StringUtils.isEmpty(AppConfig.deviceId)){
			String deviceId = ShapeUtil.get(AppConstant.key_deviceId, "");
			if(StringUtils.isEmpty(deviceId)){
				CurrencyModel.attentionFailed(isAttention,UIUtil.getString(isAttention ? R.string.deviceId : R.string.undeviceId));
				return false;
			}else {
				AppConfig.deviceId = deviceId;
			}
		}
		return true;
	}
}
