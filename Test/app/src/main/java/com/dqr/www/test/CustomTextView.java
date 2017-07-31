package com.dqr.www.test;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;

public class CustomTextView extends android.support.v7.widget.AppCompatTextView {
    
    private static final String TAG = CustomTextView.class.getSimpleName();

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.CustomizeStyle);
    }
    
    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Customize, defStyle,
                R.style.DefaultCustomizeStyle);
        String one = a.getString(R.styleable.Customize_attr_one);
        String two = a.getString(R.styleable.Customize_attr_two);
        String three = a.getString(R.styleable.Customize_attr_three);
        String four = a.getString(R.styleable.Customize_attr_four);
        Log.i(TAG, "one:" + one);
        Log.i(TAG, "two:" + two);
        Log.i(TAG, "three:" + three);
        Log.i(TAG, "four:" + four);
        a.recycle();
    }
}
