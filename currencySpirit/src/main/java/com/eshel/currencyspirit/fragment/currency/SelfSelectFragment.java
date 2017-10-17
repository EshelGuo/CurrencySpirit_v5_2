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
	protected void refreshData() {
		CurrencyViewModel.selfSelect.getData(BaseViewModel.Mode.REFRESH);
	}
}
