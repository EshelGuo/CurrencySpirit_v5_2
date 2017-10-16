package baseproject.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import java.util.LinkedHashMap;
import java.util.Map;

import baseproject.util.DataUtil;
import baseproject.util.Log;

/**
 * 项目名称: BaseProject
 * 创建人: Eshel
 * 创建时间:2017/10/2 14时42分
 * 描述: TODO
 */

public class BaseActivity extends AppCompatActivity {
	protected String TAG = "defaultActivity";
	public static BaseActivity getActivity(Class clazz){
		return activitys.get(clazz);
	}
	private static BaseActivity topActivity;
	private static LinkedHashMap<Class,BaseActivity> activitys = new LinkedHashMap<>();
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		TAG = getClass().getSimpleName();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onCreate(savedInstanceState);
		activitys.put(getClass(),this);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		topActivity = this;
		if(topActivity != null)
			Log.i("curentTopActivity: "+topActivity);
		else
			Log.i("curentTopActivity: null");
	}

	@Override
	protected void onPause() {
		super.onPause();
		topActivity = null;
		if(topActivity != null)
			Log.i("curentTopActivity: "+topActivity);
		else
			Log.i("curentTopActivity: null");
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		activitys.remove(getClass());
		super.onDestroy();
	}

	public void hideActionBar(){
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null)
			actionBar.hide();
	}
	public static BaseActivity getTopActivity(){
		return topActivity;
	}
	public static BaseActivity getLastActivity(){
		try {
			Map.Entry<Class, BaseActivity> entry = DataUtil.getTailByReflection(activitys);
			return entry.getValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
