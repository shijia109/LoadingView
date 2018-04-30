package com.sj.library;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by heiyan on 2018/2/7.
 */

public class BallLoadingView extends View {
    private int color_1;
    private int color_2;
    private static final int DEFAULT_BALL_RADIUS = 12;
    private static final int DEFAULT_ROTATE_RADIUS = 25;
    private static final int DEFAULT_MAX_SCALE_LENGTH = 3;
    private static final int DEFAULT_COLOR_1 = Color.YELLOW;
    private static final int DEFAULT_COLOR_2 = Color.RED;
    private static final int DEFAULT_DURATION = 2000;
    private static final int DEFAULT_START_ANGLE = 0;

    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 100;

    private int startAngle;
    private float degree;
    private float centerX;
    private float centerY;
    private float maxScaleLength;
    private float ballRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_BALL_RADIUS,
            getContext().getResources().getDisplayMetrics());
    private float rotateRadius;
    private long duration = 1500;
    private Paint paint;
    //    private boolean isAnimationStarted = false;
    private ValueAnimator animator;


    public BallLoadingView(Context context) {
        this(context, null);
    }

    public BallLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BallLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BallLoadingView);
        startAngle = array.getInt(R.styleable.BallLoadingView_startAngle, DEFAULT_START_ANGLE);
        color_1 = array.getColor(R.styleable.BallLoadingView_color1, DEFAULT_COLOR_1);
        color_2 = array.getColor(R.styleable.BallLoadingView_color2, DEFAULT_COLOR_2);
        ballRadius = array.getDimensionPixelSize(R.styleable.BallLoadingView_ballRadius,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_BALL_RADIUS,
                        context.getResources().getDisplayMetrics()));
        rotateRadius = array.getDimensionPixelSize(R.styleable.BallLoadingView_rotateRadius,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_ROTATE_RADIUS,
                        context.getResources().getDisplayMetrics()));
        maxScaleLength = array.getDimensionPixelSize(R.styleable.BallLoadingView_maxScaleLength,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_MAX_SCALE_LENGTH,
                        context.getResources().getDisplayMetrics()));
        duration = array.getInt(R.styleable.BallLoadingView_duration, DEFAULT_DURATION);

        array.recycle();

        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        widthSize = getMeasureSize(widthMode, widthSize, DEFAULT_WIDTH);
        heightSize = getMeasureSize(heightMode, heightSize, DEFAULT_HEIGHT);
        if (heightMode == MeasureSpec.EXACTLY){
            heightSize = Math.max(heightSize, getSuggestedMinimumHeight());
        }
        if (widthMode == MeasureSpec.EXACTLY){
            widthSize = Math.max(widthSize, getSuggestedMinimumWidth());
        }

        centerX = (widthSize + getPaddingLeft()) / 2;
        centerY = (heightSize + getPaddingTop()) / 2;
        setMeasuredDimension(widthSize, heightSize);
    }

    private int getMeasureSize(int mode, int size, int defaultSize) {
        switch (mode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, defaultSize,
                        getContext().getResources().getDisplayMetrics());
                break;
            case MeasureSpec.EXACTLY:
                break;
        }
        return size;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float length = (float) (Math.sin(degree * Math.PI / 180) * rotateRadius);
        float scale = (float) Math.cos(degree * Math.PI / 180);

        float scaleLength = scale * maxScaleLength;

        if (degree > 90 && degree < 270) {
            canvas.save();
            paint.setColor(color_1);
            canvas.drawCircle(centerX + length, centerY, ballRadius + scaleLength, paint);
            canvas.restore();

            canvas.save();
            paint.setColor(color_2);
            canvas.drawCircle(centerX - length, centerY, ballRadius - scaleLength, paint);
            canvas.restore();
        } else {
            canvas.save();
            paint.setColor(color_2);
            canvas.drawCircle(centerX - length, centerY, ballRadius - scaleLength, paint);
            canvas.restore();

            canvas.save();
            paint.setColor(color_1);
            canvas.drawCircle(centerX + length, centerY, ballRadius + scaleLength, paint);
            canvas.restore();
        }
    }

    private ValueAnimator getAnimator() {
        animator = ValueAnimator.ofFloat(0, 360);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                degree = (float) animation.getAnimatedValue() + startAngle;
                invalidate();
            }
        });
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);
        animator.setDuration(duration);
        return animator;
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == GONE || visibility == INVISIBLE) {
            stopAnimation();
        } else {
            startAnimation();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();
    }

    private void startAnimation() {
        if (getVisibility() != VISIBLE || (animator != null && animator.isStarted()))
            return;
        getAnimator().start();
    }

    private void stopAnimation() {
        if (animator == null || !animator.isStarted())
            return;
        animator.removeAllUpdateListeners();
        animator.end();
        animator.cancel();
    }
}
