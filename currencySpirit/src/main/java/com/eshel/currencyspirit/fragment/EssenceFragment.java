package com.eshel.currencyspirit.fragment;

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
import com.eshel.currencyspirit.activity.EssenceDetailsActivity;
import com.eshel.currencyspirit.activity.HomeActivity;
import com.eshel.currencyspirit.util.UIUtil;
import com.eshel.currencyspirit.widget.RecycleViewDivider;
import com.eshel.currencyspirit.widget.util.Config;
import com.eshel.currencyspirit.widget.util.GlideRoundedRectangleTransform;
import com.eshel.currencyspirit.widget.util.LoadMoreView;
import com.eshel.model.EssenceModel;
import com.eshel.viewmodel.BaseViewModel;
import com.eshel.viewmodel.EssenceViewModel;
import com.lhh.ptrrv.library.PullToRefreshRecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import baseproject.base.BaseFragment;
import baseproject.util.DensityUtil;
import baseproject.util.MD5Utils;
import baseproject.util.ReflectUtil;
import baseproject.util.StringUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.eshel.currencyspirit.activity.HomeActivity.titleElevation;

/**
 * createBy Eshel
 * createTime: 2017/10/4 20:41
 * desc: 精华 fragment
 */

public class EssenceFragment extends BaseFragment {

	private PullToRefreshRecyclerView  mRv_essence;
	private EssenceAdapter mEssenceAdapter;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EssenceViewModel.getEssenceData(BaseViewModel.Mode.NORMAL);
	}

	@Override
	protected void reloadData() {
		EssenceViewModel.getEssenceData(BaseViewModel.Mode.NORMAL);
	}

	@Override
	public View getLoadSuccessView() {
		ViewGroup parent = (ViewGroup) View.inflate(getActivity(), R.layout.view_essence, null);
		mRv_essence = (PullToRefreshRecyclerView) parent.findViewById(R.id.rv_essence);
		mRv_essence.setProgressBackgroundColorSchemeColor(UIUtil.getColor(R.color.text_white));
		mRv_essence.setSwipeEnable(true);//open swipe
		mRv_essence.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
		mRv_essence.getRecyclerView().addItemDecoration(new RecycleViewDivider(getActivity(),LinearLayoutManager.HORIZONTAL,
				DensityUtil.dp2px(Config.dividerHeight),UIUtil.getColor(R.color.dividerColor),DensityUtil.dp2px(10),DensityUtil.dp2px(10)));
		mEssenceAdapter = new EssenceAdapter();
		mRv_essence.setAdapter(mEssenceAdapter);

		LoadMoreView loadMoreView = new LoadMoreView(getActivity(), mRv_essence.getRecyclerView());
		loadMoreView.setLoadmoreString(getString(R.string.string_loadmore));
		loadMoreView.setLoadMorePadding(100);
		mRv_essence.setLoadMoreFooter(loadMoreView);
		//remove header
		mRv_essence.removeHeader();
		// set true to open swipe(pull to refresh, default is true)
		// set PagingableListener
		mRv_essence.setPagingableListener(new PullToRefreshRecyclerView.PagingableListener() {
			@Override
			public void onLoadMoreItems() {
				EssenceViewModel.getEssenceData(BaseViewModel.Mode.LOADMORE);
			}
		});

		// set OnRefreshListener
		mRv_essence.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				mRv_essence.setRefreshing(true);
				EssenceViewModel.refreshData();
			}
		});
		mRv_essence.onFinishLoading(true, false);
		return parent;
	}

	@Override
	public void notifyView(BaseViewModel.Mode mode) {
		if(mode == BaseViewModel.Mode.REFRESH)
			mRv_essence.setOnRefreshComplete();
		if(EssenceModel.loadDataCount < 20)
			mRv_essence.onFinishLoading(false, false);
		else
			mRv_essence.onFinishLoading(true,false);
		mEssenceAdapter.notifyDataSetChanged();
	}
	@Override
	public void refreshFailed() {
		mRv_essence.setOnRefreshComplete();
		super.refreshFailed();
	}
	@Override
	public void loadModeFailed() {
		super.loadModeFailed();
		mRv_essence.onFinishLoading(false, false);
		mRv_essence.onFinishLoading(true, false);
	}

	public class EssenceAdapter extends RecyclerView.Adapter<EssenceViewHolder> {
		@Override
		public EssenceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			return new EssenceViewHolder();
		}
		@Override
		public void onBindViewHolder(EssenceViewHolder holder, int position) {
			holder.bindDataToView(EssenceModel.getEssenceDataByPosition(position));
		}
		@Override
		public int getItemCount() {
			ArrayList<EssenceModel> essenceData = EssenceModel.essenceData;
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
			super(LayoutInflater.from(getActivity()).inflate(R.layout.item_essence, null));
			ButterKnife.bind(this,itemView);
			Glide.with(getActivity())
					.load(R.drawable.default_image)
					.transform(new GlideRoundedRectangleTransform(getActivity()))
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
				Glide.with(getActivity()).
						load(essenceModel.imageurl)
						.transform(new GlideRoundedRectangleTransform(getActivity()))
						.into(icon);
			} else {
				/*Glide.with(getActivity()).
						load(R.drawable.default_image)
						.transform(new GlideRoundedRectangleTransform(getActivity()))
						.into(icon);*/
				String md5 = MD5Utils.encode(essenceModel.toString());
				char c = md5.charAt(md5.length() - 1);
				int index = getIndex(c);
				Glide.with(getActivity()).
						load(ReflectUtil.getPublicStaticInt(R.drawable.class,"image_"+getIndex(c)))
						.transform(new GlideRoundedRectangleTransform(getActivity()))
						.into(icon);
			}
			itemView.setBackgroundResource(R.drawable.item_selector);
			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					essenceModel.isClicked = true;
					changeTextColor(essenceModel,time,title);
					EssenceViewModel.addDataToHistory(essenceModel);
					Intent intent = new Intent(getActivity(), EssenceDetailsActivity.class);
					intent.putExtra(EssenceDetailsActivity.key,essenceModel);
					startActivity(intent);
				}
			});
			changeTextColor(essenceModel,time,title);
		}
		private void changeTextColor(EssenceModel model, TextView ... textview){
			for (TextView textView : textview) {
				if(model.isClicked){
					textView.setTextColor(UIUtil.getColor(android.R.color.darker_gray));
				}else {
					textView.setTextColor(UIUtil.getColor(android.R.color.black));
				}
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
