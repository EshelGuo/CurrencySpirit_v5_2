package com.eshel.currencyspirit.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eshel.currencyspirit.CurrencySpiritApp;
import com.eshel.currencyspirit.R;
import com.eshel.currencyspirit.util.UIUtil;
import com.eshel.currencyspirit.widget.RecycleViewDivider;
import com.eshel.currencyspirit.widget.util.Config;
import com.eshel.currencyspirit.widget.util.GlideRoundedRectangleTransform;
import com.eshel.model.EssenceModel;
import com.eshel.viewmodel.BaseViewModel;
import com.eshel.viewmodel.EssenceViewModel;
import com.lhh.ptrrv.library.PullToRefreshRecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import baseproject.base.BaseActivity;
import baseproject.util.DensityUtil;
import baseproject.util.MD5Utils;
import baseproject.util.ReflectUtil;
import baseproject.util.StringUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guoshiwen on 2017/10/12.
 */

public class EssenceHistoryActivity extends BaseActivity {

	@BindView(R.id.rv_essence)
	PullToRefreshRecyclerView mRvEssence;
	private EssenceHistoryActivity.EssenceAdapter mEssenceAdapter;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		hideActionBar();
		setContentView(R.layout.activity_essence_history);
		ButterKnife.bind(this,getContentView());
		showTitle();
		showBack();
		setTitleText(UIUtil.getString(R.string.item_history));
		init();
		setSwipeBackEnable(true);
		EssenceViewModel.getEssenceDataFromHistory(BaseViewModel.Mode.NORMAL);
	}

	public void init() {
		mRvEssence.setProgressBackgroundColorSchemeColor(UIUtil.getColor(R.color.text_white));
		mRvEssence.setSwipeEnable(true);//open swipe
		mRvEssence.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
		mRvEssence.getRecyclerView().addItemDecoration(new RecycleViewDivider(this,LinearLayoutManager.HORIZONTAL,
				DensityUtil.dp2px(Config.dividerHeight), UIUtil.getColor(R.color.dividerColor),DensityUtil.dp2px(10),DensityUtil.dp2px(10)));
		mEssenceAdapter = new EssenceHistoryActivity.EssenceAdapter();
		mRvEssence.setAdapter(mEssenceAdapter);

		//remove header
		mRvEssence.removeHeader();
		// set true to open swipe(pull to refresh, default is true)
		// set OnRefreshListener
		mRvEssence.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				mRvEssence.setRefreshing(true);
				EssenceViewModel.getEssenceDataFromHistory(BaseViewModel.Mode.REFRESH);
			}
		});
		// set loadmore enable, onFinishLoading(can load more? , select before item)
		mRvEssence.onFinishLoading(false, false);
	}

	public void notifyView(BaseViewModel.Mode mode) {
		if(mode == BaseViewModel.Mode.REFRESH)
			mRvEssence.setOnRefreshComplete();
		if(mRvEssence != null && mEssenceAdapter != null) {
			mRvEssence.onFinishLoading(false, false);
			mEssenceAdapter.notifyDataSetChanged();
		}
	}

	public class EssenceAdapter extends RecyclerView.Adapter<EssenceHistoryActivity.EssenceViewHolder> {

		@Override
		public EssenceHistoryActivity.EssenceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			return new EssenceHistoryActivity.EssenceViewHolder();
		}

		@Override
		public void onBindViewHolder(EssenceViewHolder holder, int position) {
			holder.bindDataToView(EssenceModel.getHistoryDataByPosition(position));
		}

		@Override
		public int getItemCount() {
			List<EssenceModel> essenceData = EssenceModel.historyData;
			if(essenceData != null)
				return essenceData.size();
			return 0;
		}
	}

	public class EssenceViewHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.time)
		TextView time;
		@BindView(R.id.title)
		TextView title;
		@BindView(R.id.icon)
		ImageView icon;
		private SimpleDateFormat mFormat;

		public EssenceViewHolder() {
			super(LayoutInflater.from(EssenceHistoryActivity.this).inflate(R.layout.item_essence, null));
			ButterKnife.bind(this,itemView);
			Glide.with(EssenceHistoryActivity.this)
					.load(R.drawable.default_image)
					.transform(new GlideRoundedRectangleTransform(EssenceHistoryActivity.this))
					.into(icon);
			icon.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					icon.setMinimumHeight((int) (icon.getWidth() * 0.75f));
					icon.setMaxHeight((int) (icon.getWidth() * 0.75f));
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
						itemView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
					}
				}
			});
		}
		public void bindDataToView(final EssenceModel essenceModel){
			if(mFormat == null)
				mFormat = new SimpleDateFormat(UIUtil.getString(R.string.item_time_format), Locale.getDefault());
			time.setText(mFormat.format(new Date(essenceModel.update_time)));
			title.setText(essenceModel.title);
			if(!StringUtils.isEmpty(essenceModel.imageurl)) {
				Glide.with(EssenceHistoryActivity.this).
						load(essenceModel.imageurl)
						.transform(new GlideRoundedRectangleTransform(EssenceHistoryActivity.this))
						.into(icon);
			} else {
				/*Glide.with(EssenceHistoryActivity.this).
						load(R.drawable.default_image)
						.transform(new GlideRoundedRectangleTransform(EssenceHistoryActivity.this))
						.into(icon);*/
				String md5 = MD5Utils.encode(essenceModel.toString());
				char c = md5.charAt(md5.length() - 1);
				int index = getIndex(c);
				Glide.with(EssenceHistoryActivity.this).
						load(ReflectUtil.getPublicStaticInt(R.drawable.class,"image_"+getIndex(c)))
						.transform(new GlideRoundedRectangleTransform(EssenceHistoryActivity.this))
						.into(icon);
			}
			itemView.setBackgroundResource(R.drawable.item_selector);
			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					essenceModel.isClicked = true;
					if(essenceModel.isClicked){
						title.setTextColor(UIUtil.getColor(android.R.color.darker_gray));
					}else {
						title.setTextColor(UIUtil.getColor(android.R.color.black));
					}
					Intent intent = new Intent(EssenceHistoryActivity.this, EssenceDetailsActivity.class);
					intent.putExtra("essenceModel",essenceModel);
					startActivity(intent);
				}
			});
			if(essenceModel.isClicked){
				title.setTextColor(UIUtil.getColor(android.R.color.darker_gray));
			}else {
				title.setTextColor(UIUtil.getColor(android.R.color.black));
			}
		}
	}
	public static int getIndex(char c){
		if(c >= '0' && c <= '9'){
			return c - '0';
		}else {
			if(c == 'a'||c == 'A'){
				return 10;
			}else if(c == 'b'||c == 'B'){
				return 11;
			}else if(c == 'c'||c == 'C'){
				return 12;
			}else if(c == 'd'||c == 'D'){
				return 13;
			}else if(c == 'e'||c == 'E'){
				return 14;
			}else if(c == 'f'||c == 'F'){
				return 15;
			}else {
				return 0;
			}
		}
	}
}
