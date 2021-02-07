package com.hly.surfaceview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CustomSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private static final String TAG = CustomSurfaceView.class.getName();

    private SurfaceHolder mSurfaceHolder;
    private boolean bDrawing = false;
    private int progress;
    private Paint mPaint;
    private Paint bgPaint;
    Canvas canvas;

    public CustomSurfaceView(Context context) {
        super(context);
        initView();
    }

    public CustomSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CustomSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        // 初始化进度圆画笔
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(20f);
        // 初始化背景
        bgPaint = new Paint();
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setStrokeCap(Paint.Cap.ROUND);
        bgPaint.setAntiAlias(true);
        bgPaint.setColor(Color.GRAY);
        bgPaint.setStrokeWidth(20f);

        this.mSurfaceHolder = getHolder();
        this.mSurfaceHolder.addCallback(this);
        //画布透明处理
        this.setZOrderOnTop(true);
        this.mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);


    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.e(TAG, "surfaceCreated");

        // 开启绘制
        bDrawing = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.e(TAG, "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.e(TAG, "surfaceDestroyed");
        //停止绘制
        bDrawing = false;
    }

    @Override
    public void run() {
        //不停的绘制
        while (bDrawing) {
            //绘制刷新处理
            Drawing();
            try {
                // 控制刷新频率
                Thread.sleep(10);
                progress++;
                if (progress >= 100) {
                    progress = 0;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // 绘制圆
    private void Drawing() {
        try {
            canvas = mSurfaceHolder.lockCanvas();
            if (null != canvas) {
                synchronized (mSurfaceHolder) {
                    //清空画布-透明处理
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    int xCenter = getWidth() / 2;
                    int yCenter = getHeight() / 2;
                    int radius = (int) (xCenter - 10);
                    // 绘制背景
                    canvas.drawCircle(xCenter,yCenter,radius,bgPaint);
                    // 绘制进度圆
                    RectF rectF = new RectF(xCenter - radius, yCenter - radius, xCenter + radius,
                            yCenter + radius);
                    canvas.drawArc(rectF, -90, progress * 360 / 100, false, mPaint);
                }
            }
        } finally {
            if (null != mSurfaceHolder && null != canvas) {
                mSurfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
