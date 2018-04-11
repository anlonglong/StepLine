package com.example.jacky.myapplication;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.SeekBar;

import java.util.Set;

public class Main2Activity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private ImageView mImageView;
    private float mHue;
    private float mS;
    private float mL;
    private static int MAX_VALUE = 255;
    private static int MID_VALUE = 127;
    private Bitmap mBitmap;
    private SeekBar mSeekbarhue, mSeekbarSaturation, mSeekbarLum;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //method();
        Intent intent = getIntent();

        WebView viewById = (WebView) findViewById(R.id.web_view);
        viewById.loadUrl("file:///android_asset/start.html");
        if (null != intent) {
            Uri data = intent.getData();
            if (null == data) return;
            String path = data.getPath();
            String authority = data.getAuthority();
            String encodedQuery = data.getEncodedQuery();
            String encodedAuthority = data.getEncodedAuthority();
            String encodedSchemeSpecificPart = data.getEncodedSchemeSpecificPart();
            String host = data.getHost();
            Set<String> queryParameterNames = data.getQueryParameterNames();

        }


    }

    private void method() {
        mSeekbarhue = (SeekBar) findViewById(R.id.hue);
        mSeekbarSaturation = (SeekBar) findViewById(R.id.saturation);
        mSeekbarLum = (SeekBar) findViewById(R.id.lum);
        mSeekbarhue.setOnSeekBarChangeListener(this);
        mSeekbarSaturation.setOnSeekBarChangeListener(this);
        mSeekbarLum.setOnSeekBarChangeListener(this);
        mImageView = (ImageView) findViewById(R.id.iv);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dem);
        Bitmap bitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawRoundRect(0,0,mBitmap.getWidth(),mBitmap.getHeight(),100,100,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(mBitmap,0,0,paint);
        mImageView.setImageBitmap(bitmap);

        mSeekbarhue.setMax(MAX_VALUE);
        mSeekbarSaturation.setMax(MAX_VALUE);
        mSeekbarLum.setMax(MAX_VALUE);
        mSeekbarhue.setProgress(MID_VALUE);
        mSeekbarSaturation.setProgress(MID_VALUE);
        mSeekbarLum.setProgress(MID_VALUE);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.hue:
                mHue = (progress -MID_VALUE)*1.0f/MID_VALUE*180;

                break;
            case R.id.saturation:
                mS = progress*1.0f/MID_VALUE;
                break;
            case R.id.lum:
                mL = progress*1.0f/MID_VALUE;
                break;
        }
        //mImageView.setImageBitmap(handleImageEffect(mBitmap,mHue,mS,mL));
    }

    private Bitmap handleImageEffect(Bitmap bm, float hue, float stau, float lum) {
        //Android不允许在原图上面做修改，必须要创建副本
        Bitmap bitmap = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);//把画纸传进去
        Paint paint = new Paint();//画笔
        //为RGB三个色道设置色调
        ColorMatrix hColorMatrix = new ColorMatrix();
        hColorMatrix.setRotate(0,hue);
        hColorMatrix.setRotate(1,hue);
        hColorMatrix.setRotate(2,hue);

        //设置饱和度
        ColorMatrix sColorMatrix = new ColorMatrix();
        sColorMatrix.setSaturation(stau);


        //这只亮度
        ColorMatrix lColorMatrix = new ColorMatrix();
        lColorMatrix.setScale(lum,lum,lum,1);


        //把上面的三种属性矩阵的乘法组合在一起
        ColorMatrix imageColorMatrix = new ColorMatrix();
        imageColorMatrix.postConcat(hColorMatrix);
        imageColorMatrix.postConcat(sColorMatrix);
        imageColorMatrix.postConcat(lColorMatrix);

        //用这个画笔来画原图像，将上面的属性的混合效果作用在上面。
        paint.setColorFilter(new ColorMatrixColorFilter(imageColorMatrix));
        canvas.drawBitmap(bm,0,0,paint);
        return bitmap;
    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
