package com.eshel.currencyspirit.fragment.currency;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eshel.currencyspirit.CurrencySpiritApp;
import com.eshel.currencyspirit.R;
import com.eshel.currencyspirit.activity.CurrencyDetailsActivity;
import com.eshel.currencyspirit.activity.SearchCurrencyActivity;
import com.eshel.currencyspirit.util.UIUtil;
import com.eshel.currencyspirit.widget.RecycleViewDivider;
import com.eshel.currencyspirit.widget.util.Config;
import com.eshel.currencyspirit.widget.util.LoadMoreView;
import com.eshel.model.CurrencyModel;
import com.eshel.viewmodel.BaseViewModel;
import com.lhh.ptrrv.library.PullToRefreshRecyclerView;

import java.util.List;

import baseproject.base.BaseFragment;
import baseproject.util.DensityUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guoshiwen on 2017/10/16.
 */

public abstract class CurrencyBaseFragment extends BaseFragment{

	private View mRoot;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loadData(BaseViewModel.Mode.NORMAL);
	}

	@Override
	protected void reloadData() {
		loadData(BaseViewModel.Mode.NORMAL);
	}
	//普通 loadData
	public abstract void loadData(BaseViewModel.Mode mode);
	public abstract CurrencyModel.BaseModel getBaseMode();
	protected abstract void refreshData();
	protected PullToRefreshRecyclerView mRv_currency;
	protected BaseAdapter mAdapter;
	@Override
	public View getLoadSuccessView() {
		if(mRoot == null) {
			mRoot = View.inflate(getActivity(), R.layout.view_currency_child, null);
			mRv_currency = (PullToRefreshRecyclerView) mRoot.findViewById(R.id.rv_currency);
			mRv_currency.getRecyclerView().addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL,
					DensityUtil.dp2px(Config.dividerHeight), UIUtil.getColor(R.color.dividerColor),
					DensityUtil.dp2px(10), DensityUtil.dp2px(10)));
			mRv_currency.setSwipeEnable(true);//open swipe
			mRv_currency.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
			mAdapter = new CurrencyBaseFragment.BaseAdapter();
			mRv_currency.setAdapter(mAdapter);

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
					loadData(BaseViewModel.Mode.LOADMORE);
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
							refreshData();
						}
					}, 0);
				}
			});
			mRv_currency.onFinishLoading(true, false);
		}

		return mRoot;
	}
	
	@Override
	public void notifyView() {
		mRv_currency.post(new Runnable() {
			@Override
			public void run() {
				mRv_currency.setOnRefreshComplete();
			}
		});
		if (getBaseMode().loadDataCount < 20)
			mRv_currency.onFinishLoading(false, false);
		else
			mRv_currency.onFinishLoading(true, false);
		mAdapter.notifyDataSetChanged();
	}

	public class BaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

		@Override
		public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			if(viewType == TYPE_ADD){
				return new AddViewHolder();
			}
			return new BaseViewHolder();
		}

		@Override
		public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
			if(holder instanceof BaseViewHolder) {
				BaseViewHolder baseViewHolder = (BaseViewHolder) holder;
				baseViewHolder.bindDataToView(getBaseMode().getDataByPosition(position));
			}else if(holder instanceof AddViewHolder){
				AddViewHolder addViewHolder = (AddViewHolder) holder;
				addViewHolder.bindDataToView();
			}
		}

		@Override
		public int getItemCount() {
			int extra = CurrencyBaseFragment.this instanceof SelfSelectFragment ? 1 : 0;
			List<CurrencyModel> data = getBaseMode().data;
			if (data!= null)
				return data.size() + extra;
			return extra;
		}

		@Override
		public int getItemViewType(int position) {
			if(CurrencyBaseFragment.this instanceof SelfSelectFragment && position == getItemCount() - 1)
				return TYPE_ADD;
			return super.getItemViewType(position);
		}
	}
	public final int TYPE_ADD = 1;//添加自选

	class AddViewHolder extends RecyclerView.ViewHolder{

		public AddViewHolder() {
			super(View.inflate(getActivity(),R.layout.item_add,null));
			itemView.setBackgroundResource(R.drawable.item_selector);
			itemView.setLayoutParams(new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,DensityUtil.dp2px(60)));

		}

		public void bindDataToView() {
			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					startActivity(new Intent(getActivity(), SearchCurrencyActivity.class));
				}
			});
		}
	}

	public class BaseViewHolder extends RecyclerView.ViewHolder {

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

		public BaseViewHolder() {
			super(View.inflate(getActivity(), R.layout.item_currency, null));
			ButterKnife.bind(this,itemView);
			itemView.setBackgroundResource(R.drawable.item_selector);
			tvPercent.setWidth(UIUtil.getScreenWidth() / 5);
		}

		public void bindDataToView(final CurrencyModel currencyModel) {
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
					Intent intent = new Intent(getActivity(), CurrencyDetailsActivity.class);
					intent.putExtra(CurrencyDetailsActivity.key,currencyModel);
					startActivity(intent);
				}
			});
		}
	}
}
