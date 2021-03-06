package baseproject.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.eshel.currencyspirit.R;
import com.eshel.currencyspirit.activity.SplashActivity;
import com.eshel.currencyspirit.util.UIUtil;
import com.eshel.currencyspirit.widget.night.NightTextView;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import baseproject.base.BaseActivity;

/**
 * Created by guoshiwen on 2017/10/30.
 */

public class ViewUtil {
	public static void changeCurrentActivityStateBarColor(int color){
		BaseActivity topActivity = BaseActivity.getTopActivity();
		if(topActivity != null){
			if(!(topActivity instanceof SplashActivity)){
				changeStateBarColor(topActivity, color);
			}else {
				changeStateBarColor(topActivity, 0x00000000);
			}
		}
	}
	public static void changeCurrentActivityNavigationBarColor(int color){
		BaseActivity topActivity = BaseActivity.getTopActivity();
		if(topActivity != null){
			changeStateBarColor(topActivity,color);
		}
	}
	public static void changeStateBarColor(Activity context, int color){
		try {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				Window window = context.getWindow();
				window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
				if(!(context instanceof SplashActivity)){
					window.setStatusBarColor(color);
				}else {
					window.setStatusBarColor(0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void changeNavigationBarColor(Activity context, int color){
		try {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				Window window = context.getWindow();
				window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
				window.setNavigationBarColor(color);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Deprecated
	public static void findAllViewById(@NonNull Object taget,@NonNull View view,Class R_ID){
		if(taget == null){
			throw new NullPointerException("taget 不能为 null");
		}
		if(view == null){
			throw new NullPointerException("view 不能为 null");
		}
		Field[] tagets = taget.getClass().getDeclaredFields();
		for (Field field : tagets) {
			if(View.class.isAssignableFrom(field.getType())){
				String idName = parseNameForIdName(field.getName());
				try {
					Field idField = R_ID.getField(idName);
					if(idField != null){
						int id = idField.getInt(null);
						View v = view.findViewById(id);
						if(v == null){
							Log.e(Config.TAG,"没有字段名为 "+idName+" 的View~!!!");
						}else {
							if(!Modifier.isPublic(field.getModifiers())){
								field.setAccessible(true);
								field.set(taget,v);
								field.setAccessible(false);
							}else
								field.set(taget,v);
						}
					}
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
					Log.e(Config.TAG,"字段名为 "+idName+" 资源ID字段找不到~!!!");
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					Log.e(Config.TAG,"字段名为 "+idName+" 资源ID值找不到~!!!");
				}
			}
		}
	}

	private static String parseNameForIdName(String name) {
		StringBuilder sb = new StringBuilder();
		boolean firstChar = true;
		char[] chars = name.toCharArray();
		for (char c : chars) {
			if(firstChar){
				firstChar = false;
				if(c == 'm'){
					chars[1] = Character.toLowerCase(chars[1]);
					continue;
				}
			}
			if(Character.isUpperCase(c)){
				sb.append("_");
				sb.append(Character.toLowerCase(c));
			}else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	public static void hideStateBar(Activity activity){
		int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
				| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				| View.SYSTEM_UI_FLAG_FULLSCREEN;
		uiFlags |= 0x00001000;
		activity.getWindow().getDecorView().setSystemUiVisibility(uiFlags);
	}
	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	public static void showStateBar(Activity activity){
		activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | 0x00001000);
	}
	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	public static void hideNavigationBar(Activity activity) {
		View decorView = activity.getWindow().getDecorView();
		int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_FULLSCREEN;
		decorView.setSystemUiVisibility(uiOptions);
	}

	@RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public static void showNavigationBar(Activity activity) {
		View decorView = activity.getWindow().getDecorView();
		int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
		decorView.setSystemUiVisibility(uiOptions);
	}
	public static TextView getLoadFailedView(Context context, TextView view, String msg){
		if(context == null&&view == null)
			return null;
		if(view == null){
			view = new NightTextView(context);
			view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		}
		view.setTextColor(UIUtil.getColor(R.color.black));
		view.setTextSize(18);
		view.setGravity(Gravity.CENTER);
		view.setText(msg);
		return view;
	}
	public static View getLoadingView(Context context){
		View view = View.inflate(context, R.layout.fragment_loading, null);
		TextView tv_loading = (TextView) view.findViewById(R.id.tv_loading);
		tv_loading.setTextColor(UIUtil.getColor(R.color.black));
		return view;
	}
}
