package com.yikuanzz.fragementbestpratice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    private boolean isTwoPlane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("activityCreate", "111");
        if (findViewById(R.id.news_content_layout) != null){
            setTwoPlane(true);
        }
    }

    public boolean getTwoPlane(){
        return isTwoPlane;
    }

    public void setTwoPlane(boolean flag){
        isTwoPlane = flag;
    }
}