package com.example.jacky.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by anlonglong on 2018/4/1.
 * Email： 940752944@qq.com
 */

public class ReflectView extends View {

    private Bitmap mSrcBitmap;
    private Bitmap mRefBitmap;
    private Paint mPaint;
    private PorterDuffXfermode mPorterDuffXfermode;

    public ReflectView(Context context) {
        this(context,null);
    }

    public ReflectView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ReflectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mSrcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        Matrix matrix = new Matrix();
        matrix.setScale(1f,-1f);
        mRefBitmap = Bitmap.createBitmap(mSrcBitmap, 0, 0, mSrcBitmap.getWidth(), mSrcBitmap.getHeight(), matrix, true);
        mPaint = new Paint();
        mPaint.setShader(new LinearGradient(0, mSrcBitmap.getWidth(), mSrcBitmap.getHeight(), mSrcBitmap.getHeight()+ mSrcBitmap.getHeight()/4,Color.CYAN,Color.BLUE, Shader.TileMode.CLAMP));
        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(mSrcBitmap,0,0,null);
        canvas.drawBitmap(mRefBitmap,0,mSrcBitmap.getHeight(),null);
        mPaint.setXfermode(mPorterDuffXfermode);
        canvas.drawRect(0,mSrcBitmap.getHeight(),mRefBitmap.getWidth(), (float) (mSrcBitmap.getHeight()*1.5),mPaint);
        mPaint.setXfermode(null);
    }
}
