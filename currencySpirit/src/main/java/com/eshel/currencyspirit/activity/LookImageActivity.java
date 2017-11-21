package com.eshel.currencyspirit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.eshel.currencyspirit.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import baseproject.base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guoshiwen on 2017/11/18.
 */

public class LookImageActivity extends BaseActivity {
	public static final String key_url = "key_url";
	@BindView(R.id.photoview)
	PhotoView mPhotoview;
	private String mImgUrl;
	private PhotoViewAttacher mAttacher;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_look_image);
		ButterKnife.bind(this,this);
		hideTitle();
		Intent intent = getIntent();
		if (intent != null) {
			mImgUrl = intent.getStringExtra(key_url);
		}
		mAttacher = new PhotoViewAttacher(mPhotoview);
		Glide.with(this)
				.load(mImgUrl)
				.error(R.drawable.default_image)
				.into(mPhotoview);
		mAttacher.update();
	}
}
