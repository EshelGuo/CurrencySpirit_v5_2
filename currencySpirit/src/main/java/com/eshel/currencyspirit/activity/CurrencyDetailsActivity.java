package com.eshel.currencyspirit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eshel.currencyspirit.R;
import com.eshel.currencyspirit.util.UIUtil;
import com.eshel.model.CurrencyModel;

import baseproject.base.WebActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guoshiwen on 2017/10/16.
 */

public class CurrencyDetailsActivity extends WebActivity {
	public static String key = "currency";
	private CurrencyModel mCurrencyModel;
	private TitleHolder mTitle;

	@Override
	public View initTitleView() {
		mTitle = new TitleHolder();
		return mTitle.mRoot;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if (intent != null) {
			mCurrencyModel = (CurrencyModel) intent.getSerializableExtra(key);
			loadUrl(mCurrencyModel.url);
		}
	}

	class TitleHolder {
		@BindView(R.id.tv_title)
		TextView mTvTitle;
		@BindView(R.id.tv_attention)
		TextView mTvAttention;
		@BindView(R.id.rl_title)
		RelativeLayout mRlTitle;
		private View mRoot;

		public TitleHolder() {
			mRoot = View.inflate(CurrencyDetailsActivity.this, R.layout.title_currency_details, null);
			ButterKnife.bind(this,mRoot);
			mTvAttention.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO: 2017/10/16  关注逻辑
					if(mTvAttention.getText().equals(UIUtil.getString(R.string.attention)))
						changeAttentionState(false);
					else
						changeAttentionState(true);
				}

				private void changeAttentionState(boolean isAttention) {
					if(isAttention){
						if(!mTvAttention.getText().equals(UIUtil.getString(R.string.attention))){
							mTvAttention.setText(R.string.attention);
							mTvAttention.setTextColor(UIUtil.getColor(R.color.violet));
						}
					}else {
						if(!mTvAttention.getText().equals(UIUtil.getString(R.string.unattention))){
							mTvAttention.setText(R.string.unattention);
							mTvAttention.setTextColor(UIUtil.getColor(R.color.text_gray));
						}
					}
				}
			});
		}
	}
}
