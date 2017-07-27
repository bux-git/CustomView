package com.dqr.www.coordinate;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int lastX;
    int lastY;

    private View mMoveView;
    private TextView mTvCoordinate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMoveView=findViewById(R.id.move_view);
        mTvCoordinate = (TextView) findViewById(R.id.tv_coordinate);

        mMoveView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getRawX();
                int y = (int) event.getRawY();
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN: {

                        break;
                    }

                    case MotionEvent.ACTION_MOVE: {
                        int left = x - lastX;
                        int top = y - lastY;
                        mMoveView.layout(mMoveView.getLeft()+left,mMoveView.getTop()+top,mMoveView.getRight()+left,mMoveView.getBottom()+top);
                        mMoveView.postInvalidate();
                        break;
                    }

                    case MotionEvent.ACTION_UP: {

                        break;
                    }

                }
                lastX = x;
                lastY = y;

                //获取View自身可见坐标
                Rect localVisible = new Rect();
                mMoveView.getLocalVisibleRect(localVisible);
                //获取View相对屏幕可见区域坐标
                Rect globalVisible = new Rect();
                mMoveView.getGlobalVisibleRect(globalVisible);
                //获取View左上角相对于屏幕坐标
                int[] screen = new int[2];
                mMoveView.getLocationOnScreen(screen);

                //View左上角相对屏幕或者Dialog模式Activity 相对与标题栏左上角坐标
                int[] locationInWindow = new int[2];
                mMoveView.getLocationInWindow(locationInWindow);

                String coordinateStr=
                        "\n---------View坐标--------"
                        +"\ngetLeft():"+mMoveView.getLeft()
                        +"\ngetTop():"+mMoveView.getTop()
                        +"\ngetRight():"+mMoveView.getRight()
                        +"\ngetBottom():"+mMoveView.getBottom()
                        +"\ngetX():"+mMoveView.getX()
                        +"\ngetY():"+mMoveView.getY()
                        +"\n-------触摸事件坐标--------"
                        +"\nMotionEvent.getX():"+event.getX()
                        +"\nMotionEvent.getY():"+event.getY()
                        +"\nMotionEvent.getRawX():"+event.getRawX()
                        +"\nMotionEvent.getRawY():"+event.getRawY()
                        +"\n--------获取View自身可见坐标--------"
                        +"\ngetLocalVisibleRect.left:"+localVisible.left
                        +"\ngetLocalVisibleRect.top:"+localVisible.top
                        +"\ngetLocalVisibleRect.right:"+localVisible.right
                        +"\ngetLocalVisibleRect.bottom:"+localVisible.bottom
                        +"\n---------获取View相对屏幕可见区域坐标--------"
                        +"\ngetGlobalVisibleRect.left:"+globalVisible.left
                        +"\ngetGlobalVisibleRect.top:"+globalVisible.top
                        +"\ngetGlobalVisibleRect.right:"+globalVisible.right
                        +"\ngetGlobalVisibleRect.bottom:"+globalVisible.bottom
                        +"\n----------获取View左上角相对于屏幕坐标--------"
                        +"\ngetLocationOnScreen.x:"+screen[0]
                        +"\ngetLocationOnScreen.y:"+screen[1]
                        +"\n----------View左上角相对屏幕或者Dialog模式Activity 相对与标题栏左上角坐标-------"
                        +"\ngetLocationInWindow.x:"+locationInWindow[0]
                        +"\ngetLocationInWindow.y:"+locationInWindow[1];


                mTvCoordinate.setText(coordinateStr);
                return true;
            }
        });

    }
}
