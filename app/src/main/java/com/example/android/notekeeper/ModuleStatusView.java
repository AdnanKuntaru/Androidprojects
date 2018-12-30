package com.example.android.notekeeper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.ExploreByTouchHelper;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;

import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class ModuleStatusView extends View {
    private static final int EDIT_MODE_MODULE_COUNT = 7;
    public static final int SHAPE_CYCLE = 0;
    public static final float DEFAULT_OUTLINE_WIDTH_DP = 2f;
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;
    private float mOutLineWidth;
    private float mShapeSize;
    private float mSpacing;
    private Rect[] mModuleRectangle;
    private int mOutLineColor;
    private Paint mPaintOutline;
    private int mFillColor;
    private Paint mPaintFill;
    private float mRadius;
    private int EDIT;
    private int mMaxHorizontalModules;
    private int INVALID_iNDEX;
    private int mShape;
    private ModuleStatusAccessibilityHelper mAccessibilityHelper;
    //
//    private TextPaint mTextPaint;
//    private float mTextWidth;
//    private float mTextHeight;

    public boolean[] getmModuleStatus() {
        return mModuleStatus;
    }

    public void setmModuleStatus(boolean[] mModuleStatus) {
        this.mModuleStatus = mModuleStatus;
    }

    private boolean[] mModuleStatus;

    public ModuleStatusView(Context context) {
        super(context);
        init(null, 0);
    }

    public ModuleStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ModuleStatusView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }


    private void init(AttributeSet attrs, int defStyle) {
        if (isInEditMode())
            setupEditModeValue();

        setFocusable(true);
        mAccessibilityHelper = new ModuleStatusAccessibilityHelper(this);
        ViewCompat.setAccessibilityDelegate(this, mAccessibilityHelper);

        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        float displayDensity = dm.density;
        float defaultOutlineWithPixels = displayDensity * DEFAULT_OUTLINE_WIDTH_DP;
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ModuleStatusView, defStyle, 0);

        mOutLineColor = a.getColor(R.styleable.ModuleStatusView_outlineColor, Color.BLACK);
        mShape = a.getInt(R.styleable.ModuleStatusView_shape, SHAPE_CYCLE);
        mOutLineWidth = a.getDimension(R.styleable.ModuleStatusView_outlineWidth, defaultOutlineWithPixels);
        a.recycle();


        mShapeSize = 144f;
        mSpacing = 30f;
        mRadius = (mShapeSize - mOutLineWidth) / 2;


        mPaintOutline = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintOutline.setStyle(Paint.Style.STROKE);
        mPaintOutline.setStrokeWidth(mOutLineWidth);
        mPaintOutline.setColor(mOutLineColor);

        mFillColor = getContext().getResources().getColor(R.color.plural_sight);
        mPaintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintFill.setStyle(Paint.Style.FILL);
        mPaintFill.setColor(mFillColor);

    }


    @Override
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusRect ) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusRect);

        mAccessibilityHelper.onFocusChanged(gainFocus, direction, previouslyFocusRect);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return mAccessibilityHelper.dispatchKeyEvent(event) ||
                super.dispatchKeyEvent(event);
    }

    @Override
    protected boolean dispatchHoverEvent(MotionEvent event) {
        return mAccessibilityHelper.dispatchHoverEvent(event) || super.dispatchHoverEvent(event);
    }

    private void setupEditModeValue() {
        boolean[] exampleModuleValue = new boolean[EDIT_MODE_MODULE_COUNT];
        int middle = EDIT_MODE_MODULE_COUNT / 2;

        for (int i = 0; i < middle; i++)
            exampleModuleValue[i] = true;
        setmModuleStatus(exampleModuleValue);

    }

    private void setupMethodRectangules(int width) {

        int availableWidth = width - getPaddingLeft() - getPaddingRight();
        int horizontalModulesThatCanFit = (int) (availableWidth / (mShapeSize + mSpacing));
        int maxHorizontalModule = Math.min(horizontalModulesThatCanFit, mModuleStatus.length);

        mModuleRectangle = new Rect[mModuleStatus.length];

        for (int moduleIndex = 0; moduleIndex < mModuleStatus.length; moduleIndex++) {
            int column = moduleIndex % mMaxHorizontalModules;
            int row = moduleIndex / mMaxHorizontalModules;

            int x = getPaddingLeft() + (int) (column * (mShapeSize + mSpacing));
            int y = getPaddingTop() + (int) (row * (mShapeSize + mSpacing));
            ;


            mModuleRectangle[moduleIndex] = new Rect(x, y, x + (int) mShapeSize, y + (int) mShapeSize);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWitdth = 0;
        int desiredHeight = 0;

        int specWidth = MeasureSpec.getSize(widthMeasureSpec);
        int availableWidth = specWidth - getPaddingLeft() - getPaddingRight();
        int horizontalModuleThatCanFit = (int) (availableWidth / (mShapeSize + mSpacing));
        mMaxHorizontalModules = Math.min(horizontalModuleThatCanFit, mModuleStatus.length);

        desiredWitdth = (int) ((mModuleStatus.length * (mShapeSize + mSpacing)) - +-mSpacing);
        desiredWitdth += getPaddingLeft() + getPaddingRight();

        int rows = ((mModuleStatus.length - 1) / mMaxHorizontalModules) + 1;
        desiredHeight = (int) (rows * (mShapeSize + mSpacing) - mSpacing);
        desiredHeight += getPaddingTop() + getPaddingRight();

        int width = resolveSizeAndState(desiredWitdth, widthMeasureSpec, 0);
        int height = resolveSizeAndState(desiredHeight, heightMeasureSpec, 0);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        setupMethodRectangules(w);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int moduleIndex = 0; moduleIndex < mModuleStatus.length; moduleIndex++) {
            if (mShape == SHAPE_CYCLE) {
                float x = mModuleRectangle[moduleIndex].centerX();
                float y = mModuleRectangle[moduleIndex].centerY();


                if (mModuleStatus[moduleIndex])
                    canvas.drawCircle(x, y, mRadius, mPaintFill);
                canvas.drawCircle(x, y, mRadius, mPaintOutline);
            } else {
//                    drawSquare(canvas, moduleIndex);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_UP:
                int moduleIndex = findItemAtPoint(event.getX(), event.getY());

                onModuleSelected(moduleIndex);
                return true;
        }
        return super.dispatchTouchEvent(event);
    }

    private void onModuleSelected(int moduleIndex) {

        if (moduleIndex == INVALID_iNDEX)
            return;

        mModuleStatus[moduleIndex] = !mModuleStatus[moduleIndex];
        invalidate();

        mAccessibilityHelper.invalidateVirtualView(moduleIndex);
        mAccessibilityHelper.sendEventForVirtualView(moduleIndex, AccessibilityEvent.TYPE_VIEW_CLICKED);

    }

    private int findItemAtPoint(float x, float y) {
        INVALID_iNDEX = 1;
        int moduleIndex = INVALID_iNDEX;
        for (int i = 0; i < mModuleRectangle.length; i++) {
            if (mModuleRectangle[i].contains((int) x, (int) y)) ;
            {
                moduleIndex = i;
            }
            break;
        }

        return moduleIndex;
    }

    private class ModuleStatusAccessibilityHelper extends ExploreByTouchHelper {
        /**
         * Constructs a new helper that can expose a virtual view hierarchy for the
         * specified host view.
         *
         * @param host view whose virtual view hierarchy is exposed by this helper
         */
        public ModuleStatusAccessibilityHelper(View host) {
            super(host);
        }

        @Override
        protected int getVirtualViewAt(float x, float y) {
            int moduleIndex = findItemAtPoint(x, y);
            return moduleIndex == INVALID_iNDEX? ExploreByTouchHelper.INVALID_ID:moduleIndex;
        }


        @Override
        protected void getVisibleVirtualViews(List<Integer> virtualViewIds) {
            if (mModuleRectangle == null)
                return;
            for (int moduleIndex = 0; moduleIndex < mModuleStatus.length; moduleIndex++)
                virtualViewIds.add(moduleIndex);

        }

        @Override
        protected void onPopulateNodeForVirtualView(int virtualViewId, AccessibilityNodeInfoCompat node) {
            node.setFocusable(true);
            node.setBoundsInParent(mModuleRectangle[virtualViewId]);
            node.setContentDescription("Module" + virtualViewId);
            node.setCheckable(true);
            node.setChecked(mModuleStatus[virtualViewId]);
            node.addAction(AccessibilityNodeInfoCompat.ACTION_CLICK);

        }

        @Override
        protected boolean onPerformActionForVirtualView(int virtualViewId, int action, Bundle arguments) {
            switch (action){
                case AccessibilityNodeInfoCompat.ACTION_CLICK:
                    onModuleSelected(virtualViewId);
                    return true;
            }
            return false;
        }
    }
}