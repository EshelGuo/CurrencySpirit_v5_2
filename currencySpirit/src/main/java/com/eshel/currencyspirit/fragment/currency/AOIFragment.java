package com.eshel.currencyspirit.fragment.currency;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.eshel.currencyspirit.R;
import com.eshel.model.CurrencyModel;
import com.eshel.viewmodel.BaseViewModel;
import com.eshel.viewmodel.CurrencyViewModel;

import baseproject.base.BaseFragment;

/**
 * createBy Eshel
 * createTime: 2017/10/15 14:58
 * desc: 涨幅
 */

public class AOIFragment extends CurrencyBaseFragment{
	// true 代表升序 false代表 降序
	public boolean isUp = true;
	@Override
	public void loadData(BaseViewModel.Mode mode) {
		if(isUp)
			CurrencyViewModel.getAOIData(mode);
		else
			CurrencyViewModel.getAOI2Data(mode);
	}
	@Override
	public CurrencyModel.BaseModel getBaseMode() {
		if(isUp)
			return CurrencyModel.aoiModel;
		else
			return CurrencyModel.aoiModel2;
	}
	@Override
	protected void refreshData() {
		if(isUp)
			CurrencyViewModel.refreshAOIData();
		else
			CurrencyViewModel.refreshAOI2Data();
	}
	public void changeState(TextView textView){
		isUp = !isUp;
		int drawableId = R.drawable.sort_arrow_down;
		if(isUp)
			drawableId = R.drawable.sort_arrow_up;
		Drawable drawable = getResources().getDrawable(drawableId);
		drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
		textView.setCompoundDrawables(null,null,drawable,null);

		if(getBaseMode().data.size() == 0) {
			loadData(BaseViewModel.Mode.NORMAL);
		}
		else {
			mAdapter.notifyDataSetChanged();
		}

	}
}
