package com.eshel.model;

import com.eshel.currencyspirit.activity.EssenceHistoryActivity;
import com.eshel.currencyspirit.factory.FragmentFactory;
import com.eshel.currencyspirit.fragment.EssenceFragment;
import com.eshel.database.table.EssenceHistory;
import com.eshel.viewmodel.BaseViewModel.Mode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import baseproject.base.BaseActivity;
import baseproject.base.BaseFragment;
/**
 * createBy Eshel
 * createTime: 2017/10/5 03:29
 * desc: TODO
 */

public class EssenceModel implements Serializable{
	/**
	 * id : 326
	 * new_type : 1
	 * title : BigONE will launch today!
	 * update_time : 1506873600000
	 * imageurl : null
	 * abstracts: null
	 * url : https://help.big.one/hc/en-us/articles/115002361094-BigONE-will-launch-today-
	 * webicon : http://static.feixiaohao.com/PlatImages/20171005/7f1317d831384881b4dbae5640bec852_15_15.png
	 * webname : [BigOne]
	 */
	public static ArrayList<EssenceModel> essenceData = new ArrayList<>();
	public static List<EssenceModel> historyData = new ArrayList<>();
	public static int loadDataCount = 20;
	public int id;
	public int new_type;
	public String title;
	public long update_time;
	public String abstracts;
	public String url;
	public String imageurl;
	public String webicon;
	public String webname;
	public boolean isClicked;
	public static EssenceModel getEssenceDataByPosition(int position){
		return essenceData.get(position);
	}
	public static EssenceModel getHistoryDataByPosition(int position){
		return historyData.get(position);
	}
	public static List<EssenceModel> transitionData(List<EssenceHistory> originalData){
		if(originalData == null)
			return null;
		List<EssenceModel> transitionData = historyData;
		transitionData.clear();
		for (EssenceHistory history : originalData) {
			transitionData.add(history.get(history));
		}
		return transitionData;
	}
	public static void notifyView(Mode mode , boolean isSuccess){
		BaseFragment essenceFragment = (BaseFragment) FragmentFactory.getFragment(EssenceFragment.class);
		if(isSuccess) {
			if (essenceFragment.getCurrState() != BaseFragment.LoadState.StateLoadSuccess)
				essenceFragment.changeState(BaseFragment.LoadState.StateLoadSuccess);
			else {
				essenceFragment.notifyView();
			}
		}else {
			if(mode == Mode.NORMAL)
				essenceFragment.changeState(BaseFragment.LoadState.StateLoadFailed);
			else if(mode == Mode.REFRESH){
				essenceFragment.refreshFailed();
			}else {
				essenceFragment.loadModeFailed();
			}

		}
	}
	public static void notifyHistoryActivity(){
		EssenceHistoryActivity activity = (EssenceHistoryActivity) BaseActivity.getActivity(EssenceHistoryActivity.class);
		if(activity != null){
			activity.notifyView();
		}
	}
}
