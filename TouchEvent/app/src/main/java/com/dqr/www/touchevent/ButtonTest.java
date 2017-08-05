package com.dqr.www.touchevent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * Description：
 * Author：LiuYM
 * Date： 2017-08-05 10:33
 */

public class ButtonTest extends Button {
    public ButtonTest(Context context) {
        super(context);
    }

    public ButtonTest(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ButtonTest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
