package baseproject.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eshel.currencyspirit.R;
import com.tencent.stat.StatService;

import java.util.LinkedHashMap;
import java.util.Map;

import baseproject.util.DataUtil;
import baseproject.util.Log;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

/**
 * 项目名称: BaseProject
 * 创建人: Eshel
 * 创建时间:2017/10/2 14时42分
 * 描述: TODO
 */

public class BaseActivity extends AppCompatActivity implements SwipeBackActivityBase {
	private SwipeBackActivityHelper mHelper;
	protected String TAG = "defaultActivity";

	private ImageView mIvBack;
	private TextView mTvTitleText;
	private RelativeLayout mTitle;
	private FrameLayout mContent;
	private View mContentView;

	public static BaseActivity getActivity(Class clazz) {
		return activitys.get(clazz);
	}

	private static BaseActivity topActivity;
	private static LinkedHashMap<Class, BaseActivity> activitys = new LinkedHashMap<>();

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		TAG = getClass().getSimpleName();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_base);

		mIvBack = (ImageView) findViewById(R.id.iv_back);
		mTvTitleText = (TextView) findViewById(R.id.tv_title_text);
		mTitle = (RelativeLayout) findViewById(R.id.title);
		mContent = (FrameLayout) findViewById(R.id.content);
		mIvBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		hideActionBar();
		hideTitle();
		activitys.put(getClass(), this);
		mHelper = new SwipeBackActivityHelper(this);
		mHelper.onActivityCreate();
		setSwipeBackEnable(false);
	}
	public void setTitleText(CharSequence text){
		mTvTitleText.setText(text);
	}
	public CharSequence getTitleText(){
		return mTvTitleText.getText();
	}
	public void hideBack(){
		mIvBack.setVisibility(View.GONE);
	}
	public void showBack(){
		mIvBack.setVisibility(View.VISIBLE);
	}
	public void hideTitle(){
		mTitle.setVisibility(View.GONE);
	}
	public void showTitle(){
		mTitle.setVisibility(View.VISIBLE);
	}

	public View getContentView(){
		return mContentView;
	}

	@Override
	public void setContentView(@LayoutRes int layoutResID) {
		mContent.removeAllViews();
		mContentView = View.inflate(this, layoutResID, null);
		mContent.addView(mContentView);
	}

	@Override
	public void setContentView(View view) {
		mContentView = view;
		mContent.removeAllViews();
		mContent.addView(view);
	}

	@Override
	public void setContentView(View view, ViewGroup.LayoutParams params) {
		mContentView = view;
		mContent.removeAllViews();
		mContent.addView(view,params);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mHelper.onPostCreate();
	}

	@Override
	public View findViewById(int id) {
		View v = super.findViewById(id);
		if (v == null && mHelper != null)
			return mHelper.findViewById(id);
		return v;
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		topActivity = this;
		if (topActivity != null)
			Log.i("curentTopActivity: " + topActivity);
		else
			Log.i("curentTopActivity: null");
		StatService.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		topActivity = null;
		if (topActivity != null)
			Log.i("curentTopActivity: " + topActivity);
		else
			Log.i("curentTopActivity: null");
		StatService.onPause(this);
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

	public void hideActionBar() {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null)
			actionBar.hide();
	}
	public View getTitleView(){
		return mTitle;
	}

	public static BaseActivity getTopActivity() {
		return topActivity;
	}

	public static BaseActivity getLastActivity() {
		try {
			Map.Entry<Class, BaseActivity> entry = DataUtil.getTailByReflection(activitys);
			return entry.getValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public SwipeBackLayout getSwipeBackLayout() {
		return mHelper.getSwipeBackLayout();
	}

	@Override
	public void setSwipeBackEnable(boolean enable) {
		getSwipeBackLayout().setEnableGesture(enable);
	}

	@Override
	public void scrollToFinishActivity() {
		Utils.convertActivityToTranslucent(this);
		getSwipeBackLayout().scrollToFinishActivity();
	}
}
