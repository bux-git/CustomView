package com.dqr.www.customattrs;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Description：
 * Author：LiuYM
 * Date： 2017-07-31 10:26
 */

public class CustomView extends TextView {
    private static final String TAG = "CustomView";

    public CustomView(Context context) {
        this(context,null);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,R.attr.def_style_theme);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Customize, defStyleAttr,R.style.def_style_res);
       /* for(int i=0;i<array.length();i++){
            //根据下标获取testable中索引值
            int attr = array.getIndex(i);
            //确定是哪一个自定义属性，然后获取对应的类型数据值
            if(attr==R.styleable.testable_testAttr){
              setText(array.getString(attr));
            }
        }*/
       //根据styleable中的属性下标获取对应的属性值
        setText("one:"+array.getString(R.styleable.Customize_attr_one)
                 +"\ntwo:"+array.getString(R.styleable.Customize_attr_two)
                +"\nthree:"+array.getString(R.styleable.Customize_attr_three)
                +"\nfour:"+array.getString(R.styleable.Customize_attr_four)
                +"\nfive:"+array.getString(R.styleable.Customize_attr_five));

        array.recycle();
    }

}
