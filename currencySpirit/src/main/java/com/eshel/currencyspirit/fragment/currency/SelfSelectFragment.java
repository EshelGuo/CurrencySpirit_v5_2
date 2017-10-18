package com.eshel.currencyspirit.fragment.currency;

import com.eshel.database.dao.CurrencyDao;
import com.eshel.database.table.CurrencyTable;
import com.eshel.model.CurrencyModel;
import com.eshel.viewmodel.BaseViewModel;
import com.eshel.viewmodel.CurrencyViewModel;

import java.util.List;

/**
 * Created by guoshiwen on 2017/10/17.
 */

public class SelfSelectFragment extends CurrencyBaseFragment{
	@Override
	public void loadData(BaseViewModel.Mode mode) {
		CurrencyViewModel.selfSelect.getData(mode);
	}

	@Override
	public CurrencyModel.BaseModel getBaseMode() {
		return CurrencyModel.selfSelectModel;
	}

	@Override
	public void refreshData() {
		CurrencyViewModel.selfSelect.getData(BaseViewModel.Mode.REFRESH);
	}
	public static boolean isOnResume = false;
	@Override
	public void onResume() {
		super.onResume();
		isOnResume = true;
		if(CurrencyModel.isRefreshed){
			CurrencyModel.isRefreshed = false;
			refreshData();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		isOnResume = false;
	}
}
