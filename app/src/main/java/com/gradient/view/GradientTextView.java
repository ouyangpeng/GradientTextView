package com.gradient.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.gradient.R;


public class GradientTextView extends AppCompatTextView {
    /**
     * 原来颜色的画笔
     */
    private Paint mOriginalPaint;
    /**
     * 变色后的画笔
     */
    private Paint mChangePaint;
    /**
     * 文本原来的颜色，初始值设置为黑色
     */
    private int mOriginalColor = Color.BLACK;
    /**
     * 文本改变后的颜色，初始值设置为红色
     */
    private int mChangeColor = Color.RED;

    /**
     * 要改变颜色的方向
     */
    private Orientation orientation = Orientation.LEFT_TO_RIGHT;
    /**
     * 基线
     */
    private int baseLine;
    /**
     * 进度值
     */
    private float mCurrentProgress;

    public GradientTextView(Context context) {
        this(context, null);
    }

    public GradientTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradientTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //读取自定义属性的值
        TypedArray td = context.obtainStyledAttributes(attrs, R.styleable.GradientTextView);
        mOriginalColor = td.getColor(R.styleable.GradientTextView_original_color, mOriginalColor);
        mChangeColor = td.getColor(R.styleable.GradientTextView_change_color, mChangeColor);
        td.recycle();

        //根据颜色获取画笔
        mOriginalPaint = getPaintByColor(mOriginalColor);
        mChangePaint = getPaintByColor(mChangeColor);
    }

    /**
     * 根据颜色值来获取画笔
     * @param color 颜色值
     * @return 画笔
     */
    private Paint getPaintByColor(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setDither(true);//防抖动
        paint.setTextSize(getTextSize());
        return paint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float middle = mCurrentProgress * getWidth();
        Paint.FontMetricsInt fontMetricsInt = mOriginalPaint.getFontMetricsInt();
        baseLine = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom + getHeight() / 2
                + getPaddingTop() / 2 - getPaddingBottom() / 2;
        //根据变色的方向值来重新绘制View
        switch (orientation){
            case LEFT_TO_RIGHT:
                clipRect(canvas, 0, middle, mChangePaint);
                clipRect(canvas, middle, getWidth(), mOriginalPaint);
                break;
            case INNER_TO_OUTER:
                clipRect(canvas, getWidth() - middle, middle, mChangePaint);
                clipRect(canvas, middle, getWidth() - middle, mOriginalPaint);
                break;
            case RIGHT_TO_LEFT:
                clipRect(canvas, getWidth() - middle, getWidth(), mChangePaint);
                clipRect(canvas, 0, getWidth() - middle, mOriginalPaint);
                break;
            case RIGHT_TO_LEFT_FROM_NONE:
                clipRect(canvas, getWidth() - middle, getWidth(), mChangePaint);
                clipRect(canvas, getWidth(), getWidth() - middle, mOriginalPaint);
                break;
            case LEFT_TO_RIGHT_FORM_NONE:
                clipRect(canvas, 0, middle, mChangePaint);
                clipRect(canvas, middle, 0, mOriginalPaint);
                break;
            default:
                break;
        }
    }

    /**
     * 截取指定的裁剪区域，并进行重新绘制文本操作
     * @param canvas 画布
     * @param start 矩形裁剪区的左边位置
     * @param region 矩形裁剪区的右边位置
     * @param paint 画笔
     */
    private void clipRect(Canvas canvas, float start, float region, Paint paint) {
        //改变的颜色
        canvas.save();
        canvas.clipRect(start + getPaddingLeft(), 0, region, getHeight());
        canvas.drawText(getText().toString(), getPaddingLeft(), baseLine, paint);
        canvas.restore();
    }

    /**
     * 设置文本颜色改变的方向
     * @param orientation 文本颜色改变的方向
     */
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    /**
     * 设置当前的进度值
     * @param currentProgress 进度值
     */
    public void setCurrentProgress(float currentProgress) {
        mCurrentProgress = currentProgress;
        invalidate();
    }

    /**
     *  开始文件变色的动画并在指定时长内完成
     * @param orientation  文本颜色改变的方向
     * @param duration 时长
     */
    public void start(final Orientation orientation, long duration) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                setOrientation(orientation);
                setCurrentProgress(value);
            }
        });
        animator.start();
    }
}
