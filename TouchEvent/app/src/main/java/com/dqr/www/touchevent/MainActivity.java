package com.dqr.www.touchevent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    LinearLayout lltOne;
    Button btnOne;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lltOne=(LinearLayout) findViewById(R.id.llt1);
        btnOne=(Button) findViewById(R.id.btn1);

        lltOne.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "onTouch: "+v.getClass().getSimpleName()+"  event:"+event);
                return false;
            }
        });

        btnOne.setEnabled(false);
    }
}
