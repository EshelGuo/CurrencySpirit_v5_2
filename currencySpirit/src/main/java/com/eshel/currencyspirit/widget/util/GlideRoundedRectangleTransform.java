package com.eshel.currencyspirit.widget.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by guoshiwen on 2017/10/11.
 */

public class GlideRoundedRectangleTransform extends BitmapTransformation {
	private int roundR;
	public GlideRoundedRectangleTransform(Context context) {
		this(context,5);
	}
	public GlideRoundedRectangleTransform(Context context,int roundR) {
		super(context);
		this.roundR = roundR;
	}

	@Override protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
		return circleCrop(pool, toTransform);
	}

	private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
		if (source == null) return null;

		RectF rectF = new RectF(0,0,source.getWidth(),source.getHeight());
		// TODO this could be acquired from the pool too
		Bitmap squared = Bitmap.createBitmap(source);

		Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
		if (result == null) {
			result = Bitmap.createBitmap(source.getWidth(),source.getHeight(), Bitmap.Config.ARGB_8888);
		}

		Canvas canvas = new Canvas(result);
		Paint paint = new Paint();
		paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
		paint.setAntiAlias(true);
		canvas.drawRoundRect(rectF,roundR,roundR, paint);
		return result;
	}

	@Override public String getId() {
		return getClass().getName();
	}

}
