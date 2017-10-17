package com.eshel.model;

import com.eshel.currencyspirit.R;
import com.eshel.currencyspirit.activity.CurrencyDetailsActivity;
import com.eshel.currencyspirit.factory.FragmentFactory;
import com.eshel.currencyspirit.fragment.currency.MarketValueFragment;
import com.eshel.currencyspirit.util.UIUtil;
import com.eshel.viewmodel.BaseViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import baseproject.base.BaseActivity;
import baseproject.base.BaseFragment;
import baseproject.util.DataUtil;

/**
 * createBy Eshel
 * createTime: 2017/10/15 20:59
 * desc: TODO
 */

public class CurrencyModel implements Serializable{

	public static String moneyFormat(String pre,double turnnumber){
		return pre + DataUtil.double2Str(turnnumber,true);
	}

	public static BaseModel martetValueModel = new BaseModel();
	public static BaseModel selfSelectModel = new BaseModel();
	// true 升序
	public static BaseModel aoiModel = new BaseModel();
	// false 降序
	public static BaseModel aoiModel2 = new BaseModel();


	/**
	 * chinesename : Ethereum
	 * coin_id : ethereum
	 * englishname : ETH
	 * infoBean : {"chinesename":"Ethereum","englishname":"Ethereum","imageurl":"https://files.coinmarketcap.com/static/img/coins/128x128/ethereum.png","symbol":"ETH"}
	 * marketSymbol : USD
	 * market_type : 33
	 * percent : -4.76
	 * platform : https://coinmarketcap.com
	 * price : 326.049
	 * rank : 2
	 * symbol : ETH
	 * turnnumber : 3.1012869481E10
	 * turnvolume : 6.11255E8
	 * update_time : 1508075352000
	 * url : https://coinmarketcap.com/currencies/ethereum/
	 * yprice : null
	 */
	public String chinesename;
	public String coin_id;
	public String englishname;
	public InfoBean infoBean;
	public String marketSymbol;
	public int market_type;
	public double percent;
	public String platform;
	public double price;
	public int rank;
	public String symbol;
	public double turnnumber;
	public double turnvolume;
	public long update_time;
	public String url;
	public String yprice;

	public static class InfoBean  implements Serializable{
		/**
		 * chinesename : Ethereum
		 * englishname : Ethereum
		 * imageurl : https://files.coinmarketcap.com/static/img/coins/128x128/ethereum.png
		 * symbol : ETH
		 */
		public String chinesename;
		public String englishname;
		public String imageurl;
		public String symbol;
	}
	public static class BaseModel{
		public List<CurrencyModel> data = new ArrayList<>();
		public int loadDataCount = 20;
		public CurrencyModel getDataByPosition(int position){
			return data.get(position);
		}
		public static void notifyView(BaseViewModel.Mode mode , boolean isSuccess,Class FragmentClass){
			BaseFragment baseFragment = (BaseFragment) FragmentFactory.getFragment(FragmentClass);
			if(isSuccess) {
				if (baseFragment.getCurrState() != BaseFragment.LoadState.StateLoadSuccess)
					baseFragment.changeState(BaseFragment.LoadState.StateLoadSuccess);
				else {
					baseFragment.notifyView();
				}
			}else {
				if(mode == BaseViewModel.Mode.NORMAL)
					baseFragment.changeState(BaseFragment.LoadState.StateLoadFailed);
				else if(mode == BaseViewModel.Mode.REFRESH){
					baseFragment.refreshFailed();
				}else {
					baseFragment.loadModeFailed();
				}
			}
		}
	}

	// TODO: 2017/10/17
	public static void attentionFailed(boolean isAttention ,String msg){
		UIUtil.toast(msg);
		BaseActivity topActivity = BaseActivity.getTopActivity();
		if(topActivity instanceof CurrencyDetailsActivity){
			CurrencyDetailsActivity currencyDetailsActivity = (CurrencyDetailsActivity) topActivity;
			currencyDetailsActivity.attentionOver(isAttention,msg);
		}
	}
	public static void attentionSuccess(boolean isAttention){
		UIUtil.toast(UIUtil.getString(R.string.attention_success));
		BaseActivity topActivity = BaseActivity.getTopActivity();
		if(topActivity instanceof CurrencyDetailsActivity){
			CurrencyDetailsActivity currencyDetailsActivity = (CurrencyDetailsActivity) topActivity;
			currencyDetailsActivity.attentionOver(isAttention,null);
		}
	}
}
