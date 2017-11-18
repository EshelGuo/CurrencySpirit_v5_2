package baseproject.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * Created by guoshiwen on 2017/10/30.
 */

public class Config {
	public static final String TAG = "BASE";

	public static boolean isApkDebugable(Context context) {
		try {
			ApplicationInfo info= context.getApplicationInfo();
			return (info.flags&ApplicationInfo.FLAG_DEBUGGABLE)!=0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
