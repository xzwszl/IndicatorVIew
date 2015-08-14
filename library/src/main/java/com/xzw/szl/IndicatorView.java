package com.xzw.szl;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by xzwszl on 8/11/2015.
 */
public class IndicatorView extends View {

    public static final int INDICATOR_OVAL = 0;
    public static final int INDICATOR_RECTANGLE = 1;

    private static final int DEFAULT_SPACE = 10;
    private static final int DEFAULT_SCALE = 10;


    private static final int DEFAULT_BACK_COLOR = 0x8F666666;
    private static final int DEFAULT_FRONT_COLOR = 0xFFFFFFFF;

    public static final int LEFT = 0;
    public static final int RIGHT = 2;
    public static final int CENTER = 1;

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

    private ViewPager mViewPager;

    private ViewPager.OnPageChangeListener pageChangeListener;

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

        init();

    }
    public void setGravity(int gravity) {

        mGravity = gravity;
        invalidate();
    }

    public void setType(int type) {
        mType = type;
        invalidate();
    }

    /**
     * Set the position of frontindicator, the position is belong [0,Count]
     * for example, 0 means the first place to locate this indicator.
     * @param position
     */
    public void setPosition(int position) {
        setLocationWithRealOffset(position, 0);
    }

    /**
     * @param position
     * @param coefficient
     */
    public void setLocationWithCoefficient(int position, float coefficient) {

        float offset = (mScale + mSpace) * coefficient;
        setLocationWithRealOffset(position, offset);
    }

    public void setLocationWithRealOffset(int position, float offset) {
        mPosition = position;
        mOffset = offset;
        invalidate();
    }

    public void setCount(int count) {
        mCount = count;
        invalidate();
    }


    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPosition = 0;
        mOffset = 0;
    }

    public void setViewPager(ViewPager viewPager) {
        mViewPager = viewPager;

        if (mViewPager != null) {

            if (mViewPager.getAdapter() == null) {
                throw  new NullPointerException("The adapter of viewpager must not be nul1");
            }

            mCount = mViewPager.getAdapter().getCount();
            mViewPager.addOnPageChangeListener(new PageChangeListener());
        }
    }

    public void addOnPageChangeListener(ViewPager.OnPageChangeListener pageChangeListener) {
        this.pageChangeListener = pageChangeListener;
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

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode ==  MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {

            width = getPaddingLeft() + getPaddingRight();

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

            height = getPaddingBottom() + getPaddingTop();

            if(mCount > 0) {
                height = height + (int)mScale;
            }

            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.max(height, getSuggestedMinimumHeight());
            }
        }

        setMeasuredDimension(width, height);
    }

    class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            setLocationWithCoefficient(position, positionOffset);

            if (pageChangeListener != null) {
                pageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {

            if (pageChangeListener != null) {
                pageChangeListener.onPageSelected(position);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

            if (pageChangeListener != null) {
                pageChangeListener.onPageSelected(state);
            }
        }
    }
}
