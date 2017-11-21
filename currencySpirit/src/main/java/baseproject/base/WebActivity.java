package baseproject.base;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import com.eshel.currencyspirit.CurrencySpiritApp;
import com.eshel.currencyspirit.R;
import com.eshel.currencyspirit.util.ThreadUtil;
import com.eshel.currencyspirit.util.UIUtil;
import com.eshel.currencyspirit.widget.night.NightViewUtil;
import com.tencent.smtt.sdk.WebViewClient;

import baseproject.util.DensityUtil;
import baseproject.util.Log;
import baseproject.util.NetUtils;
import baseproject.util.StringUtils;
import baseproject.util.WebImageUtil;
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
	private volatile int truthProgress;

	private String url;
	private boolean isReadLoad = false;
	private boolean loadedJS = false;

	private final int minProgress = 5;
	private final int maxProgress = 30;

	private final int minProgressTime = 10;
	private final int maxProgressTime = 100;

	private Runnable moveProgressTask = new Runnable() {
		@Override
		public void run() {
			int progress = mProgressBar.getProgress();
			if(truthProgress == 100 && progress == 100) {
				truthProgress = 0;
				mProgressBar.setVisibility(View.GONE);
				if(isReadLoad){
					mWebView.clearHistory();
					isReadLoad = false;
				}
			}else {
				int time = 50;
				if(mProgressBar.getVisibility() == View.GONE)
					mProgressBar.setVisibility(View.VISIBLE);
				if(progress < truthProgress){
					if(truthProgress != 100) {
						int temp = truthProgress - progress;
						if (temp < minProgress)
							temp = minProgress;
						if (temp > maxProgress)
							temp = maxProgress;
						time = (int) ((1.0 - temp * 1.0 / maxProgress) * maxProgressTime);
						if (time < minProgressTime)
							time = minProgressTime;
						if (time > maxProgressTime)
							time = maxProgressTime;
					}else {
						time = /*minProgressTime/4*/1;
					}
					if(time == 1){
						progress++;
					}
					progress++;
					mProgressBar.setProgress(progress);
					mProgressBar.invalidate();
					if(progress >= tempProgress){
						// 夜间模式 加载到 10 20 30 40... 进行加载 js 代码渲染html网页, 切换夜间模式
						tempProgress=((int)(progress*1.0f/10.0f))*10+10;
						if(tempProgress > 100)
							tempProgress = 100;
						if(progress == 100)
							tempProgress = 0;
						if (NightViewUtil.getNightMode()) {
							//mWebView.loadUrl("javascript:(function(){css = document.createElement('link');  css.id = 'xxx_browser_2014';  css.rel = 'stylesheet';  css.href = 'data:text/css,html,body,header,div,a,span,table,tr,td,th,tbody,p,form,input,ul,ol,li,dl,dt,dd,section,footer,nav,h1,h2,h3,h4,h5,h6,em,pre{background: #333 !important;color:#616161!important;border-color:#454530!important;text-shadow:0!important;-webkit-text-fill-color : none!important;}html a,html a *{color:#5a8498!important;text-decoration:underline!important;}html a:visited,html a:visited *,html a:active,html a:active *{color:#505f64!important;}html a:hover,html a:hover *{color:#cef!important;}html input,html select,html button,html textarea{background:#4d4c40!important;border:1px solid #5c5a46!important;border-top-color:#494533!important;border-bottom-color:#494533!important;}html input[type=button],html input[type=submit],html input[type=reset],html input[type=image],html button{border-top-color:#494533!important;border-bottom-color:#494533!important;}html input:focus,html select:focus,html option:focus,html button:focus,html textarea:focus{background:#5c5b3e!important;color:#fff!important;border-color:#494100 #635d00 #474531!important;outline:1px solid #041d29!important;}html input[type=button]:focus,html input[type=submit]:focus,html input[type=reset]:focus,html input[type=image]:focus,html button:focus{border-color:#494533 #635d00 #474100!important;}html input[type=radio]{background:none!important;border-color:#333!important;border-width:0!important;}html img[src],html input[type=image]{opacity:.5;}html img[src]:hover,html input[type=image]:hover{opacity:1;}html,html body {scrollbar-base-color: #4d4c40 !important;scrollbar-face-color: #5a5b3c !important;scrollbar-shadow-color: #5a5b3c !important;scrollbar-highlight-color: #5c5b3c !important;scrollbar-dlight-color: #5c5b3c !important;scrollbar-darkshadow-color: #474531 !important;scrollbar-track-color: #4d4c40 !important;scrollbar-arrow-color: #000 !important;scrollbar-3dlight-color: #6a6957 !important;}dt a{background-color: #333 !important;}';  document.getElementsByTagName('head')[0].appendChild(css);   })(); ");
							/*mWebView.loadUrl("javascript:nightMode();" +
									"function nightMode(){" +
									"document.getElementsByTagName('body')[0].style.backgroundColor='#2e2e2e';}");*/
						}
					}
				}
				CurrencySpiritApp.postDelayed(this, time);
			}
		}
	};
	private int downX;
	private int downY;

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
	public boolean loadFailed;
	private int tempProgress;
	private void initWebView() {
		mWebView.setDayOrNight(!NightViewUtil.getNightMode());
		//避免输入法界面弹出后遮挡输入光标的问题
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		if(NightViewUtil.getNightMode()) {
			mWebView.setBackgroundColor(Color.parseColor("#2e2e2e"));
			mProgressBar.setBackgroundColor(Color.parseColor("#2e2e2e"));
		}
		WebSettings settings = mWebView.getSettings();

		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setAllowFileAccess(true);
		settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		settings.setSupportMultipleWindows(true);
		// webSetting.setLoadWithOverviewMode(true);
		settings.setAppCacheEnabled(true);
		// webSetting.setDatabaseEnabled(true);
		settings.setDomStorageEnabled(true);
		settings.setGeolocationEnabled(true);
		settings.setAppCacheMaxSize(Long.MAX_VALUE);
		// webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
		settings.setPluginState(WebSettings.PluginState.ON_DEMAND);
		// webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		settings.setJavaScriptEnabled(true);
		settings.setCacheMode(NetUtils.hasNetwork(this) ? WebSettings.LOAD_DEFAULT : WebSettings.LOAD_CACHE_ELSE_NETWORK);
		addJavascriptInterface();
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
//		settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		settings.setDisplayZoomControls(false);
		mWebView.setWebViewClient(new WebViewClient(){

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				//String imageOnClick = "javascript:(function(){var imgs=document.getElementsByTagName(\"img\");for(var i=0;i<imgs.length;i++){imgs[i].onclick=function(){window.BigImage.showBigImg(this.src);}}})()";
				//网页Image标签点击事件
//				String imageOnLongClick = "javascript:function holdDown(var src){timeStart=getTimeNow();time=setInterval(function(){timeEnd=getTimeNow();if(timeEnd-timeStart>1000){clearInterval(time);window.BigImage.showBigImg(src);}},100);}function holdUp(){clearInterval(time);}(function(){var imgs=document.getElementsByTagName(\"img\");for(var i=0;i<imgs.length;i++){imgs[i].onmousedown=holdDown(this.src);imgs[i].onmouseup=holdUp();}})();var timeStart,timeEnd,time;function getTimeNow(){var now=new Date();return now.getTime();}";
				//mWebView.loadUrl(imageOnClick);
				if("file:///android_asset/currency/offline.html".equals(url)){
					if(NightViewUtil.getNightMode()) {
						//mWebView.loadUrl("javascript:nightMode()");
					}
				}else {
					loadedJS = false;
					if(NightViewUtil.getNightMode()) {
						/*view.loadUrl("javascript:nightMode();" +
								"function nightMode(){" +
								"document.getElementsByTagName('body')[0].style.backgroundColor='#2e2e2e';" +
								"document.getElementsByTagName('body')[0].style.color='#bbffffff'}");*/
						//view.loadUrl("javascript:(function(){css = document.createElement('link');  css.id = 'xxx_browser_2014';  css.rel = 'stylesheet';  css.href = 'data:text/css,html,body,header,div,a,span,table,tr,td,th,tbody,p,form,input,ul,ol,li,dl,dt,dd,section,footer,nav,h1,h2,h3,h4,h5,h6,em,pre{background: #333 !important;color:#616161!important;border-color:#454530!important;text-shadow:0!important;-webkit-text-fill-color : none!important;}html a,html a *{color:#5a8498!important;text-decoration:underline!important;}html a:visited,html a:visited *,html a:active,html a:active *{color:#505f64!important;}html a:hover,html a:hover *{color:#cef!important;}html input,html select,html button,html textarea{background:#4d4c40!important;border:1px solid #5c5a46!important;border-top-color:#494533!important;border-bottom-color:#494533!important;}html input[type=button],html input[type=submit],html input[type=reset],html input[type=image],html button{border-top-color:#494533!important;border-bottom-color:#494533!important;}html input:focus,html select:focus,html option:focus,html button:focus,html textarea:focus{background:#5c5b3e!important;color:#fff!important;border-color:#494100 #635d00 #474531!important;outline:1px solid #041d29!important;}html input[type=button]:focus,html input[type=submit]:focus,html input[type=reset]:focus,html input[type=image]:focus,html button:focus{border-color:#494533 #635d00 #474100!important;}html input[type=radio]{background:none!important;border-color:#333!important;border-width:0!important;}html img[src],html input[type=image]{opacity:.5;}html img[src]:hover,html input[type=image]:hover{opacity:1;}html,html body {scrollbar-base-color: #4d4c40 !important;scrollbar-face-color: #5a5b3c !important;scrollbar-shadow-color: #5a5b3c !important;scrollbar-highlight-color: #5c5b3c !important;scrollbar-dlight-color: #5c5b3c !important;scrollbar-darkshadow-color: #474531 !important;scrollbar-track-color: #4d4c40 !important;scrollbar-arrow-color: #000 !important;scrollbar-3dlight-color: #6a6957 !important;}dt a{background-color: #333 !important;}';  document.getElementsByTagName('head')[0].appendChild(css);   })(); ");
					}
				}
				super.onPageFinished(view, url);
				/*view.postDelayed(new Runnable() {
					@Override
					public void run() {
						String  js = "document.getElementsByTagName('body')[0].style.background='#2e2e2e'";
					}
				},500);*/

			}

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
				truthProgress = newProgress;
				super.onProgressChanged(view, newProgress);
			}
		});
		initWebViewEvent();
	}

	private void initWebViewEvent() {
		mWebView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				downX = (int) event.getX();
				downY = (int) event.getY();
				return false;
			}
		});
		mWebView.getView().setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(final View v) {
				final WebView.HitTestResult result = mWebView.getHitTestResult();
				if (null == result)
					return false;
				int type = result.getType();
				if (type == WebView.HitTestResult.UNKNOWN_TYPE)
					return false;
				if (type == WebView.HitTestResult.EDIT_TEXT_TYPE) {
				}
				// 这里可以拦截很多类型，我们只处理图片类型就可以了
				switch (type) {
					case WebView.HitTestResult.PHONE_TYPE: // 处理拨号
						break;
					case WebView.HitTestResult.EMAIL_TYPE: // 处理Email
						break;
					case WebView.HitTestResult.GEO_TYPE: // TODO
						break;
					case WebView.HitTestResult.SRC_ANCHOR_TYPE: // 超链接
						break;
					case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE:
						break;
					case WebView.HitTestResult.IMAGE_TYPE: // 处理长按图片的菜单项
						WebActivity.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								UIUtil.debugShortToast("长按图片");
								int popupWidth = UIUtil.getScreenWidth() / 3;
								int popupHight = (int) (popupWidth * 2 / 2.5f);
								Log.i(popupWidth);
								Log.i(popupHight);
								View root = View.inflate(mWebView.getContext(),R.layout.layout_image_popupwindow, null);
								LinearLayout view = (LinearLayout) root;
								view.setOrientation(LinearLayout.VERTICAL);
								view.setBackgroundColor(Color.WHITE);
								view.setLayoutParams(new ViewGroup.LayoutParams(popupWidth,popupHight));
								// 相应长按事件弹出菜单
								final PopupWindow popupWindow = new PopupWindow(view,popupWidth,popupHight,true);
								popupWindow.setFocusable(true);
								popupWindow.setOutsideTouchable(false);
								popupWindow.setBackgroundDrawable(new BitmapDrawable());
								// 获取图片的路径
								final String imgUrl = result.getExtra();
								Button lookImg = (Button) view.findViewById(R.id.look_img);
								Button saveImg = (Button) view.findViewById(R.id.save_img);
								lookImg.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										WebImageUtil.lookImg(WebActivity.this,imgUrl);
										popupWindow.dismiss();
									}
								});
								saveImg.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										WebImageUtil.saveImg(imgUrl);
										popupWindow.dismiss();
									}
								});
								//通过GestureDetector获取按下的位置，来定位PopWindow显示的位置
								popupWindow.showAtLocation(v, Gravity.LEFT|Gravity.TOP, downX + DensityUtil.dp2px(40), downY - popupHight/2);
							}
						});
						break;
					default:
						break;
				}
				return true;
			}
		});
	}

	public void addJavascriptInterface(){
		mWebView.addJavascriptInterface(new LoadFailedJs(this),"LoadFailedJs");
//		mWebView.addJavascriptInterface(new BigImage(this),"BigImage");
	}

	public abstract View initTitleView();
	public void loadUrl(String url){
		/*if(NightViewUtil.getNightMode()) {
			mWebView.switchNightMode(true);
		}else {
			mWebView.switchNightMode(false);
		}*/
		this.url = url;
		StringUtils.debugCopyStringToClipboard(url);
		Log.i("url: "+ url);
		CurrencySpiritApp.getApp().getHandler().removeCallbacks(moveProgressTask);
		CurrencySpiritApp.post(moveProgressTask);
		if(ThreadUtil.isMainThread()) {
			loadUrlAndJS(url);
		} else {
			ThreadUtil.getHandler().post(new Runnable() {
				@Override
				public void run() {
					loadUrlAndJS(WebActivity.this.url);
				}
			});
		}
	}

	private void loadUrlAndJS(String url){
		mWebView.loadUrl(url);
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
		ViewGroup viewGroup = (ViewGroup) mWebView.getParent();
		if(viewGroup != null)
			viewGroup.removeView(mWebView);
		mWebView.removeAllViews();
		mWebView.destroy();
		LoadFailedJs.close();
		BigImage.close();
		super.onDestroy();
	}

	public void reLoad() {
		isReadLoad = true;
		mWebView.clearHistory();
		loadUrl(url);
	}
	private static class BigImage{
		private static BigImage mBigImage;
		public static void close(){
			if(mBigImage != null) {
				mBigImage.webActivity = null;
				mBigImage = null;
			}
		}
		WebActivity webActivity;

		public BigImage(WebActivity webActivity) {
			this.webActivity = webActivity;
			mBigImage = this;
		}

		@JavascriptInterface
		public void showBigImg(String src) {
			UIUtil.debugToast("imageurl: "+src);
		}
	}

	private static class LoadFailedJs {
		private static LoadFailedJs mLoadFailedJs;
		public static void close(){
			if(mLoadFailedJs != null) {
				mLoadFailedJs.webActivity = null;
				mLoadFailedJs = null;
			}
		}
		WebActivity webActivity;

		public LoadFailedJs(WebActivity webActivity) {
			this.webActivity = webActivity;
			mLoadFailedJs = this;
		}

		@JavascriptInterface
		public void reLoad() {
			UIUtil.debugToast("reload");
			webActivity.loadFailed = false;
			if (ThreadUtil.isMainThread())
				webActivity.reLoad();
			else
				ThreadUtil.getHandler().post(new Runnable() {
						@Override
						public void run() {
							webActivity.reLoad();
						}
					});
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
