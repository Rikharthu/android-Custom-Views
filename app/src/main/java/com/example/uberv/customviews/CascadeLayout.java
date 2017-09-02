package com.example.uberv.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import timber.log.Timber;

public class CascadeLayout extends ViewGroup {

    private int mHorizontalSpacing;
    private int mVerticalSpacing;

    // XML constructor
    public CascadeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Obtain attributes defined in res/values/attrs.html from the passed AttributeSet
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CascadeLayout);

        try {
            mHorizontalSpacing = a.getDimensionPixelSize(R.styleable.CascadeLayout_horizontal_spacing,
                    getResources().getDimensionPixelSize(R.dimen.cascade_horizontal_spacing)); // default value
            mVerticalSpacing = a.getDimensionPixelSize(R.styleable.CascadeLayout_vertical_spacing,
                    getResources().getDimensionPixelSize(R.dimen.cascade_vertical_spacing));
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // use width and height to calculate layout's final size and children's x and y positions
        int width = 0;
        int height = getPaddingTop();

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);

            // Make every child measure itself
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            int verticalSpacing = mVerticalSpacing;

            LayoutParams params = (LayoutParams) child.getLayoutParams();
            // check if child's layout params containt our custom layout_vertical_spacing attribute
            if (params.verticalSpacing >= 0) {
                verticalSpacing = params.verticalSpacing;
            }

            width = getPaddingLeft() + mHorizontalSpacing * i;
            params.x = width;
            params.y = height;

            width += child.getMeasuredWidth();
            height += verticalSpacing;

            Timber.d("i=" + i + ", width=" + width + ", height=" + height);
        }

        width += getPaddingRight();
        height += getChildAt(getChildCount() - 1).getMeasuredHeight() + getPaddingBottom();

        setMeasuredDimension(resolveSize(width, widthMeasureSpec), resolveSize(height, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            // use values calculated from onMeasure() method
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            child.layout(
                    lp.x, lp.y, lp.x + child.getMeasuredWidth(),
                    lp.y + child.getMeasuredHeight()
            );
        }
    }

    // These methods need to be overriden to support our custom LayoutParams
    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    /**
     * Custom LayoutParams class that will hold the x,y position values of each child
     */
    public static class LayoutParams extends ViewGroup.LayoutParams {

        int x, y, verticalSpacing;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            TypedArray a = c.obtainStyledAttributes(attrs,
                    R.styleable.CascadeLayout_LayoutParams);

            try {
                verticalSpacing = a.getDimensionPixelSize(
                        R.styleable.CascadeLayout_LayoutParams_layout_vertical_spacing, -1
                );
            } finally {
                a.recycle();
            }
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}
