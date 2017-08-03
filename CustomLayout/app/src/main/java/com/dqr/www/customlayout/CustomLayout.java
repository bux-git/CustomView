package com.dqr.www.customlayout;

import android.content.Context;
import android.support.annotation.Px;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Description：
 * Author：LiuYM
 * Date： 2017-08-03 18:36
 */

public class CustomLayout extends ViewGroup {

    private static final String TAG = "CustomLayout";


    public CustomLayout(Context context) {
        super(context);
    }

    public CustomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentDesireWidth=0;//累积宽
        int parentDesireHeight=0;//累积高
        //直接计算出子View宽高
        int childCount=getChildCount();
        for(int i=0;i<childCount;i++){
            View child = getChildAt(i);

            CustomLayoutParams params= (CustomLayoutParams) child.getLayoutParams();
            //测量子View的宽 处理自己的padding和 子View的 margins
            measureChildWithMargins(child,widthMeasureSpec,0,heightMeasureSpec,parentDesireHeight);
            //记录宽
            int childWidth=child.getMeasuredWidth()+params.leftMargin+params.rightMargin;
            //所有子View中最大值作为ViewGroup的宽
            parentDesireWidth = Math.max(parentDesireWidth, childWidth);

            //高为所有子View的高的累积
            parentDesireHeight +=child.getMeasuredHeight()+params.topMargin+params.bottomMargin;
            Log.d(TAG, "onMeasure: parentDesireHeight:"+parentDesireHeight);
        }
        //处理自己的内边距
        parentDesireWidth +=getPaddingLeft()+getPaddingRight();
        parentDesireHeight +=getPaddingTop()+getPaddingBottom();
        Log.d(TAG, "onMeasure: "+parentDesireHeight+" heightMeasureSize:"+MeasureSpec.getSize(heightMeasureSpec));
        //处理比较最小值和期望值的大小 取最大值
        parentDesireWidth = Math.max(parentDesireWidth, getSuggestedMinimumWidth());
        parentDesireHeight = Math.max(parentDesireHeight, getSuggestedMinimumHeight());

        //resolveSize:根据模式决定使用计算出的宽高 还是父View期望的宽高
        setMeasuredDimension(resolveSize(parentDesireWidth,widthMeasureSpec)
        ,resolveSize(parentDesireHeight,heightMeasureSpec));

    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int leftPadding=getPaddingLeft();
        int topPadding=getPaddingTop();

        //存储累积高度
        int mulHeight=0;
        int childCount=getChildCount();

        for(int i=0;i<childCount;i++){
            View child = getChildAt(i);
            CustomLayoutParams params= (CustomLayoutParams) child.getLayoutParams();

            int left=params.leftMargin+ leftPadding;
            int top=params.topMargin+topPadding+mulHeight;
            int right=left+child.getMeasuredWidth();
            int bottom=top+child.getMeasuredHeight();

            child.layout(left,top,right,bottom);

            mulHeight +=params.topMargin+child.getMeasuredHeight()+params.bottomMargin;
        }
    }


    /**
     * 定义自己的LayoutParams
     */
    public static class CustomLayoutParams extends MarginLayoutParams{

        public CustomLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public CustomLayoutParams(@Px int width, @Px int height) {
            super(width, height);
        }

        public CustomLayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public CustomLayoutParams(LayoutParams source) {
            super(source);
        }

    }

    /**
     * 生成默认布局参数
     * @return
     */
    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new CustomLayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
    }

    /**
     * 生成布局参数
     * 将布局参数包装成我们自己定义的
     * @param p
     * @return
     */
    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new CustomLayoutParams(p);
    }

    /**
     * 生成布局参数 从属性配置中生成我们的布局参数
     * @param attrs
     * @return
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new CustomLayoutParams(getContext(),attrs);
    }

    /**
     * 检测布局参数是否是我们定义的类型
     * 在代码中申明布局参数时经常用到
     * @param p
     * @return
     */
    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof CustomLayoutParams;
    }
}
