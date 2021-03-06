package com.eshel.currencyspirit.activity;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eshel.currencyspirit.CurrencySpiritApp;
import com.eshel.currencyspirit.R;
import com.eshel.currencyspirit.factory.FragmentFactory;
import com.eshel.currencyspirit.fragment.CurrencyFragment;
import com.eshel.currencyspirit.fragment.EssenceFragment;
import com.eshel.currencyspirit.fragment.InformationFragment;
import com.eshel.currencyspirit.fragment.UserFragment;
import com.eshel.currencyspirit.util.PermissionUtil;
import com.eshel.currencyspirit.util.UIUtil;
import com.eshel.currencyspirit.widget.night.NightViewUtil;
import com.eshel.model.CurrencyModel;
import com.eshel.model.EssenceModel;
import com.eshel.model.InformationModel;
import com.eshel.viewmodel.BaseViewModel;
import com.eshel.viewmodel.SearchViewModel;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

import baseproject.base.BaseActivity;
import baseproject.util.DensityUtil;
import baseproject.util.Log;
import butterknife.BindView;
import butterknife.ButterKnife;
import xgpush.XGMsage;

/**
 * createBy Eshel
 * createTime: 2017/10/2 20:22
 * desc: APP 主界面
 */

public class HomeActivity extends BaseActivity {
	@BindView(R.id.bottomBar)
	BottomBar bottomBar;
	@BindView(R.id.vp_main)
	ViewPager vpMain;

	private MainPagerAdapter mMainPagerAdapter;
	private int fragmentSize = 4;

	private final int INDEX_ESSENCE_FRAGMENT = 0;
	private final int INDEX_INFORMATION_FRAGMENT = 1;
	private final int INDEX_CURRENCY_FRAGMENT = 2;
	private final int INDEX_USER_FRAGMENT = 3;

	public static int titleElevation = 5;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		ButterKnife.bind(this);
		Log.i("PermissionUtil.activity: "+PermissionUtil.activity);
		initView();
	}

	public void updateActionBar(String title) {
		setTitleText(title);
	}

	private void initView() {
		initActionBar();
		initBottomBar();
		initViewPager();
	}

	private void initActionBar() {
		showTitle();
		hideBack();
		updateActionBar(UIUtil.getString(R.string.item_essence));
	}

	private void initViewPager() {
		mMainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
		vpMain.setAdapter(mMainPagerAdapter);
	}

	private BottomBarTab nearby;

	private void initBottomBar() {
		bottomBar.setBackgroundResource(R.drawable.bottom_bar_bg);
		bottomBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				int[] tabIds = {R.id.item_essence, R.id.item_currency, R.id.item_information, R.id.item_user};
				for (int tabId : tabIds) {
					BottomBarTab tab = bottomBar.getTabWithId(tabId);
					tab.setBarColorWhenSelected(NightViewUtil.changeNightColor(UIUtil.getColor(R.color.bottomBarColor)));
					for (int i = 0; i < tab.getChildCount(); i++) {
						View child = tab.getChildAt(i);
						switch (child.getId()) {
							case R.id.bb_bottom_bar_icon:
								AppCompatImageView icon = (AppCompatImageView) child;
								LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) icon.getLayoutParams();
								layoutParams.height = 0;
								layoutParams.weight = 3;
								icon.setLayoutParams(layoutParams);
								break;
							/*case R.id.bb_bottom_bar_title:
								if(i != 0) {
									TextView text = (TextView) child;
									text.setTextColor();
								}
								break;*/
						}
					}
				}
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					bottomBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				}
			}
		});
		bottomBar.setBackgroundResource(R.drawable.bottom_bar_bg);
		bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
			@Override
			public void onTabSelected(@IdRes int tabId) {
				int currentItem;
				String title;
				switch (tabId) {
					case R.id.item_essence:
						currentItem = INDEX_ESSENCE_FRAGMENT;
						title = UIUtil.getString(R.string.item_essence);
						break;
					case R.id.item_information:
						currentItem = INDEX_INFORMATION_FRAGMENT;
						title = UIUtil.getString(R.string.item_information);
						break;
					case R.id.item_currency:
						currentItem = INDEX_CURRENCY_FRAGMENT;
						title = UIUtil.getString(R.string.item_currency);
						break;
					case R.id.item_user:
						currentItem = INDEX_USER_FRAGMENT;
						title = UIUtil.getString(R.string.item_user);
						break;
					default:
						currentItem = INDEX_ESSENCE_FRAGMENT;
						title = UIUtil.getString(R.string.item_essence);
						break;
				}
				updateActionBar(title);
				int[] tabIds = {R.id.item_essence, R.id.item_currency, R.id.item_information, R.id.item_user};
				for (int id : tabIds) {
					BottomBarTab tab = bottomBar.getTabWithId(id);
					AppCompatImageView icon = (AppCompatImageView) tab.findViewById(R.id.bb_bottom_bar_icon);
					if (id == tabId) {
						nearby = tab;
						icon.setImageState(new int[]{android.R.attr.state_checked}, true);
					} else {
						icon.setImageState(new int[]{-android.R.attr.state_checked}, true);
					}
				}
				vpMain.setCurrentItem(currentItem, false);
			}
		}, true);
	}

	private class MainPagerAdapter extends FragmentPagerAdapter {

		public MainPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment itemFragment;
			switch (position) {
				case INDEX_ESSENCE_FRAGMENT:
					itemFragment = FragmentFactory.getFragment(EssenceFragment.class);
					break;
				case INDEX_INFORMATION_FRAGMENT:
					itemFragment = FragmentFactory.getFragment(InformationFragment.class);
					break;
				case INDEX_CURRENCY_FRAGMENT:
					itemFragment = FragmentFactory.getFragment(CurrencyFragment.class);
					break;
				case INDEX_USER_FRAGMENT:
					itemFragment = FragmentFactory.getFragment(UserFragment.class);
					break;
				default:
					itemFragment = FragmentFactory.getFragment(EssenceFragment.class);
					break;
			}
			return itemFragment;
		}

		@Override
		public int getCount() {
			return fragmentSize;
		}
	}
	private long lastTime;
	private long exitIntervalTime = 1200;
	@Override
	public void onBackPressed() {
		// two click exit
		if(lastTime == 0){
			lastTime = System.currentTimeMillis();
			UIUtil.toast(UIUtil.getString(R.string.exit_app));
			return;
		}
		long currentTime = System.currentTimeMillis();
		if(currentTime - lastTime < exitIntervalTime){
			super.onBackPressed();
			lastTime = 0;
			exitApp();
		}else {
			lastTime = 0;
			onBackPressed();
		}
	}

	private void exitApp() {
		CurrencySpiritApp.isExit = true;
		EssenceModel.clean();
		InformationModel.clean();
		CurrencyModel.clean();
		SearchViewModel.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(XGMsage.msg != null){
			XGMsage.showMsg(this);
		}
		CurrencyFragment fragment = (CurrencyFragment) FragmentFactory.getFragment(CurrencyFragment.class);
		if(fragment != null)
			fragment.notifyView(BaseViewModel.Mode.NORMAL);
	}
}
