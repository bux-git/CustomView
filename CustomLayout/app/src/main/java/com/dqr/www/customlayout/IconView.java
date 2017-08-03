package com.dqr.www.customlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Description：
 * Author：LiuYM
 * Date： 2017-08-03 16:37
 */

public class IconView extends View {
    private String mTitle;
    private int mImgResId;
    private Bitmap mImg;

    private TextPaint mPaint;//绘制文本的画笔
    private float mTextSize;//画笔文本尺寸

    private enum Ratio{
        WIDTH,HEIGHT
    }

    public IconView(Context context) {
        super(context);
    }

    public IconView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IconView, 0, 0);
        mTitle = typedArray.getString(R.styleable.IconView_title);
        mImgResId = typedArray.getResourceId(R.styleable.IconView_img, -1);
        typedArray.recycle();
        if(mImgResId>0){
            mImg = BitmapFactory.decodeResource(context.getResources(), mImgResId);
        }
        /**
         * 初始化画笔并设置
         */
        mPaint=new TextPaint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG|Paint.LINEAR_TEXT_FLAG);
        mPaint.setColor(Color.LTGRAY);
        mPaint.setTextSize(20);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getMeasureSize(widthMeasureSpec,Ratio.WIDTH),getMeasureSize(heightMeasureSpec,Ratio.HEIGHT));
    }

    private int getMeasureSize(int measureSpec,Ratio ratio){
        int result=0;
        int size = MeasureSpec.getSize(measureSpec);
        int mode = MeasureSpec.getMode(measureSpec);

        //EXACTLY直接赋值
        if(mode==MeasureSpec.EXACTLY){
            result=size;
        }else{//AT_MOST和UNSPECIFIED一起处理
            if(ratio==Ratio.HEIGHT){
                result = ((int) ((mPaint.descent() - mPaint.ascent()) * 2 + mImg.getHeight())) + getPaddingTop() + getPaddingBottom();
            }else{
                float textWidth = mPaint.measureText(mTitle);
                result = ((int) (textWidth >= mImg.getWidth() ? textWidth : mImg.getWidth())) + getPaddingLeft() + getPaddingRight();

                /**
                 * AT_MOST时 判断内容大小和父View测量出的大小 取最小值
                 */
                if(mode==MeasureSpec.AT_MOST){
                    result = Math.min(result, size);
                }
            }
        }

        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mImg, getWidth() / 2 - mImg.getWidth() / 2, getHeight() / 2 - mImg.getHeight() / 2, null);
        canvas.drawText(mTitle, getWidth() / 2, mImg.getHeight() + getHeight() / 2 - mImg.getHeight() / 2 - mPaint.ascent(), mPaint);

    }
}
