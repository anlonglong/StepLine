package com.example.steplinelibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by anlonglong on 2018/4/9.
 * Email： 940752944@qq.com
 */

@SuppressWarnings("unused")
public class StepLine extends View {

    private int mCenterLineWidth;
    private int mCircleRadius;
    private int mEdgeLineWidth;
    private Paint mPaint;
    private int mCenterLineColor;
    private int mCenterLineThick;
    private int mBottomItemDescTextZise;
    private int mPadding;
    private Paint.FontMetrics mFontMetrics;
    private int mTopItemNameSize;
    private int mCheckCircleColor;
    private int mUnCheckCircleColor;
    private String[] mTopName;
    private String[] mBottomDescription;
    private int mCurrentStep;
    private int mStepCount;
    private int mCheckBottomItemDescColor;
    private int mUnCheckBottomItemDescColor;
    private int mCheckTopNameColor;
    private int mUnCheckTopNameColor;
    private int mCheckStrokeColor;
    private int mUnCheckStrokeColor;


    public StepLine(Context context) {
        this(context, null);
    }

    public StepLine(Context context,  AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepLine(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StepLine);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        mCenterLineWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimension(R.styleable.StepLine_center_line_width, 0), displayMetrics);
        mCenterLineColor = typedArray.getColor(R.styleable.StepLine_center_line_color, Color.BLACK);
        mCircleRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimension(R.styleable.StepLine_circle_radius, 0), displayMetrics);
        mPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimension(R.styleable.StepLine_text_padding, 10), context.getResources().getDisplayMetrics());
        mEdgeLineWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimension(R.styleable.StepLine_edge_line_width, 0), displayMetrics);
        mCenterLineThick = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimension(R.styleable.StepLine_center_line_thick, 1), displayMetrics);

        int bottomId = typedArray.getResourceId(R.styleable.StepLine_bottom_description, 0);
        if (0 != bottomId) {
            mBottomDescription = getResources().getStringArray(bottomId);
        }
        //步骤描述大小
        mBottomItemDescTextZise = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimension(R.styleable.StepLine_bottom_item_description_size, 8), displayMetrics);

        mCheckBottomItemDescColor = typedArray.getColor(R.styleable.StepLine_check_bottom_item_description_color, Color.GRAY);
        mUnCheckBottomItemDescColor = typedArray.getColor(R.styleable.StepLine_uncheck_bottom_item_description_color, Color.WHITE);


        //步骤描述对应的数字
        mTopItemNameSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimension(R.styleable.StepLine_top_item_name_size, 6), displayMetrics);
        int topId = typedArray.getResourceId(R.styleable.StepLine_top_name, 0);
        if (0 != topId) {
            mTopName = getResources().getStringArray(topId);
        }

        mCheckTopNameColor = typedArray.getColor(R.styleable.StepLine_check_top_name_color, Color.BLACK);
        mUnCheckTopNameColor = typedArray.getColor(R.styleable.StepLine_uncheck_top_name_color, Color.BLACK);

        //选中和未选中的圆圈的填充颜色
        mCheckCircleColor = typedArray.getColor(R.styleable.StepLine_check_circle_color, Color.GRAY);
        mUnCheckCircleColor = typedArray.getColor(R.styleable.StepLine_uncheck_circle_color, Color.WHITE);

        mCheckStrokeColor = typedArray.getColor(R.styleable.StepLine_check_stroke_color, Color.BLACK);
        mUnCheckStrokeColor = typedArray.getColor(R.styleable.StepLine_uncheck_stroke_color, Color.BLACK);

        mCurrentStep = typedArray.getInteger(R.styleable.StepLine_current_step, 1);
        mStepCount = typedArray.getInteger(R.styleable.StepLine_step_count, 2);

        //mTopName = typedArray.getTextArray(R.styleable.PassWorldLine_top_name);

        //mBottomDescription = typedArray.getTextArray(R.styleable.PassWorldLine_bottom_description);


        typedArray.recycle();
        initPaint();

        mFontMetrics = mPaint.getFontMetrics();

    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mFontMetrics = mPaint.getFontMetrics();

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWith(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWith(int widthMeasureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int with = MeasureSpec.getSize(widthMeasureSpec);
        switch (mode) {
            case MeasureSpec.EXACTLY:
                result = with;
                mCenterLineWidth = with - getPaddingLeft() - getPaddingRight() - 2 * mEdgeLineWidth - 3 * 2 * mCircleRadius;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                int realWidth = getPaddingLeft() + getPaddingRight() + 2 * mEdgeLineWidth + 2 * mCircleRadius * 3 + 2 * mCenterLineWidth;
                result = Math.min(realWidth, with);
                break;
        }
        return result;
    }


    private int measureHeight(int heightMeasureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        switch (mode) {
            case MeasureSpec.EXACTLY:
                result = height;
                break;
            case MeasureSpec.AT_MOST:
                //case MeasureSpec.UNSPECIFIED:
                int realHeight = ((int) (getPaddingTop() + getPaddingBottom() + 2 * mCircleRadius + /**底部文字高度**/(mFontMetrics.bottom - mFontMetrics.top))) + mPadding; //
                result = Math.min(realHeight, height);
                break;
        }
        return result;


    }


    private int toPx(float dimen) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, dimen, getResources().getDisplayMetrics());
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (mCenterLineThick != 0) {
            mPaint.setStrokeWidth(mCenterLineThick);
        }
        mPaint.setColor(mCenterLineColor);
        float startX = getPaddingLeft();
        float startY = getPaddingTop() + mCircleRadius;
        float endX = getMeasuredWidth() - getPaddingRight();
        float endY = startY;
        canvas.drawLine(startX, startY, endX, endY, mPaint);
        for (int i = 0; i < mStepCount; i++) {
            int cx = getPaddingLeft() + mEdgeLineWidth + mCircleRadius * (2 * i + 1) + mCenterLineWidth * i;
            int screenWidth = ((WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
             if (cx >= screenWidth) {
                 return;
             }
            int cy = getPaddingTop() + mCircleRadius;
            float topNameItemWidth = mPaint.measureText(mTopName[i]);
            float rectTopNameHeight = mFontMetrics.bottom - mFontMetrics.top;
            if (i == mCurrentStep) {
                //画实心圆
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(mCheckCircleColor);
                canvas.drawCircle(cx, cy, mCircleRadius, mPaint);
                //画描边
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(mCheckStrokeColor);
                mPaint.setStrokeWidth(2);
                canvas.drawCircle(cx, cy, mCircleRadius, mPaint);
                //画数字
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setTextSize(mTopItemNameSize);
                mPaint.setColor(mCheckTopNameColor);
                canvas.drawText(mTopName[i], cx - topNameItemWidth / 2, cy + rectTopNameHeight / 2 + mFontMetrics.bottom, mPaint);
                invalidate();
            } else {
                //画实心圆
                mPaint.setColor(mUnCheckCircleColor);
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(cx, cy, mCircleRadius, mPaint);

                //画描边
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(mUnCheckStrokeColor);
                mPaint.setStrokeWidth(2);
                canvas.drawCircle(cx, cy, mCircleRadius, mPaint);
                //画圆中的步骤数字
                mPaint.setTextSize(mTopItemNameSize);
                mPaint.setColor(mUnCheckTopNameColor);
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawText(mTopName[i], cx - topNameItemWidth / 2, cy + rectTopNameHeight / 2 + mFontMetrics.bottom, mPaint);
            }
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setTextSize(mBottomItemDescTextZise);
            mPaint.setColor(i == mCurrentStep ? mCheckBottomItemDescColor : mUnCheckBottomItemDescColor);
            float descWith = mPaint.measureText(mBottomDescription[i]);//文字的宽度
            canvas.drawText(mBottomDescription[i], cx - descWith / 2, getHeight() - getPaddingBottom() - (mFontMetrics.bottom), mPaint);
        }
    }

    public int getCenterLineWidth() {
        return mCenterLineWidth;
    }

    public void setCenterLineWidth(int centerLineWidth) {
        mCenterLineWidth = centerLineWidth;
        invalidate();
    }

    public int getCircleRadius() {
        return mCircleRadius;
    }

    public void setCircleRadius(int circleRadius) {
        mCircleRadius = circleRadius;
        invalidate();
    }

    public int getEdgeLineWidth() {
        return mEdgeLineWidth;
    }

    public void setEdgeLineWidth(int edgeLineWidth) {
        mEdgeLineWidth = edgeLineWidth;
        invalidate();
    }

    public int getCenterLineColor() {
        return mCenterLineColor;
    }

    public void setCenterLineColor(int centerLineColor) {
        mCenterLineColor = centerLineColor;
        invalidate();
    }

    public int getCenterLineThick() {
        return mCenterLineThick;
    }

    public void setCenterLineThick(int centerLineThick) {
        mCenterLineThick = centerLineThick;
        invalidate();
    }

    public int getBottomItemDescTextZise() {
        return mBottomItemDescTextZise;
    }

    public void setBottomItemDescTextZise(int bottomItemDescTextZise) {
        mBottomItemDescTextZise = bottomItemDescTextZise;
        invalidate();
    }

    public int getPadding() {
        return mPadding;
    }

    public void setPadding(int padding) {
        mPadding = padding;
        invalidate();
    }

    public int getTopItemNameSize() {
        return mTopItemNameSize;
    }

    public void setTopItemNameSize(int topItemNameSize) {
        mTopItemNameSize = topItemNameSize;
        invalidate();
    }

    public int getCheckCircleColor() {
        return mCheckCircleColor;
    }

    public void setCheckCircleColor(int checkCircleColor) {
        mCheckCircleColor = checkCircleColor;
        invalidate();
    }

    public int getUnCheckCircleColor() {
        return mUnCheckCircleColor;
    }

    public void setUnCheckCircleColor(int unCheckCircleColor) {
        mUnCheckCircleColor = unCheckCircleColor;
        invalidate();
    }

    public String[] getTopName() {
        return mTopName;
    }

    public void setTopName(String[] topName) {
        mTopName = topName;
        invalidate();
    }

    public void setTopName( int resIdopName) {
        mTopName = getStringArray(resIdopName);
        invalidate();
    }


    private String[] getStringArray( int resIdopName) {
        return getResources().getStringArray(resIdopName);
    }


    public String[] getBottomDescription() {
        return mBottomDescription;
    }

    public void setBottomDescription(String[] bottomDescription) {
        mBottomDescription = bottomDescription;
        invalidate();
    }


    public void setBottomDescription( int resIdottomDescription) {
        mBottomDescription = getStringArray(resIdottomDescription);
        invalidate();
    }

    public int getCurrentStep() {
        return mCurrentStep;
    }

    public void setCurrentStep(int currentStep) {
        mCurrentStep = currentStep;
        invalidate();
    }

    public int getStepCount() {
        return mStepCount;
    }

    public void setStepCount(int stepCount) {
        mStepCount = stepCount;
        invalidate();
    }

    public int getCheckBottomItemDescColor() {
        return mCheckBottomItemDescColor;
    }

    public void setCheckBottomItemDescColor(int checkBottomItemDescColor) {
        mCheckBottomItemDescColor = checkBottomItemDescColor;
        invalidate();
    }

    public int getUnCheckBottomItemDescColor() {
        return mUnCheckBottomItemDescColor;
    }

    public void setUnCheckBottomItemDescColor(int unCheckBottomItemDescColor) {
        mUnCheckBottomItemDescColor = unCheckBottomItemDescColor;
        invalidate();
    }

    public int getCheckTopNameColor() {
        return mCheckTopNameColor;
    }

    public void setCheckTopNameColor(int checkTopNameColor) {
        mCheckTopNameColor = checkTopNameColor;
        invalidate();
    }

    public int getUnCheckTopNameColor() {
        return mUnCheckTopNameColor;
    }

    public void setUnCheckTopNameColor(int unCheckTopNameColor) {
        mUnCheckTopNameColor = unCheckTopNameColor;
        invalidate();
    }

    public int getCheckStrokeColor() {
        return mCheckStrokeColor;
    }

    public void setCheckStrokeColor(int checkStrokeColor) {
        mCheckStrokeColor = checkStrokeColor;
        invalidate();
    }

    public int getUnCheckStrokeColor() {
        return mUnCheckStrokeColor;
    }

    public void setUnCheckStrokeColor(int unCheckStrokeColor) {
        mUnCheckStrokeColor = unCheckStrokeColor;
        invalidate();

    }

    /**
     * 绘制文字的时候要注意：
     *    文字有宽度和高度，文字从左下角开始绘制
     *    文字画笔的样式最好是Paint.Style.FILL的
     */
}
