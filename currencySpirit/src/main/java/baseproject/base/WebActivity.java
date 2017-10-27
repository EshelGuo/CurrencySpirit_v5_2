package baseproject.base;

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
import com.eshel.currencyspirit.util.ThreadUtil;
import com.eshel.currencyspirit.util.UIUtil;

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
	protected WebView mWebView;

	private String url;
	private boolean isReadLoad = false;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_base);
		hideActionBar();
		ButterKnife.bind(this,getContentView());
		View view = initTitleView();
		if(view != null)
			mTitle.addView(view,
					new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		mProgressBar.setProgress(0);
		initWebView();
		setSwipeBackEnable(true);
	}
	private boolean loadFailed;
	private void initWebView() {
		WebSettings settings = mWebView.getSettings();

		settings.setCacheMode(NetUtils.hasNetwork(this) ? WebSettings.LOAD_DEFAULT : WebSettings.LOAD_CACHE_ELSE_NETWORK);
		settings.setJavaScriptEnabled(true);
		mWebView.addJavascriptInterface(new LoadFailedJs(this),"LoadFailedJs");

		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		settings.setDisplayZoomControls(false);

		mWebView.setWebViewClient(new WebViewClient(){
			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				view.loadUrl("file:///android_asset/currency/offline.html");
				loadFailed = true;
				super.onReceivedError(view, errorCode, description, failingUrl);
			}
		});
		mWebView.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
//				Log.i(newProgress);
				if(newProgress == 100){
					mProgressBar.setVisibility(View.GONE);
					if(isReadLoad){
						mWebView.clearHistory();
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
			mWebView.loadUrl(url);
		else
			ThreadUtil.getHandler().post(new Runnable() {
				@Override
				public void run() {
					mWebView.loadUrl(WebActivity.this.url);
				}
			});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(loadFailed)
				return super.onKeyDown(keyCode, event);
			if (mWebView.canGoBack()) {
				mWebView.goBack();//返回上一页面
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mWebView.removeAllViews();
		mWebView.destroy();
	}

	public void reLoad() {
		isReadLoad = true;
		mWebView.clearHistory();
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
			loadFailed = false;
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

	/**
	 * 1 到 72 之间, 默认是 16
	 * @param fontsize 字体大小
	 */
	public void setFontsize(int fontsize){
		mWebView.getSettings().setDefaultFontSize(fontsize);
	}
	/**
	 * LOAD_CACHE_ELSE_NETWORK	只要本地有缓存, 无论是否过期或者 no-cache 都使用缓存中的数据
	 * LOAD_CACHE_ONLY 			不使用网络, 只读取本地缓存数据
	 * LOAD_DEFAULT				默认, 根据 cache-control 决定是否从网络上取数据
	 * LOAD_NORMAL				已废弃, 作用跟 LOAD_DEFAULT 相同
	 * LOAD_NO_CACHE			不使用缓存, 只从网络获取数据
	 * @param cacheMode			缓存模式
	 */
	public void setCacheMode(CacheMode cacheMode){
		mWebView.getSettings().setCacheMode(cacheMode.mode);
	}
	public enum CacheMode{
		LOAD_CACHE_ELSE_NETWORK(WebSettings.LOAD_CACHE_ELSE_NETWORK),
		LOAD_CACHE_ONLY(WebSettings.LOAD_CACHE_ONLY),
		LOAD_DEFAULT(WebSettings.LOAD_DEFAULT),
		LOAD_NO_CACHE(WebSettings.LOAD_NO_CACHE);
		public int mode;
		CacheMode(int mode){
			this.mode = mode;
		}
	}

	public WebSettings getSettings(){
		return mWebView.getSettings();
	}
}
