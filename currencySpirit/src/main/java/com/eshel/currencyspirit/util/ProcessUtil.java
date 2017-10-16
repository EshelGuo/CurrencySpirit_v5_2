package com.eshel.currencyspirit.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import com.eshel.currencyspirit.CurrencySpiritApp;

import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by guoshiwen on 2017/10/13.
 */

public class ProcessUtil {

	private static ActivityManager mAm;

	/**
	 * 判断 APP 是否在前台
	 * @return
	 */
	public static boolean appIsForeground(){
		Context context = CurrencySpiritApp.getContext();
		if(mAm == null)
			mAm = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> tasks = mAm.getRunningTasks(1);
		if(!tasks.isEmpty()){
			ComponentName topActivity = tasks.get(0).topActivity;
			if(topActivity.getPackageName().equals(context.getPackageName())){
				return true;
			}
		}
		return false;
	}
	public static void moveAppToForeground(){
		Context context = CurrencySpiritApp.getContext();
		if(mAm == null)
			mAm = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> tasks = mAm.getRunningTasks(1000);
		for (ActivityManager.RunningTaskInfo task : tasks) {
			if(task.topActivity.getPackageName().equals(context.getPackageName())){
				mAm.moveTaskToFront(task.id,ActivityManager.MOVE_TASK_WITH_HOME);
			}
		}
	}
	public static String getCurrentProcessName(Context context){
		int pid = android.os.Process.myPid();
		ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
		for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : am.getRunningAppProcesses()) {
			if(runningAppProcessInfo.pid == pid){
				return runningAppProcessInfo.processName;
			}
		}
		return null;
	}
}
