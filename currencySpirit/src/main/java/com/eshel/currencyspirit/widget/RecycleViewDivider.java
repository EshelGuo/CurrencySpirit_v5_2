package com.eshel.currencyspirit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.eshel.currencyspirit.util.UIUtil;
import com.eshel.currencyspirit.widget.night.INight;
import com.eshel.currencyspirit.widget.night.NightView;
import com.eshel.currencyspirit.widget.night.NightViewUtil;

public class RecycleViewDivider extends RecyclerView.ItemDecoration implements INight{

    private Paint mPaint;
    private Drawable mDivider;
    private int mDividerHeight = 2;//分割线高度，默认为1px
    private int mOrientation;//列表的方向：LinearLayoutManager.VERTICAL或LinearLayoutManager.HORIZONTAL
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    public int bgColor = NightViewUtil.changeNightColor(UIUtil.getColor(android.R.color.white));
    public int dividerColor = UIUtil.getColor(android.R.color.darker_gray);
    /**
     * 默认分割线：高度为2px，颜色为灰色
     *
     * @param context
     * @param orientation 列表方向
     */
    public RecycleViewDivider(Context context, int orientation) {
        if (orientation != LinearLayoutManager.VERTICAL && orientation != LinearLayoutManager.HORIZONTAL) {
            throw new IllegalArgumentException("请输入正确的参数！");
        }
        mOrientation = orientation;

        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param orientation 列表方向
     * @param drawableId  分割线图片
     */
    public RecycleViewDivider(Context context, int orientation, int drawableId) {
        this(context, orientation);
        mDivider = ContextCompat.getDrawable(context, drawableId);
        mDividerHeight = mDivider.getIntrinsicHeight();
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param orientation   列表方向
     * @param dividerHeight 分割线高度
     * @param dividerColor  分割线颜色
     */
    public RecycleViewDivider(Context context, int orientation, int dividerHeight, int dividerColor) {
        this(context, orientation);
        this.dividerColor = dividerColor;
        mDividerHeight = dividerHeight;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(dividerColor);
        mPaint.setStyle(Paint.Style.FILL);
    }
    public RecycleViewDivider(Context context, int orientation, int dividerHeight, int dividerColor,int left, int right) {
        this(context, orientation);
        dividerColor = NightViewUtil.changeNightColor(dividerColor);
        this.dividerColor = dividerColor;
        mDividerHeight = dividerHeight;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(dividerColor);
        mPaint.setStyle(Paint.Style.FILL);
        this.paddingright = right;
        paddingleft = left;
    }


    //获取分割线尺寸
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0, 0, 0, mDividerHeight);
    }

    //绘制分割线
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }
    int paddingleft;
    int paddingright;
    public void setPaddingLR(int left, int right){
        this.paddingleft = left;
        this.paddingright = right;
    }

    //绘制横向 item 分割线
    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        int left = parent.getPaddingLeft();
        if(paddingleft != 0)
            left+=paddingleft;
        int right = parent.getMeasuredWidth() - parent.getPaddingRight();
        if(paddingright != 0)
            right -= paddingright;
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize - 1; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + layoutParams.bottomMargin;
            final int bottom = top + mDividerHeight;
            if (mDivider != null) {
                mDivider.setBounds(parent.getPaddingLeft(), top, parent.getMeasuredWidth() - parent.getPaddingRight(), bottom);
                mDivider.draw(canvas);
            }
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
                mPaint.setColor(bgColor);
                canvas.drawRect(parent.getPaddingLeft(), top, left, bottom, mPaint);
                canvas.drawRect(right, top, parent.getMeasuredWidth() - parent.getPaddingRight(), bottom, mPaint);
                mPaint.setColor(dividerColor);
            }
        }
    }

    //绘制纵向 item 分割线
    private void drawVertical(Canvas canvas, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + layoutParams.rightMargin;
            final int right = left + mDividerHeight;
            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

    @Override
    public void changeNightMode(boolean isNight) {
        int originalColor = mPaint.getColor();
        int changedColor = NightViewUtil.changeNightColor(originalColor);
        int changeBackgroundColor = NightViewUtil.changeNightColor(bgColor);
        bgColor = changeBackgroundColor;
        if(changedColor != originalColor){
            mPaint.setColor(changedColor);
            dividerColor = changedColor;
        }
    }
}