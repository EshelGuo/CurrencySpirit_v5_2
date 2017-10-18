package com.eshel.currencyspirit.fragment;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.eshel.currencyspirit.R;
import com.eshel.currencyspirit.factory.FragmentFactory;
import com.eshel.currencyspirit.fragment.currency.AOIFragment;
import com.eshel.currencyspirit.fragment.currency.MarketValueFragment;
import com.eshel.currencyspirit.fragment.currency.SelfSelectFragment;
import com.eshel.currencyspirit.util.UIUtil;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import baseproject.base.BaseFragment;
import baseproject.util.Log;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * createBy Eshel
 * createTime: 2017/10/4 20:43
 * desc: TODO
 */

public class CurrencyFragment extends BaseFragment {

	SmartTabLayout tab;
	ViewPager viewpager;
	private CurrencyAdapter mCurrencyAdapter;
	private View mView;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		container.postDelayed(new Runnable() {
			@Override
			public void run() {
				changeState(LoadState.StateLoadSuccess);
			}
		}, 100);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected void reloadData() {
	}

	@Override
	public View getLoadSuccessView() {
		if(mView == null) {
			mView = View.inflate(getActivity(), R.layout.view_currency, null);
			tab = (SmartTabLayout) mView.findViewById(R.id.tab);
			viewpager = (ViewPager) mView.findViewById(R.id.viewpager);
			mCurrencyAdapter = new CurrencyAdapter();
			viewpager.setAdapter(mCurrencyAdapter);
			tab.setViewPager(viewpager);
			tab.setOnTabClickListener(new SmartTabLayout.OnTabClickListener() {
				@Override
				public void onTabClicked(int position) {
					if(viewpager.getCurrentItem() == position){
						UIUtil.debugToast("tab再次被点击了 position: "+position);
						if(position == zhangfu){
							// TODO: 2017/10/16  做更新 RecyleView 对涨幅栏目重新排序
							TextView textView = (TextView) tab.getTabAt(position).findViewById(R.id.currency_tab_textview);
							AOIFragment aoiFragment = (AOIFragment) FragmentFactory.getFragment(AOIFragment.class);
							aoiFragment.changeState(textView);
						}
					}else {
						UIUtil.debugToast("tab被点击了 position: " + position);
					}
				}
			});
			TextView textView = (TextView) tab.getTabAt(zhangfu).findViewById(R.id.currency_tab_textview);
			Drawable drawable = getResources().getDrawable(R.drawable.sort_arrow_up);
			drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
			textView.setCompoundDrawables(null,null,drawable,null);
		}
		return mView;
	}

	@Override
	public void notifyView() {
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	class CurrencyAdapter extends FragmentPagerAdapter{
		String[] pagerTitle = new String[]{
				getString(R.string.self_select),
				getString(R.string.market_value),
				getString(R.string.amount_of_increase)
		};
		public CurrencyAdapter() {
			super(getChildFragmentManager());
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			switch (position){
				case self_select:
					fragment = FragmentFactory.getFragment(SelfSelectFragment.class);
					break;
				case shizhi:
					fragment = FragmentFactory.getFragment(MarketValueFragment.class);
					break;
				case zhangfu:
					fragment = FragmentFactory.getFragment(AOIFragment.class);
					TextView textView = (TextView) tab.getTabAt(position).findViewById(R.id.currency_tab_textview);
					Drawable drawable = getResources().getDrawable(R.drawable.sort_arrow_up);
					drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
					textView.setCompoundDrawables(null,null,drawable,null);
					break;
			}
			Log.i("fragment: ",fragment);
			return fragment;
		}

		@Override
		public int getCount() {
			if(pagerTitle != null)
				return pagerTitle.length;
			return 0;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return pagerTitle[position];
		}
	}

	public final int self_select = 0;
	public final int shizhi = 1;
	public final int zhangfu = 2;

	@Override
	public void onDestroy() {
		super.onDestroy();
		mView = null;
	}
}
