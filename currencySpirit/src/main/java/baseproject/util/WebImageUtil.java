package baseproject.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import com.eshel.currencyspirit.activity.LookImageActivity;
import com.eshel.currencyspirit.util.UIUtil;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;

/**
 * Created by guoshiwen on 2017/11/18.
 */

public class WebImageUtil {
	public static void lookImg(Activity activity, String url){
		if(!StringUtils.isEmpty(url)){
			Intent intent = new Intent(activity, LookImageActivity.class);
			intent.putExtra(LookImageActivity.key_url,url);
			activity.startActivity(intent);
		}
	}
	public static void saveImg(String url){
		UIUtil.debugToast("saveImg url: "+url);
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			UIUtil.toast("错误, 未检测到SD卡, 保存图片失败");
			return;
		}
		File root = Environment.getExternalStorageDirectory();
		File bdjl = new File(root,"bdjl");
		if(!bdjl.exists() || bdjl.isFile())
			bdjl.mkdir();
		final File imgdir = new File(bdjl,"image");
		if(!imgdir.exists() || imgdir.isFile())
			imgdir.mkdir();

		BaseDownloadTask task = FileDownloader.getImpl().create(url)
				.setPath(imgdir.getAbsolutePath()+"/"+url.substring(url.lastIndexOf("/")+1,url.length()))
				.setListener(new FileDownloadSampleListener() {
					@Override
					protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
					}
					@Override
					protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
					}
					@Override
					protected void error(BaseDownloadTask task, Throwable e) {
						UIUtil.toast("下载失败");
					}
					@Override
					protected void completed(BaseDownloadTask task) {
						UIUtil.toast("下载成功, 图片已保存至 "+imgdir.getAbsolutePath()+ " 目录");
					}
				});
				task.start();
	}
}
