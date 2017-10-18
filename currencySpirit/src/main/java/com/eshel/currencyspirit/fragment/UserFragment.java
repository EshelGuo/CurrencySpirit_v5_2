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
import com.eshel.currencyspirit.activity.OptionActivity;
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
	private View mRoot;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		container.postDelayed(new Runnable() {
			@Override
			public void run() {
				changeState(LoadState.StateLoadSuccess);
			}
		}, 0);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public View getLoadSuccessView() {
		if(mRoot == null) {
			mRoot = View.inflate(getActivity(), R.layout.fragment_user, null);
			mSuccessViewHolder = new SuccessViewHolder();
			ButterKnife.bind(mSuccessViewHolder, mRoot);
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
					startActivity(new Intent(getActivity(), OptionActivity.class));
				}
			});
			mSuccessViewHolder.itemPraised.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
				}
			});
		}
		return mRoot;
	}

	@Override
	public void notifyView() {
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
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
	public void onDestroy() {
		super.onDestroy();
		mRoot = null;
	}
}
