package baseproject.view;

import android.content.Context;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.View;

import com.eshel.currencyspirit.widget.night.NightBottomBar;
import com.eshel.currencyspirit.widget.night.NightButton;
import com.eshel.currencyspirit.widget.night.NightEditText;
import com.eshel.currencyspirit.widget.night.NightFrameLayout;
import com.eshel.currencyspirit.widget.night.NightImageView;
import com.eshel.currencyspirit.widget.night.NightLinearLayout;
import com.eshel.currencyspirit.widget.night.NightRelativeLayout;
import com.eshel.currencyspirit.widget.night.NightSwitchButton;
import com.eshel.currencyspirit.widget.night.NightTextView;

/**
 * Created by guoshiwen on 2017/10/31.
 */

public class ViewFactory implements LayoutInflaterFactory {
	@Override
	public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
		View view = null;
		switch (name){
			case "TextView":
				view = new NightTextView(context, attrs);
				break;
			case "ImageView":
				view = new NightImageView(context, attrs);
				break;
			case "Button":
				view = new NightButton(context, attrs);
				break;
			case "EditText":
				view = new NightEditText(context, attrs);
				break;
			case "Spinner":
				view = new AppCompatSpinner(context, attrs);
				break;
			case "ImageButton":
				view = new AppCompatImageButton(context, attrs);
				break;
			case "CheckBox":
				view = new AppCompatCheckBox(context, attrs);
				break;
			case "RadioButton":
				view = new AppCompatRadioButton(context, attrs);
				break;
			case "CheckedTextView":
				view = new AppCompatCheckedTextView(context, attrs);
				break;
			case "AutoCompleteTextView":
				view = new AppCompatAutoCompleteTextView(context, attrs);
				break;
			case "MultiAutoCompleteTextView":
				view = new AppCompatMultiAutoCompleteTextView(context, attrs);
				break;
			case "RatingBar":
				view = new AppCompatRatingBar(context, attrs);
				break;
			case "SeekBar":
				view = new AppCompatSeekBar(context, attrs);
				break;
			case "LinearLayout":
				view = new NightLinearLayout(context, attrs);
				break;
			case "RelativeLayout":
				view = new NightRelativeLayout(context, attrs);
				break;
			case "FrameLayout":
				view = new NightFrameLayout(context, attrs);
				break;
			case "com.kyleduo.switchbutton.SwitchButton":
				view = new NightSwitchButton(context, attrs);
				break;
			case "com.roughike.bottombar.BottomBar":
				view = new NightBottomBar(context, attrs);
				break;
		}
		return view;
	}
}
