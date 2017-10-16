package com.eshel.currencyspirit.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eshel.currencyspirit.R;
import com.eshel.currencyspirit.activity.EssenceHistoryActivity;
import com.eshel.currencyspirit.activity.HomeActivity;
import com.eshel.currencyspirit.widget.OptionItemView;
import com.eshel.currencyspirit.widget.OverScrollView;

import baseproject.base.BaseFragment;
import baseproject.util.DensityUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * createBy Eshel
 * createTime: 2017/10/4 20:42
 * desc: TODO
 */

public class UserFragment extends BaseFragment {
	SuccessViewHolder mSuccessViewHolder;
	Unbinder unbinder;

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
	public View getLoadSuccessView() {
		View root = View.inflate(getActivity(), R.layout.fragment_user, null);
		mSuccessViewHolder = new SuccessViewHolder();
		unbinder = ButterKnife.bind(mSuccessViewHolder,root);
		mSuccessViewHolder.itemHistory.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), EssenceHistoryActivity.class));
			}
		});
		mSuccessViewHolder.itemAbout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		mSuccessViewHolder.itemOption.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		mSuccessViewHolder.itemPraised.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		return root;
	}

	@Override
	public void notifyView() {
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}

	@Override
	protected void reloadData() {
	}

	class SuccessViewHolder{
		@BindView(R.id.item_icon)
		OptionItemView itemIcon;
		@BindView(R.id.item_history)
		OptionItemView itemHistory;
		@BindView(R.id.item_praised)
		OptionItemView itemPraised;
		@BindView(R.id.item_about)
		OptionItemView itemAbout;
		@BindView(R.id.item_option)
		OptionItemView itemOption;
		@BindView(R.id.OverScroller)
		OverScrollView OverScroller;
	}
	@Override
	public void onResume() {
		super.onResume();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			HomeActivity homeActivity = (HomeActivity) getActivity();
			homeActivity.getTitle2().setElevation(DensityUtil.dp2px(HomeActivity.titleElevation/2));
		}
	}
}
