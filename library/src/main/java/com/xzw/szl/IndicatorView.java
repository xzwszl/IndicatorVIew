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
 * Created by xzwszl on 8/11 * 0.5f015.
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

    private float startX;
    private float startY;

    private int mPosition;

    private float mLocation;

    private float mOffset;

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

    /**
     * Set the position of frontindicator, the position is belong [0,Count]
     * for example, 0 means the first place to locate this indicator.
     * @param position
     */
    public void setPosition(int position) {
        setLocationWithRealOffest(position,0);
    }

    /**
     * @param position
     * @param coefficient
     */
    public void setLocationWithCoefficient(int position, float coefficient) {

        float offest = (mScale + mSpace) * coefficient;
        setLocationWithRealOffest(position,offest);
    }

    public void setLocationWithRealOffest(int position, float offest) {
        mPosition = position;
        mOffset = offest;
        invalidate();
    }


    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPosition = 0;
        mOffset = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawIndicator(canvas);
    }

    private void drawIndicator(Canvas canvas) {

        switch (mGravity) {
            case LEFT:
                startX = getPaddingLeft();
                break;
            case RIGHT:
                startX = getWidth() - getPaddingRight();

                if (mCount > 0) {
                    startX -= mScale * mCount + mSpace * (mCount -1);
                }
                break;
            case CENTER:
            default:
                float mid = (getWidth() - getPaddingRight() + getPaddingLeft()) * 0.5f;
                float length = mScale * mCount;

                if (mCount >0) {
                    length += mSpace * (mCount -1);
                }

                startX = mid - length * 0.5f;
                break;
        }

        startY = (getHeight() + getPaddingTop() - getPaddingBottom() - mScale) * 0.5f;

        mPaint.setColor(mBackInidcaotrColor);

        float cur = startX;

        for (int i = 0;i < mCount; i++) {

            if (mType == INDICATOR_OVAL) {
                canvas.drawCircle(cur+mScale * 0.5f, startY + mScale * 0.5f,mScale * 0.5f,mPaint);
            } else if (mType == INDICATOR_RECTANGLE){
                canvas.drawRect(cur,startY,cur+mScale,startY +mScale, mPaint);
            }

            if (mPosition == i) {
                mLocation = cur + mOffset;
            }
            cur+= mScale+mSpace;
        }

        mPaint.setColor(mFrontIndicatorColor);

        if (mType == INDICATOR_OVAL) {
            canvas.drawCircle(mLocation+mScale * 0.5f, startY+mScale * 0.5f,mScale * 0.5f,mPaint);
        } else if (mType == INDICATOR_RECTANGLE){
            canvas.drawRect(mLocation,startY,mLocation + mScale,startY +mScale, mPaint);
        }
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
                height = height + (int)mScale;
            }

            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.max(height, getSuggestedMinimumHeight());
            }
        }

        setMeasuredDimension(width, height);
    }
}
