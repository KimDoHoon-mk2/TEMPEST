package com.example.kim.tempest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StartPageActivity extends AppCompatActivity {

    private int[] TvID = {R.id.startpage_textview0,R.id.startpage_textview1,R.id.startpage_textview2,R.id.startpage_textview3,R.id.startpage_textview4,R.id.startpage_textview5,R.id.startpage_textview6};
    private TextView[] textViews = new TextView[TvID.length];
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startpage);

        init();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                overridePendingTransition(0,0);
            }
        },4000);
    }

    void init(){
        for(int i=0;i<TvID.length;i++){
            textViews[i] = (TextView) findViewById(TvID[i]);
        }
        linearLayout = (LinearLayout) findViewById(R.id.startpage_layout1);

        textViews[6].getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                anime();
                textViews[6].getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

    void anime(){
        linearLayout.setScaleX(0);
        linearLayout.animate().scaleX(1).setDuration(2100).setStartDelay(1000);

        for(int i=0;i<TvID.length;i++){
            textViews[i].setTranslationY(getResources().getDimensionPixelSize(R.dimen.dp60));
            textViews[i].animate().translationY(0).setDuration(800).setStartDelay(1100+(300*i)).setInterpolator(AnimationUtils.loadInterpolator(this,android.R.anim.overshoot_interpolator));
        }
    }
}

