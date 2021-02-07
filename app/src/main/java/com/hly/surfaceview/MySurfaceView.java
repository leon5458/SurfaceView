package com.hly.surfaceview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder mSurfaceHolder;
    private Canvas mCanvas;
    private Paint paint;

    public MySurfaceView(Context context) {
        super(context);
        initView();

    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
        // 返回SurfaceHolder，提供对该SurfaceView的基础表面的访问和控制
        this.mSurfaceHolder = getHolder();
        //注册回调方法
        this.mSurfaceHolder.addCallback(this);
        //画布透明处理
        this.setZOrderOnTop(true);
        this.mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Surface创建时触发
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Surface改变时触发
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface销毁时触发
    }

    @Override
    public void run() {
        Drawing();
    }

    // 绘制圆
    private void Drawing() {
        mCanvas = mSurfaceHolder.lockCanvas();
        if (null != mCanvas) {
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            mCanvas.drawCircle(200, 200, 200, paint);
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }

    }

}
