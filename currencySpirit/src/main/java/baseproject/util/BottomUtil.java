package baseproject.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewTreeObserver;

import java.lang.reflect.Method;

/**
 * Created by guoshiwen on 2017/11/11.
 */

public class BottomUtil {

	@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
	public static void setAutoHideBottomBar(final Activity activity, final long time){
		try {
			if (Build.VERSION.SDK_INT >= 19) {
				//透明状态栏
				activity.getWindow().addFlags(0x04000000);
				//透明导航栏
				activity.getWindow().addFlags(0x08000000);
			}
			if(checkDeviceHasNavigationBar(activity))
				hideBottomUIMenu(activity);
			final View view = activity.getWindow().getDecorView();
			view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					try {
						if(checkDeviceHasNavigationBar(activity))
							view.postDelayed(new Runnable() {
								@Override
								public void run() {
									hideBottomUIMenu(activity);
								}
							}, time);
					}catch (Exception e){
					    e.printStackTrace();
					}catch (Error e){
					    e.printStackTrace();
					}
				}
			});
		}catch (Exception e){
		    e.printStackTrace();
		}catch (Error e){
		    e.printStackTrace();
		}
	}
	@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
	public static boolean checkDeviceHasNavigationBar(Context context) {
		try {
			boolean hasNavigationBar = false;
			Resources rs = context.getResources();
			int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
			if (id > 0) {
				hasNavigationBar = rs.getBoolean(id);
			}
			try {
				Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
				Method m = systemPropertiesClass.getMethod("get", String.class);
				String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
				if ("1".equals(navBarOverride)) {
					hasNavigationBar = false;
				} else if ("0".equals(navBarOverride)) {
					hasNavigationBar = true;
				}
			} catch (Exception e) {

			}
			return hasNavigationBar;

		}catch (Exception e){
		    e.printStackTrace();
		}catch (Error e){
		    e.printStackTrace();
		}
		return false;
	}
	public static void hideBottomUIMenu(Activity activity) {
		try {
			//隐藏虚拟按键，并且全屏
			if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
				View v = activity.getWindow().getDecorView();
				v.setSystemUiVisibility(View.GONE);
			} else if (Build.VERSION.SDK_INT >= 19) {
				//for new api versions.
				View decorView = activity.getWindow().getDecorView();
				int uiOptions = 0x00000002
						| 0x00001000 | 0x00000004;
				decorView.setSystemUiVisibility(uiOptions);
			}
		}catch (Exception e){
		    e.printStackTrace();
		}catch (Error e){
		    e.printStackTrace();
		}
	}
}
