package com.xzw.szl;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by xzwszl on 8/11/2015.
 */
public class IndicatorView extends View {

    private static final int INDICATOR_OVAL = 0;
    private static final int INDICATOR_RECTANGLE = 1;

    private static final int DEFAULT_SPACE = 10;
    private static final int DEFAULT_SCALE = 10;


    private static final int DEFAULT_BACK_COLOR = 0x8F0893AD;
    private static final int DEFAULT_FRONT_COLOR = 0xAF0893AD;

    private static final int LEFT = 0;
    private static final int RIGHT = 2;
    private static final int CENTER = 1;


    private float mPaddingTop;
    private float mPaddingLeft;
    private float mPaddingRight;
    private float mPaddingBottom;
    private float mSpace; //间隔
    private float mScale; //圆 直径,正方形 边长


    private int mBackInidcaotrColor;
    private int mFrontIndicatorColor;

    private Paint mPaint;

    private int mType = INDICATOR_OVAL;

    private int mGravity = CENTER;

    private int mCount = 0;

    public IndicatorView(Context context) {
        super(context);
        init();
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IndicatorView, defStyleAttr, 0);

        mSpace = a.getDimension(R.styleable.IndicatorView_space, DEFAULT_SPACE);
        mScale = a.getDimension(R.styleable.IndicatorView_scale, DEFAULT_SCALE);
        mBackInidcaotrColor = a.getColor(R.styleable.IndicatorView_back_color, DEFAULT_BACK_COLOR);
        mFrontIndicatorColor = a.getColor(R.styleable.IndicatorView_front_color, DEFAULT_FRONT_COLOR);

        mType = a.getInt(R.styleable.IndicatorView_type, INDICATOR_OVAL);

        mGravity = a.getInt(R.styleable.IndicatorView_gravity, CENTER);

        mCount = a.getInt(R.styleable.IndicatorView_count, 0);

        a.recycle();

        mPaddingTop = getPaddingTop();
        mPaddingLeft = getPaddingLeft();
        mPaddingRight = getPaddingRight();
        mPaddingBottom = getPaddingBottom();

        init();

    }

    private void init() {
        mPaint = new Paint();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode ==  MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {

            width = (int) (mPaddingLeft + mPaddingRight);

            if (mCount > 0) {
                width += mCount * mScale + (mCount-1) * mSpace;
            }

            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.max(width, getSuggestedMinimumWidth());
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {

            height = (int) (mPaddingBottom +mPaddingTop);

            if(mCount > 0) {
                height += mScale;
            }
        }

        setMeasuredDimension(width, height);
    }
}
