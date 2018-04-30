package com.sj.loadingview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.sj.library.BallLoadingView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private BallLoadingView loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.show).setOnClickListener(this);
        findViewById(R.id.hide).setOnClickListener(this);
        loadingView = findViewById(R.id.loadingView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show:
                loadingView.setVisibility(View.VISIBLE);
                break;
            case R.id.hide:
                loadingView.setVisibility(View.GONE);
                break;
        }
    }
}
