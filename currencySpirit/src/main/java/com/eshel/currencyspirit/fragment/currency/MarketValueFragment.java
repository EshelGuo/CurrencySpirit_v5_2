package com.eshel.currencyspirit.fragment.currency;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eshel.currencyspirit.CurrencySpiritApp;
import com.eshel.currencyspirit.R;
import com.eshel.currencyspirit.util.UIUtil;
import com.eshel.currencyspirit.widget.RecycleViewDivider;
import com.eshel.currencyspirit.widget.util.Config;
import com.eshel.currencyspirit.widget.util.LoadMoreView;
import com.eshel.model.CurrencyModel;
import com.eshel.viewmodel.BaseViewModel;
import com.eshel.viewmodel.CurrencyViewModel;
import com.lhh.ptrrv.library.PullToRefreshRecyclerView;

import java.util.List;

import baseproject.base.BaseFragment;
import baseproject.util.DensityUtil;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * createBy Eshel
 * createTime: 2017/10/15 14:57
 * desc: 市值
 */

public class MarketValueFragment extends CurrencyBaseFragment {

	@Override
	public void loadData(BaseViewModel.Mode mode) {
		CurrencyViewModel.getMarketValueData(mode);
	}

	@Override
	public CurrencyModel.BaseModel getBaseMode() {
		return CurrencyModel.martetValueModel;
	}

	@Override
	protected void refreshData() {
		CurrencyViewModel.refreshMarketValueData();
	}

	{
/*
		private PullToRefreshRecyclerView mRv_currency;
		private MarketValueAdapter mMarketValueAdapter;

		@Override
		public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CurrencyViewModel.getMarketValueData(BaseViewModel.Mode.NORMAL);
	}

		@Nullable
		@Override
		public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

		@Override
		protected void reloadData() {
		CurrencyViewModel.getMarketValueData(BaseViewModel.Mode.NORMAL);
	}

		@Override
		public View getLoadSuccessView() {
		View root = View.inflate(getActivity(), R.layout.view_currency_child, null);
		mRv_currency = (PullToRefreshRecyclerView) root.findViewById(R.id.rv_currency);
		mRv_currency.setSwipeEnable(true);//open swipe
		mRv_currency.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
		mRv_currency.getRecyclerView().addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL,
				DensityUtil.dp2px(Config.dividerHeight), UIUtil.getColor(R.color.dividerColor), DensityUtil.dp2px(10), DensityUtil.dp2px(10)));
		mMarketValueAdapter = new MarketValueAdapter();
		mRv_currency.setAdapter(mMarketValueAdapter);

		LoadMoreView loadMoreView = new LoadMoreView(getActivity(), mRv_currency.getRecyclerView());
		loadMoreView.setLoadmoreString(getString(R.string.string_loadmore));
		loadMoreView.setLoadMorePadding(100);
		mRv_currency.setLoadMoreFooter(loadMoreView);
		//remove header
		mRv_currency.removeHeader();
		// set true to open swipe(pull to refresh, default is true)
		// set PagingableListener
		mRv_currency.setPagingableListener(new PullToRefreshRecyclerView.PagingableListener() {
			@Override
			public void onLoadMoreItems() {
				CurrencyViewModel.getMarketValueData(BaseViewModel.Mode.LOADMORE);
			}
		});

		// set OnRefreshListener
		mRv_currency.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				mRv_currency.setRefreshing(true);
				CurrencySpiritApp.getApp().getHandler().postDelayed(new Runnable() {
					@Override
					public void run() {
						CurrencyViewModel.refreshMarketValueData();
						mRv_currency.setOnRefreshComplete();
						mRv_currency.onFinishLoading(true, false);
					}
				}, 1000);
			}
		});
		mRv_currency.onFinishLoading(true, false);
		return root;
	}

		@Override
		public void notifyView() {
		if (CurrencyModel.MarketValueModel.loadDataCount < 20)
			mRv_currency.onFinishLoading(false, false);
		else
			mRv_currency.onFinishLoading(true, false);
		mMarketValueAdapter.notifyDataSetChanged();
	}

		public class MarketValueAdapter extends RecyclerView.Adapter<MarketValueFragment.MarketValueViewHolder> {

			@Override
			public MarketValueFragment.MarketValueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
				return new MarketValueFragment.MarketValueViewHolder();
			}

			@Override
			public void onBindViewHolder(MarketValueFragment.MarketValueViewHolder holder, int position) {
				holder.bindDataToView(CurrencyModel.MarketValueModel.getDataByPosition(position));
			}

			@Override
			public int getItemCount() {
				List<CurrencyModel> marketValueData = CurrencyModel.MarketValueModel.marketValueData;
				if (marketValueData!= null)
					return marketValueData.size();
				return 0;
			}
		}

		public class MarketValueViewHolder extends RecyclerView.ViewHolder {

			@BindView(R.id.tv_rank_chinesename)
			TextView tvRankChinesename;
			@BindView(R.id.tv_symbo)
			TextView tvSymbo;
			@BindView(R.id.tv_turnnumber)
			TextView tvTurnnumber;
			@BindView(R.id.tv_percent)
			TextView tvPercent;
			@BindView(R.id.tv_price)
			TextView tvPrice;

			public MarketValueViewHolder() {
				super(View.inflate(getActivity(), R.layout.item_currency, null));
				ButterKnife.bind(this,itemView);
				itemView.setBackgroundResource(R.drawable.item_selector);
				tvPercent.setWidth(UIUtil.getScreenWidth() / 5);
			}

			public void bindDataToView(CurrencyModel currencyModel) {
				tvSymbo.setText(currencyModel.symbol);
				tvRankChinesename.setText(String.format("#%d, %s",currencyModel.rank,currencyModel.chinesename));
				tvTurnnumber.setText(CurrencyModel.moneyFormat(UIUtil.getString(R.string.market_value)+"$",currencyModel.turnnumber));
				tvPrice.setText(CurrencyModel.moneyFormat("$",currencyModel.price));
				String percent = new java.text.DecimalFormat("######0.00").format(currencyModel.percent);
				if(!percent.contains("-")){
					if(!percent.contains("+")){
						percent = "+" + percent;
					}
				}
				tvPercent.setText(percent +"%");
				tvPercent.setBackgroundResource(
						currencyModel.percent < 0 ? R.drawable.drawable_percent_down : R.drawable.drawable_percent_up);
				itemView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
					}
				});
			}
		}*/
	}
}
