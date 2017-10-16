package baseproject.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.eshel.currencyspirit.R;
import com.eshel.currencyspirit.activity.HomeActivity;
import com.eshel.currencyspirit.util.ThreadUtil;
import com.eshel.currencyspirit.util.UIUtil;

import baseproject.util.DensityUtil;
import baseproject.util.NetUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guoshiwen on 2017/10/13.
 */

public abstract class WebActivity extends BaseActivity {

	@BindView(R.id.title)
	protected LinearLayout mTitle;
	@BindView(R.id.progress_bar)
	protected ProgressBar mProgressBar;
	@BindView(R.id.wv_essence)
	protected WebView mWvEssence;

	private String url;
	private boolean isReadLoad = false;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_base);
		hideActionBar();
		ButterKnife.bind(this);
		mTitle.addView(initTitleView(),
				new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			mTitle.setElevation(DensityUtil.dp2px(HomeActivity.titleElevation/2));
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			UIUtil.debugToast("elevation: "+mTitle.getElevation());
		}
		mProgressBar.setProgress(0);
		initWebView();
	}

	private void initWebView() {
		WebSettings settings = mWvEssence.getSettings();
//		settings.setDefaultFontSize();

		settings.setCacheMode(NetUtils.hasNetwork(this) ? WebSettings.LOAD_DEFAULT : WebSettings.LOAD_CACHE_ELSE_NETWORK);
		settings.setJavaScriptEnabled(true);
		mWvEssence.addJavascriptInterface(new LoadFailedJs(this),"LoadFailedJs");

		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);

		mWvEssence.setWebViewClient(new WebViewClient(){
			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				view.loadUrl("file:///android_asset/currency/offline.html");
				super.onReceivedError(view, errorCode, description, failingUrl);
			}
		});
		mWvEssence.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
//				Log.i(newProgress);
				if(newProgress == 100){
					mProgressBar.setVisibility(View.GONE);
					if(isReadLoad){
						mWvEssence.clearHistory();
						isReadLoad = false;
					}
				}else {
					if(mProgressBar.getVisibility() == View.GONE)
						mProgressBar.setVisibility(View.VISIBLE);
					mProgressBar.setProgress(newProgress);
					mProgressBar.invalidate();
				}
				super.onProgressChanged(view, newProgress);
			}
		});
	}

	public abstract View initTitleView();
	public void loadUrl(String url){
		this.url = url;
		if(ThreadUtil.isMainThread())
			mWvEssence.loadUrl(url);
		else
			ThreadUtil.getHandler().post(new Runnable() {
				@Override
				public void run() {
					mWvEssence.loadUrl(WebActivity.this.url);
				}
			});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mWvEssence.canGoBack()) {
				mWvEssence.goBack();//返回上一页面
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mWvEssence.removeAllViews();
		mWvEssence.destroy();
	}

	public void reLoad() {
		isReadLoad = true;
		mWvEssence.clearHistory();
		loadUrl(url);
	}

	public class LoadFailedJs {
		BaseActivity webActivity;

		public LoadFailedJs(BaseActivity webActivity) {
			this.webActivity = webActivity;
		}

		@JavascriptInterface
		public void reLoad() {
			UIUtil.debugToast("reload");
			if (webActivity instanceof WebActivity) {
				final WebActivity activity = (WebActivity) webActivity;
				if (ThreadUtil.isMainThread())
					activity.reLoad();
				else
					ThreadUtil.getHandler().post(new Runnable() {
						@Override
						public void run() {
							activity.reLoad();
						}
					});
			}
		}
	}
}
