package com.eshel.currencyspirit.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;

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
		ButterKnife.bind(this, this);
		hideTitle();
		Intent intent = getIntent();
		if (intent != null) {
			mImgUrl = intent.getStringExtra(key_url);
		}
		mAttacher = new PhotoViewAttacher(mPhotoview);
		initPhotoView();
		if (mImgUrl != null && mImgUrl.startsWith("http")) {
			Glide.with(this)
					.load(mImgUrl)
					.error(R.drawable.default_image)
					.into(mPhotoview);
		} else if (isBase64()) {
			mPhotoview.setImageBitmap(base64ToBitmap(mImgUrl));
		}
		mAttacher.update();
	}

	private void initPhotoView() {
	}

	public boolean isBase64() {
		if(mImgUrl!= null) {
			if(mImgUrl.startsWith("data:image/") && mImgUrl.contains(";base64")){
				return true;
			}else {
				return false;
			}
		}
		return false;
	}
	public static Bitmap base64ToBitmap(String base64Data) {
		base64Data = base64Data.substring(base64Data.indexOf(";base64")+";base64".length(),base64Data.length());
		byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}
}
