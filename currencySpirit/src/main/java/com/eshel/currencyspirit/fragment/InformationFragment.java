package com.eshel.currencyspirit.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eshel.currencyspirit.CurrencySpiritApp;
import com.eshel.currencyspirit.R;
import com.eshel.currencyspirit.activity.HomeActivity;
import com.eshel.currencyspirit.activity.WeiboDetailsActivity;
import com.eshel.currencyspirit.util.UIUtil;
import com.eshel.currencyspirit.widget.RecycleViewDivider;
import com.eshel.currencyspirit.widget.util.Config;
import com.eshel.currencyspirit.widget.util.GlideCircleTransform;
import com.eshel.currencyspirit.widget.util.LoadMoreView;
import com.eshel.model.InformationModel;
import com.eshel.viewmodel.BaseViewModel;
import com.eshel.viewmodel.InformationViewModel;
import com.lhh.ptrrv.library.PullToRefreshRecyclerView;

import java.util.ArrayList;

import baseproject.base.BaseFragment;
import baseproject.util.DensityUtil;
import baseproject.util.StringUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * createBy Eshel
 * createTime: 2017/10/4 20:41
 * desc: 消息 Fragment
 */

public class InformationFragment extends BaseFragment {
	private PullToRefreshRecyclerView mRv_Information;
	private InformationFragment.InformationAdapter mInformationAdapter;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		InformationViewModel.getInformationData(BaseViewModel.Mode.NORMAL);
	}

	@Override
	protected void reloadData() {
		InformationViewModel.getInformationData(BaseViewModel.Mode.NORMAL);
	}

	@Override
	public View getLoadSuccessView() {
		ViewGroup parent = (ViewGroup) View.inflate(getActivity(), R.layout.view_information, null);
		mRv_Information = (PullToRefreshRecyclerView) parent.findViewById(R.id.rv_information);
		mRv_Information.setProgressBackgroundColorSchemeColor(UIUtil.getColor(R.color.text_white));
		mRv_Information.setSwipeEnable(true);//open swipe
		mRv_Information.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
		mRv_Information.getRecyclerView().addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL,
				DensityUtil.dp2px(Config.dividerHeight), UIUtil.getColor(R.color.dividerColor), DensityUtil.dp2px(10), DensityUtil.dp2px(10)));
		mInformationAdapter = new InformationFragment.InformationAdapter();
		mRv_Information.setAdapter(mInformationAdapter);

		LoadMoreView loadMoreView = new LoadMoreView(getActivity(), mRv_Information.getRecyclerView());
		loadMoreView.setLoadmoreString(getString(R.string.string_loadmore));
		loadMoreView.setLoadMorePadding(100);
		mRv_Information.setLoadMoreFooter(loadMoreView);
		//remove header
		mRv_Information.removeHeader();
		// set true to open swipe(pull to refresh, default is true)
		// set PagingableListener
		mRv_Information.setPagingableListener(new PullToRefreshRecyclerView.PagingableListener() {
			@Override
			public void onLoadMoreItems() {
				// todo do loadmore here
				InformationViewModel.getInformationData(BaseViewModel.Mode.LOADMORE);
			}
		});

		// set OnRefreshListener
		mRv_Information.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				mRv_Information.setRefreshing(true);
				// todo do refresh here
				CurrencySpiritApp.getApp().getHandler().postDelayed(new Runnable() {
					@Override
					public void run() {
						InformationViewModel.refreshData();
						mRv_Information.setOnRefreshComplete();
						mRv_Information.onFinishLoading(true, false);
					}
				}, 1000);
			}
		});
//		mRv_Information.addHeaderView(View.inflate(getActivity(), android.R.layout.simple_list_item_1, null));
		// add headerView
		//mRv_Information.addHeaderView(View.inflate(this, R.layout.header, null));

		//set EmptyVlist
		//mRv_Information.setEmptyView(View.inflat(this,R.layout.empty_view, null));

		// set loadmore String
		//mRv_Information.setLoadmoreString("loading");

		// set loadmore enable, onFinishLoading(can load more? , select before item)
		mRv_Information.onFinishLoading(true, false);
		return parent;
	}

	@Override
	public void notifyView() {
		if (InformationModel.loadDataCount < 20)
			mRv_Information.onFinishLoading(false, false);
		else
			mRv_Information.onFinishLoading(true, false);
		mInformationAdapter.notifyDataSetChanged();
	}

	public class InformationAdapter extends RecyclerView.Adapter<InformationFragment.InformationViewHolder> {

		@Override
		public InformationFragment.InformationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			return new InformationFragment.InformationViewHolder();
		}

		@Override
		public void onBindViewHolder(InformationFragment.InformationViewHolder holder, int position) {
			holder.bindDataToView(InformationModel.getInformationDataByPosition(position));
		}

		@Override
		public int getItemCount() {
			ArrayList<InformationModel> informationData = InformationModel.informationData;
			if (informationData != null)
				return informationData.size();
			return 0;
		}
	}

	public class InformationViewHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.iv_icon)
		ImageView mIvIcon;
		@BindView(R.id.tv_time)
		TextView mTvTime;
		@BindView(R.id.tv_title)
		TextView mTvTitle;
		@BindView(R.id.tv_desc)
		TextView mTvDesc;

		public InformationViewHolder() {
			super(LayoutInflater.from(getActivity()).inflate(R.layout.item_information, null));
			ButterKnife.bind(this, itemView);
			int itemHeight = (int) (UIUtil.getScreenWidth() * 0.1935f);
			ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
			if(layoutParams == null){
				layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,itemHeight);
			}else {
				layoutParams.height = itemHeight;
			}
			itemView.setLayoutParams(layoutParams);
			ViewGroup.LayoutParams imageParams = mIvIcon.getLayoutParams();
			if(imageParams == null){
				imageParams = new ViewGroup.LayoutParams(itemHeight,itemHeight);
			}else {
				imageParams.height = itemHeight;
				imageParams.width = itemHeight;
			}
			mIvIcon.setLayoutParams(imageParams);
			itemView.setBackgroundResource(R.drawable.item_selector);
//			mIvIcon.setImageDrawable(new ColorDrawable(0xFFE7E7E7));
			/*mIvIcon.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					icon.setMinimumHeight((int) (icon.getWidth() * 0.75f));
					icon.setMaxHeight((int) (icon.getWidth() * 0.75f));
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
						itemView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
					}
				}
			});*/
		}

		public void bindDataToView(final InformationModel informationModel) {
			mTvTitle.setTextColor(Color.BLACK);
			mTvDesc.setTextColor(UIUtil.getColor(R.color.text_gray));
			mTvTime.setText(StringUtils.timeFormat(informationModel.update_time));
			mTvTitle.setText(informationModel.wbname);
			mTvDesc.setText(informationModel.text);
			Glide.with(getActivity()).
					load(informationModel.imageurl)
					.transform(new GlideCircleTransform(getActivity()))
					.into(mIvIcon);
			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), WeiboDetailsActivity.class);
					intent.putExtra(WeiboDetailsActivity.key,informationModel);
					startActivity(intent);
				}
			});
		}

	}
}
