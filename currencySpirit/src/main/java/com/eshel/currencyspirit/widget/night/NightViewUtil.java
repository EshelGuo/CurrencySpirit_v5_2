package com.eshel.currencyspirit.widget.night;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.eshel.config.AppConfig;
import com.eshel.config.AppConstant;
import com.eshel.currencyspirit.R;
import com.eshel.currencyspirit.util.UIUtil;

import java.util.ArrayList;

import baseproject.interfaces.Utilable;
import baseproject.util.ReflectUtil;
import baseproject.util.shape.ShapeUtil;

/**
 * Created by guoshiwen on 2017/10/31.
 */

public class NightViewUtil implements Utilable{
	private static Context mContext;
	private static ArrayList<Integer> dayResIds = new ArrayList<>();
	private static ArrayList<Integer> nightResIds = new ArrayList<>();

	private static ArrayList<Integer> dayColors = new ArrayList<>();
	private static ArrayList<Integer> nightColors = new ArrayList<>();
	public static void addColor(int dayColor,int nightColor){
		dayColors.add(dayColor);
		nightColors.add(nightColor);
	}
	public static void addResId(int dayResId,int nightResId){
		dayResIds.add(dayResId);
		nightResIds.add(nightResId);
	}
	public static void changeNightDrawable(Drawable drawable){
		if(NightViewUtil.getNightMode()) {
			NightViewUtil.setFilter(drawable);
		}else {
			NightViewUtil.removeFilter(drawable);
		}
	}
	public static int changeNightColor(int color){
		if(NightViewUtil.getNightMode()){
			int index = dayColors.indexOf(color);
			if(index != -1){
				color = nightColors.get(index);
			}
		}else {
			int index = nightColors.indexOf(color);
			if(index != -1){
				color = dayColors.get(index);
			}
		}
		return color;
	}
	public static int changeNightResId(int resId){
		if(NightViewUtil.getNightMode()) {
			int index = dayResIds.indexOf(resId);
			if(index != -1){
				resId = nightResIds.get(index);
			}
		}else {
			int index = nightResIds.indexOf(resId);
			if(index != -1){
				resId = dayResIds.get(index);
			}
		}
		return resId;
	}
	public static void changeNightBackgroundDrawable(Drawable drawable){
		if(drawable instanceof ColorDrawable){
			ColorDrawable colorDrawable = (ColorDrawable) drawable;
			colorDrawable.setColor(changeNightColor(colorDrawable.getColor()));
		}else if(drawable instanceof StateListDrawable){
			StateListDrawable stateListDrawable = (StateListDrawable) drawable;
			DrawableContainer.DrawableContainerState state =
					(DrawableContainer.DrawableContainerState) ReflectUtil.getFiledValue(stateListDrawable,"mStateListState");
			Drawable[] drawables = (Drawable[])ReflectUtil.getFiledValue(
					DrawableContainer.DrawableContainerState.class,state,"mDrawables");
			for (Drawable drawable1 : drawables) {
				if(drawable1 instanceof ColorDrawable){
					ColorDrawable colorDrawable = (ColorDrawable) drawable1;
					colorDrawable.setColor(changeNightColor(colorDrawable.getColor()));
				}else {
					if(NightViewUtil.getNightMode()) {
						NightViewUtil.setFilter(drawable);
					}else {
						NightViewUtil.removeFilter(drawable);
					}
				}
			}
		} else {
			if(NightViewUtil.getNightMode()) {
				NightViewUtil.setFilter(drawable);
			}else {
				NightViewUtil.removeFilter(drawable);
			}
		}
	}
	public static void setFilter(Drawable drawable){
		setFilter(drawable,Color.GRAY);
	}
	public static void setFilter(Drawable drawable,int color){
		if(drawable == null)
			return;
		drawable.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
	}
	public static void removeFilter(Drawable drawable){
		if(drawable == null)
			return;
		drawable.clearColorFilter();
	}

	public static void changeNightMode(boolean isNight, Fragment fragment){
		changeNightMode(isNight);
		if(fragment == null)
			return;
		if(fragment.getView() instanceof ViewGroup){
			changeNightMode(isNight,(ViewGroup)fragment.getView());
		}else {
			changeNightMode(isNight,fragment.getView());
		}
	}
	public static void onResume(Activity activity){
		changeNightMode(getNightMode(),activity);
	}

	public static void changeNightMode(boolean isNight, Activity activity) {
		changeNightMode(isNight);
		if(activity == null)
			return;
		changeNightMode(isNight,(ViewGroup)activity.getWindow().
				getDecorView().findViewById(Window.ID_ANDROID_CONTENT));
	}

	public static void changeNightMode(boolean isNight, ViewGroup parent){
		changeNightMode(isNight);
		if(parent == null)
			return;
		if(parent instanceof INight){
			((INight)(parent)).changeNightMode(isNight);
		}
		for (int i = 0; i < parent.getChildCount(); i++) {
			View child = parent.getChildAt(i);
			if(child instanceof ViewGroup){
				changeNightMode(isNight,(ViewGroup) child);
			}else {
				changeNightMode(isNight,child);
			}
		}
	}
	public static void changeNightMode(boolean isNight, View view){
		changeNightMode(isNight);
		if (view == null)
			return;
		if(view instanceof INight){
			INight nightView = (INight) view;
			nightView.changeNightMode(isNight);
		}
	}
	public static void changeNightMode(boolean isNight){
		if(getNightMode() != isNight){
			setNightMode(isNight);
		}
	}
	public static boolean getNightMode(){
		return AppConfig.isNight;
	}
	public static void setNightMode(boolean nightMode){
		if(nightMode != AppConfig.isNight) {
			AppConfig.isNight = nightMode;
			ShapeUtil.put(AppConstant.key_nightMode, nightMode);
		}
	}

	@Override
	public void init(Context context) {
		AppConfig.isNight = ShapeUtil.get(AppConstant.key_nightMode,false);
		mContext = context;
		AppConfig.nightConfig();
	}

	@Override
	public void deInit() {
		mContext = null;
		dayColors.clear();
		nightColors.clear();
		dayResIds.clear();
		nightResIds.clear();
	}
}
