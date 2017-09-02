package com.example.uberv.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import timber.log.Timber;

public class BullsEyeView extends View {

    private Paint mPaint;

    private Point mCenter;
    private float mRadius;

    // Java constructor
    public BullsEyeView(Context context) {
        super(context);
        init();
    }

    // XML constructor
    public BullsEyeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    // XML constructor with styles
    public BullsEyeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    // XML constructor with styles 2
    public BullsEyeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        // Create a paintbrush to draw with
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // We want to draw our circles filled in
        mPaint.setStyle(Paint.Style.FILL);
        mCenter = new Point();
    }

    // Before a view hierarchy is displayed, Android calls onMeasure() for each element
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Timber.d("onMeasure: " + widthMeasureSpec + ", " + heightMeasureSpec);
        /*
        * widthMeasureSpec and heightMeasureSpec are packed integers that include a mode flag
        * and a size value. Mode will be one of the following values:
        * AT_MOST - typically the layout parameters of the view are match_parent or any other limit on the size
        * the view should report any size it wants, as long as it doesn't exceed the value in the spec
        * EXACTLY - typically the layout parameters of the view are fixed value. Framework expects the same values (not less or bigger)
        * UNSPECIFIED - often used to figure out how big the view wants to be if unconstrainted.
        * This may be a precursor to another measurement with different constraints or simple because
        * layout parameters were set to wrap_content. Report whatever you want.
        *
        * One calculations on what size to report, those values MUST be passed in a call to setMeasuredImension()
        * before onMeasure() returns
        */

        int width, height;
        // Determine the ideal size of your content, unconstrained
        int contentWidth = 200;
        int contentHeight = 200;

        width = getMeasurement(widthMeasureSpec, contentWidth);
        height = getMeasurement(heightMeasureSpec, contentHeight);
        Timber.d("Measured width and height: " + width + "x" + height);
        // MUST call this method with measured values before onMeasure() is finished!
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Timber.d("onSize changed from " + w + "x" + h + " to " + oldw + "x" + oldh);
        if (w != oldw || h != oldh) {
            //If there was a change, reset the parameters
            mCenter.x = w / 2;
            mCenter.y = h / 2;
            mRadius = Math.min(mCenter.x, mCenter.y);
        }
    }

    /**
     * Helper method to measure width and height
     */
    private int getMeasurement(int measureSpec, int contentSize) {
        int specSize = MeasureSpec.getSize(measureSpec);
        Timber.d("MeasureSpec's specSize is " + specSize);
        switch (MeasureSpec.getMode(measureSpec)) {
            case MeasureSpec.AT_MOST:
                // The size of the view will be minimum necessary to fit our content and not
                // get out of specified borders
                Timber.d("Measurespec is AT_MOST");
                return Math.min(specSize, contentSize);
            case MeasureSpec.UNSPECIFIED:
                Timber.d("Measurespec is UNSPECIFIED");
                return contentSize;
            case MeasureSpec.EXACTLY:
                Timber.d("Measurespec is EXACTLY");
                return specSize;
            default:
                return 0;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Timber.d("onDraw, canvas size: " + canvas.getWidth() + "x" + canvas.getHeight());
        // Draw a series of concentric circles, smallest to largest, alternating colors
        mPaint.setColor(Color.RED);
        canvas.drawCircle(mCenter.x, mCenter.y, mRadius, mPaint);
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(mCenter.x, mCenter.y, mRadius * 0.8f,
                mPaint);
        mPaint.setColor(Color.BLUE);
        canvas.drawCircle(mCenter.x, mCenter.y, mRadius * 0.6f,
                mPaint);
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(mCenter.x, mCenter.y, mRadius * 0.4f,
                mPaint);
        mPaint.setColor(Color.RED);
        canvas.drawCircle(mCenter.x, mCenter.y, mRadius * 0.2f,
                mPaint);
    }
}
